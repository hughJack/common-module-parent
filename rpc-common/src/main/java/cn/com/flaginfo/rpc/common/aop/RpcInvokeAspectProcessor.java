package cn.com.flaginfo.rpc.common.aop;

import cn.com.flaginfo.exception.rpc.RpcRuntimeException;
import cn.com.flaginfo.module.common.diamond.DiamondProperties;
import cn.com.flaginfo.module.common.diamond.PropertyChangeListener;
import cn.com.flaginfo.rpc.common.configuration.RpcConfiguration;
import com.alibaba.com.caucho.hessian.HessianException;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;

import static cn.com.flaginfo.rpc.common.configuration.RpcConfiguration.LOG_LEVEL_CONF;
import static cn.com.flaginfo.rpc.common.configuration.RpcConfiguration.SLOW_THRESHOLD_CONF;

/**
 * @author: Meng.Liu
 * @date: 2018/12/11 上午10:57
 */
@Slf4j
@Component
@Aspect
public class RpcInvokeAspectProcessor {

    @Autowired(required = false)
    private RpcConfiguration rpcConfiguration;

    /**
     * 成功
     */
    private static final String SUCCESS = "Success";
    /**
     * 失败
     */
    private static final String FAILED = "Failed";

    @PostConstruct
    private void init() {
        if (null == rpcConfiguration) {
            rpcConfiguration = new RpcConfiguration();
        }
        try{
            this.loadConfigListen();
        }catch (Exception e){
            log.warn("cannot find class [DiamondProperties.class], the log level cannot be config at the runtime.");
        }

    }

    private void loadConfigListen(){
        DiamondProperties.addChangeListener(new PropertyChangeListener() {
            @Override
            public String[] register() {
                return new String[]{SLOW_THRESHOLD_CONF, LOG_LEVEL_CONF};
            }

            @Override
            public void change(String key, Object oldValue, Object newValue, Map<String, Object> allConfig) {
                if (key.equals(SLOW_THRESHOLD_CONF)) {
                    rpcConfiguration.setSlowThreshold(Long.valueOf(newValue.toString()));
                } else if (key.equals(LOG_LEVEL_CONF)) {
                    rpcConfiguration.setLogLevel(RpcConfiguration.getLogLevelWithName(newValue.toString()));
                }
            }
        });
    }

    @Pointcut("@within(cn.com.flaginfo.rpc.common.aop.RpcInterface)")
    private void annotation() {
    }

