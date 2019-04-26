package cn.com.flaginfo.module.common.annotation;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.module.common.domain.restful.HttpBaseVO;
import cn.com.flaginfo.module.common.domain.restful.HttpRequestVO;
import cn.com.flaginfo.module.common.domain.restful.RestfulResponse;
import cn.com.flaginfo.module.common.utils.RestfulResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author: Meng.Liu
 * @date: 2019/4/17 下午5:35
 */
@Component
@Aspect
@Slf4j
public class ValidParamsProcessor {

    @Pointcut(value = "@annotation(cn.com.flaginfo.module.common.annotation.ValidParams)")
    private void point() {
    }

    @Autowired
    private Validator validator;


    @Around("point()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Set<ConstraintViolation<Object>> errorSet;
        for (Object object : pjp.getArgs()) {
            if (object instanceof HttpRequestVO || object instanceof HttpBaseVO) {
                errorSet = validator.validate(args[0]);
                if (null != errorSet && errorSet.size() > 0) {
                    ConstraintViolation<Object> constraintViolation = errorSet.iterator().next();
                    return this.validErrorResult(args[0].getClass().getName(), constraintViolation.getPropertyPath().toString(), constraintViolation.getMessageTemplate());
                }
            }
        }
        return pjp.proceed();
    }

    /**
     * 服务不可用
     *
     * @return
     */
    public RestfulResponse validErrorResult(String objectName, String property, String messageTemplate) {
        ErrorCode restfulCode = ErrorCode.getErrorCode(messageTemplate);
        if (null != restfulCode) {
            log.error("valid params error. code : {}, message : {}", restfulCode.code(), restfulCode.message());
            return RestfulResponseUtils.error(restfulCode);
        } else {
            log.error("unknown error : class:[{}], field:[{}], errorCode:[{}]", objectName, property, messageTemplate);
            return RestfulResponseUtils.error(ErrorCode.UNKNOWN_ERROR.code(), "[" + messageTemplate + "]");
        }
    }

}
