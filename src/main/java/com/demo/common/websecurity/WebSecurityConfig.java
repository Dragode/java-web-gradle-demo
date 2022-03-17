package com.demo.common.websecurity;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dragode
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private Pattern pattern = Pattern.compile("(GET|POST|DELETE|PUT|PATCH)?(:)?([\\s\\S]+)?");
    @Value("${blade.unAuthenticated:}")
    private String unAuthenticated;
    @Value("${blade.global.unAuthenticated:}")
    private String globalUnAuthenticated;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        ArrayListMultimap<HttpMethod, String> multiMap = ArrayListMultimap.create();
        if (!StringUtils.isEmpty(globalUnAuthenticated)) {
            multiMap.putAll(this.tranUnAuthenticatedToMap(globalUnAuthenticated));
        }
        if (!StringUtils.isEmpty(unAuthenticated)) {
            multiMap.putAll(this.tranUnAuthenticatedToMap(unAuthenticated));
        }
        // 转换成method为key，value为一个数组的map，key为null的分一组
        Map<HttpMethod, Collection<String>> methodPathMap = multiMap.asMap();
        if (methodPathMap.size() > 0) {
            for (Map.Entry<HttpMethod, Collection<String>> entry : methodPathMap.entrySet()) {
                registry.antMatchers(entry.getKey(), entry.getValue().toArray(new String[0])).permitAll();
            }
        }
        registry.anyRequest().authenticated();
    }

    /**
     * 将不需要认证的配置转换成Map
     *
     * @param unAuthenticated 不需要认证的配置字符串
     * @return 转换成method或者null为key，value为一个数组的map
     */
    private ArrayListMultimap<HttpMethod, String> tranUnAuthenticatedToMap(String unAuthenticated) {
        String[] unAuthenticatedPaths = unAuthenticated.split(",");
        ArrayListMultimap<HttpMethod, String> multiMap = ArrayListMultimap.create();
        for (String val : unAuthenticatedPaths) {
            // 过滤空字符
            Matcher matcher = pattern.matcher(val.trim());
            if (matcher.find()) {
                String methodName = matcher.group(1);
                HttpMethod method = Strings.isNullOrEmpty(methodName) ? null : HttpMethod.valueOf(methodName);
                String path = matcher.group(3);
                // path配置为空错误，丢弃
                if (!Strings.isNullOrEmpty(path)) {
                    multiMap.put(method, path.trim());
                }
            }
        }
        return multiMap;
    }
}
