package com.demo.constant;

import com.demo.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum RestErrorType implements ErrorInfo {

    DEMO(),
    ;

    RestErrorType() {
        this.code = this.name();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    RestErrorType(HttpStatus httpStatus) {
        this.code = this.name();
        this.httpStatus = httpStatus;
    }

    RestErrorType(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    private String code;
    private HttpStatus httpStatus;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
