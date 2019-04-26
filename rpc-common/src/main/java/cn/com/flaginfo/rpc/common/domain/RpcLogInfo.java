package cn.com.flaginfo.rpc.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;

/**
 * RPC日志对象
 *
 * @author meng.liu
 */
@Getter
@Setter
@ToString
public class RpcLogInfo implements Serializable {

    private RpcLogInfo(String serviceName, String interfaceName, String methodName, Class<?>[] parameterTypes, Object[] args, Class<?> returnType, Object result, Long invokeStartTimestamp, Long takeTime, boolean isSuccess) {
        this.serviceName = serviceName;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
        this.returnType = returnType;
        this.result = result;
        this.invokeStartTimestamp = invokeStartTimestamp;
        this.takeTime = takeTime;
        this.isSuccess = isSuccess;
    }

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

    public String getParameterTypes() {
        if( null == this.parameterTypes || this.parameterTypes.length == 0){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : this.parameterTypes) {
            builder.append(clazz.getSimpleName()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }

    public static RpcLogInfoBuilder builder(){
        return new RpcLogInfoBuilder();
    }

    public static class RpcLogInfoBuilder {

        private String serviceName;
        private String interfaceName;
        private String methodName;
        private Class<?>[] parameterTypes;
        private Object[] args;
        private Class<?> returnType;
        private Object result;
        private Long invokeStartTimestamp;
        private Long takeTime;
        private boolean isSuccess;

        public RpcLogInfoBuilder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RpcLogInfoBuilder interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public RpcLogInfoBuilder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public RpcLogInfoBuilder parameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
            return this;
        }

        public RpcLogInfoBuilder args(Object[] args) {
            this.args = args;
            return this;
        }

        public RpcLogInfoBuilder returnType(Class<?> returnType) {
            this.returnType = returnType;
            return this;
        }

        public RpcLogInfoBuilder result(Object result) {
            this.result = result;
            return this;
        }

        public RpcLogInfoBuilder invokeStartTimestamp(Long invokeStartTimestamp) {
            this.invokeStartTimestamp = invokeStartTimestamp;
            return this;
        }

        public RpcLogInfoBuilder takeTime(Long endTime) {
            this.takeTime = endTime - this.invokeStartTimestamp;
            return this;
        }

        public RpcLogInfoBuilder isSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
            return this;
        }

        public RpcLogInfo build() {
            return new RpcLogInfo(this.serviceName, this.interfaceName, this.methodName, this.parameterTypes, this.args, this.returnType, this.result, this.invokeStartTimestamp, this.takeTime, this.isSuccess);
        }

        @Override
        public String toString() {
            return "RpcLogInfo.RpcLogInfoBuilder(serviceName=" + this.serviceName
                    + ", interfaceName=" + this.interfaceName
                    + ", methodName=" + this.methodName
                    + ", parameterTypes=" + Arrays.deepToString(this.parameterTypes)
                    + ", args=" + Arrays.deepToString(this.args)
                    + ", returnType=" + this.returnType
                    + ", result=" + this.result
                    + ", invokeStartTimestamp=" + this.invokeStartTimestamp
                    + ", takeTime=" + this.takeTime
                    + ", isSuccess=" + this.isSuccess + ")";
        }

    }

}