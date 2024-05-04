package top.recordsite.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
public class Result  implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result().setCode(200);
    }

    public static Result success(String msg) {
        return new Result().setCode(200).setMsg(msg);
    }

    public static Result success(String msg,Object data) {
        return new Result().setCode(200).setMsg(msg).setData(data);
    }

    public static Result success(Object data){
        return new Result().setCode(200).setData(data);
    }

    public static Result error(Integer code, String msg) {
        return new Result().setCode(code).setMsg(msg);
    }

    public static Result error(AppHttpCodeEnum codeEnum) {
        return new Result().setCode(codeEnum.getCode()).setMsg(codeEnum.getMsg());
    }

    public static Result error(AppHttpCodeEnum codeEnum, Object data) {
        return new Result()
                .setCode(codeEnum.getCode())
                .setMsg(codeEnum.getMsg())
                .setData(data);
    }

    public static Result error(AppHttpCodeEnum codeEnum, String msg, Object data) {
        return new Result().setCode(codeEnum.getCode()).setMsg(msg).setData(data);
    }

}