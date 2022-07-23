package com.miBudget.core;

public class MiBudgetError extends Exception
{
    private String message;
    private Exception exception;
    private String errorCode;
    private String errorType;

    public MiBudgetError() {}

    public MiBudgetError(String message, Exception exception)
    {
        setMessage(message);
        setException(exception);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public MiBudgetError setMessage(String message) {
        this.message = message;
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public MiBudgetError setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public MiBudgetError setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorType() {
        return errorType;
    }

    public MiBudgetError setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }
}
