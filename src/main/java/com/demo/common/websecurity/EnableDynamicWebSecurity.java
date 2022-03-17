package com.demo.common.websecurity;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用支持动态配置的web接口认证
 *
 * @author 970127
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WebSecurityConfig.class})
@Documented
public @interface EnableDynamicWebSecurity {
}
