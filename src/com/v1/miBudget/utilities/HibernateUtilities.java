package com.v1.miBudget.utilities;

import org.hibernate.HibernateException;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.v1.miBudget.entities.User;

public class HibernateUtilities {

	private static SessionFactory sessionFactory;
	private static StandardServiceRegistry registry;
	private static boolean newSessionFactory = false;
	
	public static SessionFactory getSessionFactory() {
		try {
			if (sessionFactory == null) {
				newSessionFactory = true;
				// loads configuration and mappings
	            Configuration configuration = new Configuration().configure("hibernate.cfg.xml")
	            		.addAnnotatedClass(com.v1.miBudget.entities.User.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.Item.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.UserItem.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.Account.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.UserAccountObject.class);
	            ServiceRegistry serviceRegistry
	                = new StandardServiceRegistryBuilder()
	                    .applySettings(configuration.getProperties()).build();
	             
	            // builds a session factory from the service registry
	            sessionFactory = configuration.buildSessionFactory(serviceRegistry); 
			}
		} catch (HibernateException e) {
			System.out.println("Error trying to create sessionFactory");
			System.out.println(e.getMessage());
		} finally {
			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
		}
		if (newSessionFactory) 
			System.out.println("Returning new sessionFactory...");
		else
			System.out.println("Session factory already created.");
		return sessionFactory;
	}
}
