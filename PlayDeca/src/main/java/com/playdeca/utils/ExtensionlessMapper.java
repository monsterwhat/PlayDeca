package com.playdeca.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to handle extensionless URLs by adding .xhtml extension
 * Replaces existing extensions with .xhtml if needed
 */
@WebFilter("/*")
public class ExtensionlessMapper implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🌐 Extensionless Mapper initialized - clean URLs enabled");
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
        
        // Skip static resources and already processed paths
        if (shouldSkip(relativePath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if we need to add .xhtml extension
        if (!relativePath.isEmpty() && !relativePath.equals("/") && !relativePath.endsWith(".xhtml")) {
            // Replace any existing extension with .xhtml, or add .xhtml if no extension
            int lastDotIndex = relativePath.lastIndexOf('.');
            if (lastDotIndex > 0 && lastDotIndex < relativePath.length() - 1) {
                relativePath = relativePath.substring(0, lastDotIndex) + ".xhtml";
            } else {
                relativePath = relativePath + ".xhtml";
            }
            
            // Forward to the .xhtml file
            httpRequest.getRequestDispatcher(relativePath).forward(httpRequest, httpResponse);
            return;
        }
        
        // Continue with normal processing
        chain.doFilter(request, response);
    }
    
    private boolean shouldSkip(String path) {
        // Skip static resources, API endpoints, and other non-JSF paths
        return path.equals("/") ||
               path.startsWith("/javax.faces.resource/") ||
               path.startsWith("/resources/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/assets/") ||
               path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".gif") ||
               path.endsWith(".ico") ||
               path.endsWith(".map");
    }
    
    @Override
    public void destroy() {
        System.out.println("🌐 Extensionless Mapper destroyed");
    }
}