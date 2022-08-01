//package com.miBudget.utilities;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.List;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * Servlet Filter implementation class CORSFilter
// */
//// Enable it for Servlet 3.x implementations
///* @ WebFilter(asyncSupported = true, urlPatterns = { "/*" }) */
//
////In order for Spring Boot to be able to recognize a filter, we just
//// needed to define it as a bean with the @Component annotation.
//@Component
//public class CorsFilter implements Filter {
//    private static Logger LOGGER = LogManager.getLogger(CorsFilter.class);
//    /**
//     * Default constructor.
//     */
//    public CorsFilter() {
//        // TODO Auto-generated constructor stub
//    }
//
//    /**
//     * @see Filter#destroy()
//     */
//    public void destroy() {
//        // TODO Auto-generated method stub
//    }
//
//    /**
//     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
//     */
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        LOGGER.info("CorsFilter HTTP Request: {}", request.getMethod());
//
//        // Authorize (allow) all domains to consume the content
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "*");
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST");
//
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
////        if (request.getMethod().equals("OPTIONS")) {
////            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
////            return;
////        }
//
////        String requestPath = request.getRequestURI();
////        HttpSession session = request.getSession(false);
////        if (needsAuthentication(requestPath)) { // change "user" for the session attribute you have defined
////
////            resp.sendRedirect(request.getContextPath() + "/static/index.jsp"); // No logged-in user found, so redirect to login page.
////        } else {
////            // pass the request along the filter chain
////            chain.doFilter(request, resp); // Logged-in user found, so just continue request.
////        }
//        // pass the request along the filter chain
//        chain.doFilter(request, response); // Logged-in user found, so just continue request.
//    }
//
//    //basic validation of pages that do not require authentication
//    private boolean needsAuthentication(String url) {
//        List<String> validNonAuthenticationUrls =
//                List.of("/miBudget/", "/miBudget/static/index.jsp",
//                        "/miBudget/login/", "/miBudget/static/Login.html",
//                        "/miBudget/register/signup", "/miBudget/static/Register.html");
//        return !validNonAuthenticationUrls.contains(url);
//    }
//
//    /**
//     * @see Filter#init(FilterConfig)
//     */
//    public void init(FilterConfig fConfig) throws ServletException {
//        // TODO Auto-generated method stub
//    }
//
//}
