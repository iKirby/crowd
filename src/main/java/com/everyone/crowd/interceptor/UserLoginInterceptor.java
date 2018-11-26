package com.everyone.crowd.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        boolean isLoginRelated = isLoginRelated(requestURI);
        if (request.getSession().getAttribute("user") != null) {
            if (isLoginRelated) {
                String from = request.getParameter("from");
                if (from != null && !isLoginRelated(from)) {
                    response.sendRedirect(from);
                } else {
                    response.sendRedirect("/");
                }
                return false;
            }
            return true;
        } else if (request.getSession().getAttribute("userTo2FA") != null) {
            if (isLoginRelated) {
                response.sendRedirect("/user/2fa");
            } else {
                response.sendRedirect("/user/2fa?from=" + requestURI);
            }
            return false;
        } else if (isLoginRelated) {
            return true;
        } else {
            response.sendRedirect("/user/login?from=" + requestURI);
            return false;
        }
    }

    private boolean isLoginRelated(String requestURI) {
        return requestURI.equals("/user/login")
                || requestURI.equals("/user/register")
                || requestURI.equals("/user/findpassword")
                || requestURI.equals("/user/2fa")
                || requestURI.equals("/user/activate");
    }
}
