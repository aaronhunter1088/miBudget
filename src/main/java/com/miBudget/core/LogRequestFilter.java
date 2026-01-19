package com.miBudget.core;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class LogRequestFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(LogRequestFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logRequestHeaders(httpRequest);
        chain.doFilter(request, response);
    }

    private void logRequestHeaders(HttpServletRequest request)
    {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                logger.info("{}: {}", headerName, headerValue);
            }
        }
    }

    @Override
    public void destroy()
    {}
}
