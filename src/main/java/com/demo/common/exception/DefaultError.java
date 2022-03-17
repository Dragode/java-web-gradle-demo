package com.demo.common.exception;

import org.springframework.http.HttpStatus;

public class DefaultError implements ErrorInfo {

    private String code;
    private HttpStatus httpStatus;

    public DefaultError(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public DefaultError(HttpStatus httpStatus) {
        this(httpStatus.getReasonPhrase().toUpperCase().replace(" ", "_"), httpStatus);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
