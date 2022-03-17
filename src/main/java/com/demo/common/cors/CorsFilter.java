package com.demo.common.cors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 跨域资源共享过滤器
 *
 * @author dragode
 */
@Slf4j
public class CorsFilter extends OncePerRequestFilter {

    private final String DEFAULT_ALLOW_METHOD = "GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, PATCH";

    private final String DEFAULT_ALLOW_HEADER = "Origin, Accept, X-Requested-With, Content-Type" +
            ", Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, Cache-control";

    private final String DEFAULT_MAX_AGE = "1800";

    private CorsProperties corsProperties;

    public CorsFilter(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.trace("Blade CorsFilter doFilter start");

        if (CorsUtils.isPreFlightRequest(request)) {
            // 设定CORS的初始化参数
            // cors.allowed.origins
            response.addHeader("Access-Control-Allow-Origin", getPropertyOrDefault(corsProperties.getAllowOrigin(), HttpHeaders.ORIGIN));
            // cors.allowed.methods Access-Control-Allow-Methods: A comma separated
            // list of HTTP methods that can be used to access the resource
            response.addHeader("Access-Control-Allow-Methods", getPropertyOrDefault(corsProperties.getAllowMethods(), DEFAULT_ALLOW_METHOD));
            // cors.allowed.headers Access-Control-Allow-Headers: A comma separated
            // list of request headers that can be used when making an actual
            // request
            response.addHeader("Access-Control-Allow-Headers", getAllowHeaders(request));
            response.addHeader("Access-Control-Expose-Headers", getPropertyOrDefault(corsProperties.getAllowExposeHeaders(), ""));
            // cors.preflight.maxage Access-Control-Max-Age The amount of seconds,
            // browser is allowed to cache the result of the pre-flight request
            response.addHeader("Access-Control-Max-Age", getPropertyOrDefault(corsProperties.getMaxAge(), DEFAULT_MAX_AGE));
            response.addHeader(HttpHeaders.VARY, HttpHeaders.ORIGIN);
        }else {
            filterChain.doFilter(request, response);
        }

        log.trace("Blade CorsFilter doFilter end");
    }

    private String getPropertyOrDefault(String value, String defaultValue) {
        if (StringUtils.isNotEmpty(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }

    private String getAllowHeaders(HttpServletRequest request) {
        if (corsProperties.getAllowHeadersAutoEnabled()) {
            //自动从请求的Access-Control-Request-Headers获取跨域头信息，拼装到allowHeaders
            return getPropertyOrDefault(request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS), "");
        } else {
            String allowHeaders = getPropertyOrDefault(corsProperties.getAllowHeaders(), DEFAULT_ALLOW_HEADER);
            String additionalAllowHeaders = corsProperties.getAllowHeadersAdditional();
            if (StringUtils.isNotEmpty(additionalAllowHeaders)) {
                allowHeaders += "," + additionalAllowHeaders;
            }
            return allowHeaders;
        }
    }
}
