package cn.com.flaginfo.module.common.validator;

import cn.com.flaginfo.module.common.utils.ValidatorUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 电话验证器
 * @author: Meng.Liu
 * @date: 2018/12/17 下午2:19
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = Phone.PhoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Phone {

    String message() default "ILLEGAL_PHONE_FORMAT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PhoneValidator implements ConstraintValidator<Phone, String>{

        @Override
        public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
            return ValidatorUtils.checkPhone(mobile);
        }
    }

}
