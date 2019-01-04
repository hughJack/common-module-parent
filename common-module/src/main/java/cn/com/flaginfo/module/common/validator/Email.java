package cn.com.flaginfo.module.common.validator;

import cn.com.flaginfo.module.common.utils.ValidatorUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 邮箱验证器
 * @author: Meng.Liu
 * @date: 2018/12/17 下午2:19
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = Email.EmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Email {

    String message() default "ILLEGAL_EMAIL_FORMAT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EmailValidator implements ConstraintValidator<Email, String>{

        @Override
        public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
            return ValidatorUtils.checkEmail(mobile);
        }
    }

}
