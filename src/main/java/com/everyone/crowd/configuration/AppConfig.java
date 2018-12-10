package com.everyone.crowd.configuration;

import com.everyone.crowd.interceptor.AdminLoginInterceptor;
import com.everyone.crowd.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/js/**", "/css/**", "/font/**", "/img/**")
                .excludePathPatterns("/", "/error", "/admin/**");
        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/css/**", "/admin/fonts/**", "/admin/js/**", "/admin/plugins/**");
    }
}
