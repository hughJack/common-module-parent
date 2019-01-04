package cn.com.flaginfo.module.common.validator;

import cn.com.flaginfo.module.common.utils.ValidatorUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机验证器
 * @author: Meng.Liu
 * @date: 2018/12/17 下午2:19
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = Mobile.MobileValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mobile {

    String message() default "ILLEGAL_MOBILE_FORMAT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class MobileValidator implements ConstraintValidator<Mobile, String>{

        @Override
        public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
            return ValidatorUtils.checkMobile(mobile);
        }
    }

}
