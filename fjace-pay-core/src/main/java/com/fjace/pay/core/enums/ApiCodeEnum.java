package com.fjace.pay.core.enums;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 接口返回编码枚举
 * @date 2021-09-05 10:35:00
 */
public enum ApiCodeEnum {
    SUCCESS(0, "SUCCESS"), //请求成功

    CUSTOM_FAIL(9999, "自定义业务异常"),  //自定义业务异常

    SYSTEM_ERROR(10, "系统异常[%s]"),
    PARAMS_ERROR(11, "参数有误[%s]"),
    DB_ERROR(12, "数据库服务异常"),

    SYS_OPERATION_FAIL_CREATE(5000, "新增失败"),
    SYS_OPERATION_FAIL_DELETE(5001, "删除失败"),
    SYS_OPERATION_FAIL_UPDATE(5002, "修改失败"),
    SYS_OPERATION_FAIL_SELETE(5003, "记录不存在"),
    SYS_PERMISSION_ERROR(5004, "权限错误，当前用户不支持此操作");

    private final int code;

    private final String msg;

    ApiCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
