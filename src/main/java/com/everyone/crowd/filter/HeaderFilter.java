package com.everyone.crowd.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "headerFilter", urlPatterns = "/*")
public class HeaderFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String turbolinksLocation = httpReq.getRequestURI();
        String queryString = httpReq.getQueryString();
        if (queryString != null) {
            turbolinksLocation += "?" + queryString;
        }
        httpResp.addHeader("Turbolinks-Location", turbolinksLocation);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
