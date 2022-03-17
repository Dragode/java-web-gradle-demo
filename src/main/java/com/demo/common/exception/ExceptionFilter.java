package com.demo.common.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * 统一异常处理、日志requestId
 *
 * @author Dragode
 */
@Slf4j
public class ExceptionFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "TraceID";

    private static final Map<Class<? extends Exception>, ErrorInfo> commonExceptionErrorInfo;

    static {
        //预留通用异常消息处理
        commonExceptionErrorInfo = new HashMap<>();
        commonExceptionErrorInfo.put(Exception.class, new DefaultError(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.trace("Blade exception filter start");

        ErrorMessage errorMessage = ErrorMessage.newInstance();
        try {
            //输出requestId到log上下文
            MDC.put(TRACE_ID_KEY, errorMessage.getRequestId());
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause() instanceof Exception) {
                ex = (Exception) ex.getCause();
            }
            errorMessage.setMessage(ex.getMessage());
            //todo 根据配置决定是否要写detail，以免生产环境泄露敏感信息
            errorMessage.setDetail(ExceptionUtil.stacktraceToString(ex));
            ErrorInfo errorInfo = resolveErrorInfo(ex);
            errorMessage.setCode(errorInfo.getCode());
            writeToResponse(errorMessage, errorInfo.getHttpStatus(), response);
            log.error("Error occurred!", ex);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }

        log.trace("Blade exception filter end");
    }

    private ErrorInfo resolveErrorInfo(Exception ex) {
        ErrorInfo errorInfo;
        if (ex instanceof ErrorException) {
            ErrorException errorException = (ErrorException) ex;
            errorInfo = errorException.getErrorInfo();
        } else if (commonExceptionErrorInfo.containsKey(ex.getClass())) {
            errorInfo = commonExceptionErrorInfo.get(ex.getClass());
        } else {
            //无法处理的，返回500,并打印错误日志
            errorInfo = new DefaultError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return errorInfo;
    }

    private void writeToResponse(ErrorMessage errorMessage, HttpStatus httpStatus, HttpServletResponse response) {
        try {
            response.setStatus(httpStatus.value());
            response.addHeader(CONTENT_TYPE, "application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(JSONUtil.toJsonStr(errorMessage));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}