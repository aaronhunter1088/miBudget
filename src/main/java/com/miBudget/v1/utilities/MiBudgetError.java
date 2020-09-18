package com.miBudget.v1.utilities;

public class MiBudgetError extends Exception {

    private String errorMessage;
    private Exception exception;

    public MiBudgetError(String errorMessage, Exception e) {
        setErrorMessage(errorMessage);
        setException(e);
    }

    public MiBudgetError(String errorMessage) {
        this(errorMessage, new Exception());
    }

    @Override
    public String getMessage() {
        return this.getMessage();
    }

    public Exception getException() {
        return this.getException();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}
