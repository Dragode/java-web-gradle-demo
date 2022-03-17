package com.demo.common.exception;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = -5401402542472113075L;

    private String code;
    private String message;
    //详细的异常信息，例如异常堆栈
    private String detail;
    private String requestId;
    private Date serverTime;

    public static ErrorMessage newInstance() {
        ErrorMessage errorMessage = new ErrorMessage();
        String requestId = UUID.randomUUID().toString();
        errorMessage.setRequestId(requestId);
        errorMessage.setServerTime(new Date());
        return errorMessage;
    }
}
