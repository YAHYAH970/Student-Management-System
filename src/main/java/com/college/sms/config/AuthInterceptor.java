package com.college.sms.config;

import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) return true;
        if (path.equals("/api/auth/login") || path.equals("/api/auth/logout") ||
            path.equals("/api/student-auth/login") || path.equals("/api/student-auth/logout")) return true;
        HttpSession session = request.getSession(false);
        boolean studentRoute = path.startsWith("/api/student-portal/");
        boolean allowed = session != null && (studentRoute
            ? session.getAttribute("studentId") != null
            : session.getAttribute("adminUser") != null);
        if (!allowed) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Please log in first.\"}");
            return false;
        }
        return true;
    }
}
