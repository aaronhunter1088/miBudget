package com.miBudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;
import java.util.Properties;

//@EnableWebMvc
//@EnableAutoConfiguration enabled by SpringBootApplication
@EnableSpringDataWebSupport
@Configuration
@EnableCaching
@EnableTransactionManagement()
@ComponentScan(basePackages = {"com.miBudget.controllers", "com.miBudget.services"})
//@EntityScan(basePackages = {"com.miBudget.entities"})
@EnableJpaRepositories(basePackages = "com.miBudget.dao", entityManagerFactoryRef="entityManagerFactory")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MiBudgetApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MiBudgetApplication.class, args);
    }

    @ConfigurationProperties(prefix="spring.datasource")
    @Bean(name = "dataSource")
    public DataSource dataSource()
    {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/mibudget?serverTimezone=UTC");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("rootRoot");
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        return dataSourceBuilder.build();
    }

    @Bean
    public Properties additionalProps() {
        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        jpaProps.put("hibernate.hbm2ddl.auto", "update");
        jpaProps.put("spring.jpa.show-sql", true);
        jpaProps.put("spring.hibernate.format_sql", true);
        return jpaProps;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.miBudget.entities");
        em.setJpaProperties(additionalProps());
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean
    public InternalResourceViewResolver getViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
