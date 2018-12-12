package com.everyone.crowd.configuration;

import com.everyone.crowd.interceptor.AdminLoginInterceptor;
import com.everyone.crowd.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final UserLoginInterceptor userLoginInterceptor;
    private AdminLoginInterceptor adminLoginInterceptor;

    @Autowired
    public AppConfig(UserLoginInterceptor userLoginInterceptor, AdminLoginInterceptor adminLoginInterceptor) {
        this.userLoginInterceptor = userLoginInterceptor;
        this.adminLoginInterceptor = adminLoginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/js/**", "/css/**", "/font/**", "/img/**")
                .excludePathPatterns("/error", "/admin/**");
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/css/**", "/admin/fonts/**", "/admin/js/**", "/admin/plugins/**");
    }
}
