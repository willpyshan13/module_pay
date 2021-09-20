package com.fjace.pay.core.base.exception;

/**
 * @author fjace
 * @version 1.0.0
 * @Description Api 连接异常
 * @date 2021-09-04 16:29:00
 */
public class ApiConnectionException extends PayException {

    private static final long serialVersionUID = 5909392363963012319L;

    public ApiConnectionException(String message) {
        super(message);
    }

    public ApiConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
