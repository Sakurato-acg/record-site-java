package top.recordsite.exception;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 网址校验规则
 */
@Data
public class UrlValidator implements ConstraintValidator<Url, String> {

    private boolean required = false;// 是否强制校验

    @Override
    public void initialize(Url constraintAnnotation) {
        this.required=constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        String regex= "^(https|http|ftp)\\:\\/\\/[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(\\/[\\S]*)?$";
        Pattern pattern=Pattern.compile(regex);
        if (ObjectUtils.isEmpty(url)){
            return true;
        }
        return pattern.matcher(url).matches();
    }
}
