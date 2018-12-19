package com.everyone.crowd.interceptor;

import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        boolean isLoginRelated = isLoginRelated(requestURI);
        if (request.getSession().getAttribute("user") != null) {
            if (requestURI.equals("/user/logout")) {
                return true;
            }
            User user = (User) request.getSession().getAttribute("user");
            request.getSession().setAttribute("user", userService.findById(user.getId()));
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
            if (requestURI.equals("/user/2fa") || requestURI.equals("/user/logout")) {
                return true;
            }
            if (isLoginRelated) {
                response.sendRedirect("/user/2fa");
            } else {
                response.sendRedirect("/user/2fa?from=" + requestURI);
            }
            return false;
        } else if (isLoginRelated || requestURI.equals("/")) {
            return true;
        } else {
            response.sendRedirect("/user/login?from=" + requestURI);
            return false;
        }
    }

    private boolean isLoginRelated(String requestURI) {
        return requestURI.equals("/user/login")
                || requestURI.equals("/user/register")
                || requestURI.equals("/user/resetPassword")
                || requestURI.equals("/user/activate")
                || requestURI.equals("/user/logout");
    }
}
