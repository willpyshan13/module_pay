package com.fjace.pay.core.base.exception;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付异常抽象类
 * @date 2021-09-04 16:21:00
 */
public abstract class PayException extends Exception {

    private static final long serialVersionUID = 4801397439364538971L;

    private int errorCode;

    public PayException(String message) {
        super(message, null);
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
