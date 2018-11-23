package com.everyone.crowd.interceptor;

import com.everyone.crowd.configuration.Constants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        boolean isLoginRelated = isLoginRelated(requestURI);
        if (request.getSession().getAttribute(Constants.SESSION_USER_NAME) != null) {
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
        } else if (request.getSession().getAttribute(Constants.SESSION_USER_2FA_NAME) != null) {
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
                || requestURI.equals("/user/2fa");
    }
}
