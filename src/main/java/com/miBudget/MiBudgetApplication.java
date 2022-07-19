package com.miBudget;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class MiBudgetApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MiBudgetApplication.class, args);
    }

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        // Create a dispatcher servlet object
//        XmlWebApplicationContext webApplicationContext = new XmlWebApplicationContext();
//        webApplicationContext.setConfigLocation(
//                "classpath:application-properties");
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);
//
//        // Register Dispatcher Servlet with Servlet Context
//        ServletRegistration
//                .Dynamic myCustomDispatcherServlet = servletContext
//                .addServlet("dispatcherServlet", dispatcherServlet);
//
//        // Setting load on startup
//        myCustomDispatcherServlet.setLoadOnStartup(1);
//
//        // Adding mapping URL
//        myCustomDispatcherServlet.addMapping("/miBudget/login/*");
//        myCustomDispatcherServlet.addMapping("/miBudget/register/*");
//    }
}
