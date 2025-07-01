package org.project.social_account_business.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.project.social_account_business.service.LoggingService;

import java.util.Optional;

@Component
public class LogInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private final ObjectMapper mapper;
    private final LoggingService loggingService;

    @Autowired
    public LogInterceptor(ObjectMapper mapper, LoggingService loggingService) {
        this.mapper = mapper;
        this.loggingService = loggingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
//
        log.info("🚀 Request Started: [{}] {}", request.getMethod(), getUrl(request));

        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && HttpMethod.GET.matches(request.getMethod())) {
            loggingService.logRequest(request, null);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) {
        log.info("✅ Response Status: {}", response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long executeTime = System.currentTimeMillis() - startTime;
        log.debug("🏁 Completed [{}] in {} ms", getUrl(request), executeTime);

        if (ex != null) {
            log.error("❌ Exception occurred: {}", ex.getMessage());
        }
    }

    private static String getUrl(HttpServletRequest req) {
        return Optional.ofNullable(req.getQueryString()).map(q -> req.getRequestURL() + "?" + q).orElse(req.getRequestURL().toString());
    }
}
