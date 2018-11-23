package com.everyone.crowd.interceptor;

import com.everyone.crowd.configuration.URIConstants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.equals(URIConstants.USER_LOGIN) || requestURI.equals("/") || requestURI.equals("/error")) {
            return true;
        }
        if (request.getSession().getAttribute("user") != null) {
            return true;
        } else {
            Object userTo2FA = request.getSession().getAttribute("userTo2FA");
            if (userTo2FA != null) {
                response.sendRedirect(URIConstants.USER_2FA + "?from=" + requestURI);
                return false;
            }
            response.sendRedirect(URIConstants.USER_LOGIN + "?from=" + requestURI);
            return false;
        }
    }
}
