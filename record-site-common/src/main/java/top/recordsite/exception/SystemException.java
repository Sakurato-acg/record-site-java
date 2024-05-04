package top.recordsite.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.recordsite.vo.AppHttpCodeEnum;

@Data
@AllArgsConstructor
public class SystemException extends RuntimeException {

    private int code;

    private String msg;

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

    public SystemException(String msg) {
        this.code = AppHttpCodeEnum.SYSTEM_ERROR.getCode();
        this.msg = msg;
    }

}