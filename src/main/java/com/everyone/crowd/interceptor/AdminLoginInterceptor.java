package com.everyone.crowd.interceptor;

import com.everyone.crowd.entity.Admin;
import com.everyone.crowd.service.AdminService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    private final AdminService adminService;

    public AdminLoginInterceptor(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        boolean isLoginRelated = isLoginRelated(requestURI);
        if (request.getSession().getAttribute("admin") != null) {
            if (requestURI.equals("/admin/logout")) {
                return true;
            }
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            request.getSession().setAttribute("admin", adminService.findById(admin.getId()));
            if (isLoginRelated) {
                String from = request.getParameter("from");
                if (from != null && isLoginRelated(from)) {
                    response.sendRedirect(from);
                } else {
                    response.sendRedirect("/admin/dashboard");
                }
                return false;
            }
            return true;
        } else if (request.getSession().getAttribute("adminTo2FA") != null) {
            if (requestURI.equals("/admin/2fa") || requestURI.equals("/admin/logout")) {
                return true;
            }
            if (isLoginRelated) {
                response.sendRedirect("/admin/2fa");
            } else {
                response.sendRedirect("/admin/2fa?from=" + requestURI);
            }
            return false;
        } else if (isLoginRelated) {
            return true;
        } else {
            response.sendRedirect("/admin/login?from=" + requestURI);
            return false;
        }
    }

    private boolean isLoginRelated(String requestURI) {
        return requestURI.equals("/admin/login")
                || requestURI.equals("/admin/2fa")
                || requestURI.equals("/admin/findpassword")
                || requestURI.equals("/admin/logout");
    }
}
