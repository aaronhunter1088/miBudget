package com.v1.miBudget.utilities;

import org.hibernate.HibernateException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtilities {
	
	public HibernateUtilities() {}

	public static SessionFactory sessionFactory = null;
	public static StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
	public static StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
	private static boolean newSessionFactory = false;
	
	public static void shutdown() {
       if (serviceRegistry != null) {
          StandardServiceRegistryBuilder.destroy(serviceRegistry);
       }
    }
	
	public static SessionFactory getSessionFactory() {
		try {
			if (sessionFactory == null) {
				// loads configuration and mappings
				
				MetadataSources sources = new MetadataSources(serviceRegistry)
	            		.addAnnotatedClass(com.v1.miBudget.entities.User.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.Item.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.Account.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.UserAccountObject.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.ItemAccountObject.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.UsersInstitutionIdsObject.class)
	            		.addAnnotatedClass(com.v1.miBudget.entities.UsersItemsObject.class);
				sessionFactory = new Configuration(sources)
	            		.configure("hibernate.cfg.xml")
	            		.buildSessionFactory();
				newSessionFactory = true;
	         }
		} catch (HibernateException e) {
			System.out.println("Hibernate threw an error.");
			System.out.println(e.getMessage());
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				System.out.println(ste[i]);
			}
			//newSessionFactory = false;
		} catch (Exception e) {
			System.out.println("Error trying to create sessionFactory");
			System.out.println(e.getMessage());
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				System.out.println(ste[i]);
			}
			//newSessionFactory = false;
		} finally {
			if (serviceRegistry != null) {
				StandardServiceRegistryBuilder.destroy(serviceRegistry);
			}
			if (newSessionFactory) 
				System.out.println("Returning new sessionFactory...");
			else if (!newSessionFactory || sessionFactory != null) {
				System.out.println("sessionFactory was not created due to an error.");
				return null;
			} else
				System.out.println("sessionFactory already created.");
		}
		
		return sessionFactory;
	}
}
