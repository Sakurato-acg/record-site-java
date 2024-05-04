package top.recordsite.utils;


import lombok.extern.slf4j.Slf4j;
import top.recordsite.exception.SystemException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 参数校验工具类
 * NOTE： 具体使用参照测试类
 *
 * @since 2019/5/7
 */
@Slf4j
public class ValidationUtil {

    private final static String msg = "校验不通过";
    /**
     * 全部校验
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtil() {
    }

    /**
     * 注解验证参数(全部校验)
     *
     * @param obj
     */
    public static <T> void allCheckValidate(T obj) {

        if (obj == null) {
            throw new SystemException(400, msg + "数据为空");
        }
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        //返回异常result
        if (constraintViolations.size() > 0) {

            StringBuilder buffer = new StringBuilder();
            for (ConstraintViolation<T> item : constraintViolations) {
                String errorForAdmin = "%s类的%s属性,数据校验不通过,错误为%s".formatted(
                        item.getRootBeanClass().getCanonicalName(),
                        item.getPropertyPath(),
                        item.getMessage()
                );
                String errorForUser = "%s\n".formatted(
                        item.getMessage()
                );
                log.error(errorForAdmin);
                buffer.append(errorForUser).append(";");
            }

            throw new SystemException(400,buffer.toString());
        }
    }
}
