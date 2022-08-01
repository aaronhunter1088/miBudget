package com.miBudget.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
class WebMVCConfig implements WebMvcConfigurer {

    @Bean(name = "dispatcher")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

//    @Bean
//    WebMvcConfigurer configurer () {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addResourceHandlers (ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("/").
//                        addResourceLocations("classpath:/WEB-INF/views/");
//            }
//        };
//    }


//    @Bean
//    @Description("Thymeleaf template resolver serving HTML 5")
//    public ClassLoaderTemplateResolver templateResolver() {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix("/templates/");
//        templateResolver.setCacheable(false);
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML");
//        templateResolver.setCharacterEncoding("UTF-8");
//        return templateResolver;
//    }
//
//    @Bean
//    @Description("Thymeleaf template engine with Spring integration")
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver());
//        return templateEngine;
//    }
//
//    @Bean
//    @Description("Thymeleaf view resolver")
//    public ViewResolver htmlViewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine());
//        resolver.setContentType("text/html");
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setViewNames(new String[]{"*.html"});
//        return resolver;
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/miBudget/").setViewName("index");
//        registry.addViewController("/Login.jsp").setViewName("Login");
//        registry.addViewController("/miBudget/Register.jsp").setViewName("Register");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Register resource handler for images
        registry.addResourceHandler("/miBudget/images/**")
                .addResourceLocations("classpath:/images/")
                .setCachePeriod(0);
    }
}