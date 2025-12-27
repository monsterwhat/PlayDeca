package com.playdeca.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to redirect root requests to index
 * Handles both extensionless URLs and ensures proper navigation
 */
@WebFilter("/*")
public class RootRedirectFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔄 Root Redirect Filter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Remove context path for comparison
        String relativePath = requestURI;
        if (contextPath != null && !contextPath.isEmpty()) {
            relativePath = requestURI.substring(contextPath.length());
        }
        
        // If requesting root "/" - redirect to /index
        if (relativePath.equals("/")) {
            httpResponse.sendRedirect(contextPath + "/index");
            return;
        }
        
        // If requesting "/index" without extension, forward to index.xhtml
        if (relativePath.equals("/index")) {
            httpRequest.getRequestDispatcher("/index.xhtml").forward(httpRequest, httpResponse);
            return;
        }
        
        // Otherwise continue with normal processing
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        System.out.println("🔄 Root Redirect Filter destroyed");
    }
}