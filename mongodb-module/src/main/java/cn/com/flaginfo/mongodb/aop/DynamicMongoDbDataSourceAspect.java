package cn.com.flaginfo.mongodb.aop;

import cn.com.flaginfo.mongodb.config.selector.MongodbSourceSelector;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 9:42
 */
@Aspect
@Component
public class DynamicMongoDbDataSourceAspect {

    @Pointcut("@annotation(cn.com.flaginfo.mongodb.aop.MongoSource)")
    private void daoMethod() { }

    @Pointcut("execution(* cn.com.flaginfo..*mongo*.dao.impl.*DaoImpl.*(..)) ||" +
            "execution(* cn.com.flaginfo..dao.*mongo*.impl.*DaoImpl.*(..))")
    private void daoClass() { }

    @Before("daoMethod() || daoClass()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        MongoSource mongoSource =  AnnotationUtils.findAnnotation(method, MongoSource.class);
        if( null == mongoSource ){
            mongoSource = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), MongoSource.class);
        }
        if( null != mongoSource ){
            if( StringUtils.isNotBlank(mongoSource.value())  ){
                MongodbSourceSelector.getInstance(false).select(mongoSource.value());
            }else{
                MongodbSourceSelector.getInstance(false).clearSelected();
            }
        }
    }
}