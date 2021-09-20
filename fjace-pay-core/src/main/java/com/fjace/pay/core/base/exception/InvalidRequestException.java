package com.fjace.pay.core.base.exception;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 无效请求异常
 * @date 2021-09-04 16:30:00
 */
public class InvalidRequestException extends PayException {

    private static final long serialVersionUID = -5395396631425406390L;

    public InvalidRequestException(String message, int errorCode, Throwable cause) {
        super(message, cause, errorCode);
    }
}
