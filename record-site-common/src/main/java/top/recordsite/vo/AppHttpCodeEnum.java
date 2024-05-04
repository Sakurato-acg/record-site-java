package top.recordsite.vo;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(501, "需要登录后操作"),

    NO_OPERATOR_AUTH(503, "无权限操作"),

    SYSTEM_ERROR(500, "出现错误"),
    BUSINESS(502, "业务错误"),
    LIMIT(504, "限流"),

    BOYMISSING(400, "参数校验错误");


//
//    USERNAME_EXIST(501, "用户名已存在"),
//
//    PHONENUMBER_EXIST(502, "手机号已存在"),
//
//    EMAIL_EXIST(503, "邮箱已存在"),
//
//    REQUIRE_USERNAME(504, "必需填写用户名"),
//
//    LOGIN_ERROR(505, "用户名或密码错误"),
//
//    CONTENT_NOT_NULL(506, "评论内容不能为空"),
//
//    FILE_TYPE_ERROR(507, "文件类型错误,限制文件类型为.png/.jpg"),
//
//    USER_NOT_NULL(508, "用户信息不能为空"),
//
//    NICKNAME_EXIST(512, "昵称已存在"),
//
//
//    TAG_EXIST(513, "标签已存在"),
//    TAG_BLANK(514, "便签不能为空"),
//    PAGE_NOT_EXIST(515,"分页信息不能为空"),
//    NEED_ID(516, "需要id"),
//    MENU_EXIST(517,"Menu已存在"),
//    MENU_ERROR(518, "不能创建根Menu"),
//    ROLE_EXIST(519, "角色已存在");


    int code;

    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
