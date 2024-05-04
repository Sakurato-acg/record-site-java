package top.recordsite.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.recordsite.vo.AppHttpCodeEnum;
import top.recordsite.vo.Result;

import java.util.List;

@Slf4j
@RestControllerAdvice
@SuppressWarnings("all")
public class GlobalExceptionHandler {


    @ExceptionHandler(BindException.class)
    public Result exceptionHandler(BindException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors.size() != 0) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    log.warn("Bad Request Parameters: dto entity [{}],field [{}],message [{}]", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
                    stringBuilder.append(fieldError.getDefaultMessage()).append(";");
                });
            }
        } else {
            log.error("req params error ! {}", e.getMessage());
        }
//                return Result.errorResult(AppHttpCodeEnum.USER_NOT_NULL,stringBuilder.toString());
        return Result.error(400, stringBuilder.toString());
    }

    @ExceptionHandler(SystemException.class)
    public Result systemExceptionHandler(SystemException e) {
        //打印异常信息
        log.error("出现了异常！ {}", e.getMsg());
        //从异常对象中获取提示信息封装返回
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e) {
        //打印异常信息
        log.error("出现了异常！ {}", e.getMsg());
        //从异常对象中获取提示信息封装返回
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result RequestBodyMissing(HttpMessageNotReadableException e) {
//        e.getMessage();
        log.error("Required request body is missing");
//        Map<String,String> map=new HashMap<>();
//        map.put("path",request.getURI().getPath());
        //400 Bad Request
        return Result.error(400, "Required request body is missing");
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public Result duplicateKey(DuplicateKeyException e) {
        Throwable cause = e.getCause();
        String localizedMessage = cause.getLocalizedMessage();
        String[] split = localizedMessage.split(" ");
        String path = split[2];
        String key = split[5];
        String error = "数据库字段要求唯一" + "path:" + path + ",key:" + key;
        log.error(error);

        return Result.error(AppHttpCodeEnum.SYSTEM_ERROR).setMsg(key + "不能重复");
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public Result sqlException(BadSqlGrammarException e) {
        //打印异常信息
        log.error("出现了异常！ {}", e.getLocalizedMessage());
        //从异常对象中获取提示信息封装返回
        return Result.error(AppHttpCodeEnum.SYSTEM_ERROR);
    }

//    @ExceptionHandler(Exception.class)
//    public Result otherException(Exception e){
//        return Result.error(AppHttpCodeEnum.SYSTEM_ERROR,e.getMessage());
//    }
}
