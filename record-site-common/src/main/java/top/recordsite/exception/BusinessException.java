package top.recordsite.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import top.recordsite.vo.AppHttpCodeEnum;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class BusinessException extends RuntimeException {

    private int code;

    private String msg;

    public BusinessException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

    public BusinessException(String msg) {
        this.code = AppHttpCodeEnum.BUSINESS.getCode();
        this.msg = msg;
    }

}