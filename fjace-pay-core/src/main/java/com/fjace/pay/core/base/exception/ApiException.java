package com.fjace.pay.core.base.exception;

/**
 * @author fjace
 * @version 1.0.0
 * @Description Api 接口异常
 * @date 2021-09-04 16:28:00
 */
public class ApiException extends PayException {

    private static final long serialVersionUID = -150937028484945103L;

    public ApiException(String message, String type, String code, int errorCode, Throwable cause) {
        super(message, cause, errorCode);
    }
}
