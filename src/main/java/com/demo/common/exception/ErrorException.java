package com.demo.common.exception;

public class ErrorException extends RuntimeException {

    private ErrorInfo errorInfo;

    public ErrorException(ErrorInfo restErrorType) {
        this.errorInfo = restErrorType;
    }

    ErrorException(ErrorInfo restErrorType, Throwable cause) {
        super(cause);
        this.errorInfo = restErrorType;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
