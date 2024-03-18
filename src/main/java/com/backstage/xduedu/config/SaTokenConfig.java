package com.backstage.xduedu.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-17
 */

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(
                new SaInterceptor())
//                .excludePathPatterns("/**");
                    .addPathPatterns("/**")
                    .excludePathPatterns("/user/sign-up","/user/login");
    }
}