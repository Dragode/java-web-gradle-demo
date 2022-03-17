package com.demo.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorInfo {

    String getCode();

    HttpStatus getHttpStatus();
}