    @Around("annotation()")
    public Object aroundPoint(ProceedingJoinPoint pjp) throws Throwable {
        LogInfo logInfo = null;
        boolean logOff = rpcConfiguration.getLogLevel() == RpcConfiguration.RpcLogLevel.Off;
        if (!logOff) {
            logInfo = new LogInfo();
            logInfo.setInvokeStartTimestamp(System.currentTimeMillis());
        }
        Class<?> clazz = pjp.getTarget().getClass();
        RpcInterface rpcInterface = AnnotationUtils.findAnnotation(clazz, RpcInterface.class);
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        if (!logOff) {
            logInfo.setServiceName(rpcInterface.service().name());
            logInfo.setInterfaceName(rpcInterface.serviceInterface().getSimpleName());
            logInfo.setMethodName(method.getName());
            if (rpcConfiguration.getLogLevel() == RpcConfiguration.RpcLogLevel.Detailed
                    || rpcConfiguration.getLogLevel() == RpcConfiguration.RpcLogLevel.Detailed_When_Slow) {
                logInfo.setParameterTypes(method.getParameterTypes());
                logInfo.setReturnType(method.getReturnType());
                logInfo.setArgs(pjp.getArgs());
            }
        }
        try {
            Object obj = pjp.proceed();
            if (!logOff) {
                logInfo.setResult(obj);
            }
            return obj;
        } catch (HessianException e) {
            log.error("Rpc Service HessianException, service:{}, exception:{}", logInfo.getServiceName(), e.getMessage());
            if (!logOff) {
                logInfo.setSuccess(false);
            }
            throw new RpcRuntimeException(rpcInterface.service().getErrorCode().code(), rpcInterface.service().getErrorCode().message());
        }
        catch (RpcException e) {
            log.error("Rpc Service RpcException, service:{}, exception:{}", logInfo.getServiceName(), e.getMessage());
            if (!logOff) {
                logInfo.setSuccess(false);
            }
            throw new RpcRuntimeException(rpcInterface.service().getErrorCode().code(), rpcInterface.service().getErrorCode().message());
        } catch (Exception e) {
            log.error("Rpc called failed, cause : {}", e.getMessage());
            if (!logOff) {
                logInfo.setSuccess(false);
            }
            throw e;
        } finally {
            if (!logOff) {
                logInfo.setTakeTime(System.currentTimeMillis() - logInfo.getInvokeStartTimestamp());
                switch (rpcConfiguration.getLogLevel()) {
                    case Detailed:
                        this.logDetailed(logInfo);
                        break;
                    case Detailed_When_Slow:
                        if (logInfo.getTakeTime() > rpcConfiguration.getSlowThreshold()) {
                            this.logDetailed(logInfo);
                        }
                        break;
                    case Succinct_When_Slow:
                        if (logInfo.getTakeTime() > rpcConfiguration.getSlowThreshold()) {
                            this.logSuccinct(logInfo);
                        }
                        break;
                    case Succinct:
                    default:
                        this.logSuccinct(logInfo);
                        break;
                }
                if (logInfo.getTakeTime() > rpcConfiguration.getSlowThreshold()) {
                    log.warn("Rpc invoke rpc is too slow, take {}ms.", logInfo.getTakeTime());
                }
            }
        }
    }


    private void logSuccinct(LogInfo logInfo) {
        log.info("Rpc called : [{}] [{}] [{}] {} take {}ms",
                logInfo.getServiceName(),
                logInfo.getInterfaceName(),
                logInfo.getMethodName(),
                logInfo.isSuccess() ? SUCCESS : FAILED,
                logInfo.getTakeTime());
    }

    private void logDetailed(LogInfo logInfo) {
        log.info("Rpc called : [{}] [{}] [{} {}({})] {} take {}ms",
                logInfo.getServiceName(),
                logInfo.getInterfaceName(),
                logInfo.getReturnType().getSimpleName(),
                logInfo.getMethodName(),
                logInfo.getParameterTypes(),
                logInfo.isSuccess() ? SUCCESS : FAILED,
                logInfo.getTakeTime());
        log.info("Rpc invoke args : {}", JSONObject.toJSONString(logInfo.getArgs()));
        log.info("Rpc invoke response : {}", JSONObject.toJSONString(logInfo.getResult()));
    }

    /**
     * 获取调用类的名称
     *
     * @param clazz
     * @return
     */
    private String getRpcInterfaceName(Class<?> clazz) {
        return clazz.getSimpleName()
                .replace("Wrap", "")
                .replace("Impl", "");
    }

    @Getter
    @Setter
    static class LogInfo {
        /**
         * 服务名称
         */
        private String serviceName;
        /**
         * 类名
         */
        private String interfaceName;
        /**
         * 方法名称
         */
        private String methodName;
        /**
         * 参数类型列表
         */
        private Class<?>[] parameterTypes;
        /**
         * 参数列表
         */
        private Object[] args;
        /**
         * 方法返回参数
         */
        private Class<?> returnType;
        /**
         * 返回结果
         */
        private Object result;
        /**
         * 调用时间搓
         */
        private Long invokeStartTimestamp;
        /**
         * 执行耗时
         */
        private Long takeTime;
        /**
         * 调用是否成功
         */
        private boolean isSuccess = true;

        private String getParameterTypes() {
            StringBuilder builder = new StringBuilder();
            for (Class<?> clazz : this.parameterTypes) {
                builder.append(clazz.getSimpleName()).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
            return builder.toString();
        }
    }
}
