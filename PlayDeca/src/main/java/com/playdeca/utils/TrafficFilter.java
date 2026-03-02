package com.playdeca.utils;

import com.playdeca.models.TrafficLog;
import com.playdeca.services.TrafficLogService;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebFilter("/*")
public class TrafficFilter implements Filter {

    @Inject
    private TrafficLogService trafficLogService;

    private static final boolean SKIP_AJAX_REQUESTS = true;
    private static final boolean SKIP_RESOURCE_REQUESTS = true;
    private static final boolean SKIP_POST_REQUESTS = true;
    private static final boolean SKIP_LOOPBACK_IPS = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("📊 Traffic Filter initialized - logging enabled");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String pageUrl = req.getRequestURL().toString();
        String httpMethod = req.getMethod();
        String ip = request.getRemoteAddr();

        if (SKIP_AJAX_REQUESTS && isAjaxRequest(req)) {
            chain.doFilter(request, response);
            return;
        }

        if (SKIP_RESOURCE_REQUESTS && isResourceRequest(pageUrl)) {
            chain.doFilter(request, response);
            return;
        }

        if (SKIP_POST_REQUESTS && "POST".equals(httpMethod)) {
            chain.doFilter(request, response);
            return;
        }

        if (SKIP_LOOPBACK_IPS && ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip))) {
            chain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();

        String referrerUrl = req.getHeader("Referer");
        String userAgent = req.getHeader("User-Agent");

        String deviceType = extractDeviceType(userAgent);
        String operatingSystem = extractOperatingSystem(userAgent);
        String browserVersion = extractBrowserVersion(userAgent);

        chain.doFilter(request, response);

        long responseTime = System.currentTimeMillis() - startTime;
        int statusCode = resp.getStatus();

        TrafficLog log = new TrafficLog();
        log.setIpAddress(ip);
        log.setPageUrl(pageUrl);
        log.setVisitTime(LocalDateTime.now());
        log.setDeviceType(deviceType);
        log.setOperatingSystem(operatingSystem);
        log.setBrowserVersion(browserVersion);
        log.setHttpMethod(httpMethod);
        log.setReferrerUrl(referrerUrl);
        log.setResponseTime(responseTime);
        log.setStatusCode(statusCode);
        log.setSessionId(req.getSession(false) != null ? req.getSession().getId() : null);

        trafficLogService.create(log);
    }

    private boolean isResourceRequest(String pageUrl) {
        return pageUrl.contains("/images") || pageUrl.contains("/resources")
                || pageUrl.contains("favicon.ico") || pageUrl.contains("jakarta.faces.resource")
                || pageUrl.contains("/css/") || pageUrl.contains("/js/")
                || pageUrl.contains("/assets/");
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        String ajaxHeader = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxHeader);
    }

    private String extractDeviceType(String userAgent) {
        if (userAgent == null) {
            return "Unknown Device";
        }
        if (userAgent.toLowerCase().contains("mobile")) {
            return "Mobile";
        } else if (userAgent.toLowerCase().contains("tablet") || userAgent.toLowerCase().contains("ipad")) {
            return "Tablet";
        } else {
            return "Desktop";
        }
    }

    private String extractOperatingSystem(String userAgent) {
        if (userAgent == null) {
            return "Unknown OS";
        }
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac OS")) {
            return "Mac OS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        } else {
            return "Unknown OS";
        }
    }

    private String extractBrowserVersion(String userAgent) {
        if (userAgent == null) {
            return "Unknown Browser";
        }
        if (userAgent.contains("Chrome")) {
            return "Chrome " + extractVersion(userAgent, "Chrome/");
        } else if (userAgent.contains("Firefox")) {
            return "Firefox " + extractVersion(userAgent, "Firefox/");
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari " + extractVersion(userAgent, "Version/");
        } else if (userAgent.contains("Edge")) {
            return "Edge " + extractVersion(userAgent, "Edge/");
        } else if (userAgent.contains("MSIE")) {
            return "Internet Explorer " + extractVersion(userAgent, "MSIE ");
        } else if (userAgent.contains("Trident")) {
            return "Internet Explorer " + extractVersion(userAgent, "rv:");
        } else {
            return "Unknown Browser";
        }
    }

    private String extractVersion(String userAgent, String browserPrefix) {
        int startIndex = userAgent.indexOf(browserPrefix);
        if (startIndex != -1) {
            String versionSubstring = userAgent.substring(startIndex + browserPrefix.length());
            String[] versionParts = versionSubstring.split("[ ;)]");
            return versionParts[0].trim();
        }
        return "Unknown Version";
    }

    @Override
    public void destroy() {
        System.out.println("📊 Traffic Filter destroyed");
    }
}
