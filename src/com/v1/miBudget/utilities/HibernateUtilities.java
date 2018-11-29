package com.v1.miBudget.utilities;


import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtilities {

	private static SessionFactory sessionFactory;
	private static StandardServiceRegistry registry;
	private static boolean newSessionFactory = false;
	
	public static SessionFactory getSessionFactory() {
		try {
			if (sessionFactory == null) {
				// loads configuration and mappings
	            Configuration configuration = new Configuration()
	            		.configure("hibernate.cfg.xml");
//	            		.addAnnotatedClass(com.v1.miBudget.entities.User.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.Item.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.Account.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UserAccountObject.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.ItemAccountObject.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UsersInstitutionIdsObject.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UsersItemsObject.class);
	            configuration.addAnnotatedClass(com.v1.miBudget.entities.User.class)
	            			 .addAnnotatedClass(com.v1.miBudget.entities.Item.class)
	            			 .addAnnotatedClass(com.v1.miBudget.entities.Account.class)
	            			 .addAnnotatedClass(com.v1.miBudget.entities.UsersItemsObject.class)
	            			 .addAnnotatedClass(com.v1.miBudget.entities.ItemAccountObject.class)
	            			 .addAnnotatedClass(com.v1.miBudget.entities.UsersInstitutionIdsObject.class)
	            			 .addAnnotatedClass(com.v1.miBudget.entities.UserAccountObject.class);
	            ServiceRegistry serviceRegistry
	            	= new StandardServiceRegistryBuilder()
	                    .applySettings(configuration.getProperties()).build();
	             
	             //builds a session factory from the service registry
	            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	            newSessionFactory = true;
	            
	            
	            
//	            StandardServiceRegistryBuilder serviceRegistry = new StandardServiceRegistryBuilder();
//				Map<String, String> settings = new HashMap<>();
//	            settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
//	            settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/mibudget?useSSL=false");
//	            settings.put("hibernate.connection.username", "michaelball");
//	            settings.put("hibernate.connection.password", "rootpassword");
//	            settings.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//	            settings.put("hibernate.show_sql", "true");
//	            settings.put("hibernate.format_sql", "true");
//	            settings.put("hibernate.hbm2ddl.auto", "update");
//	            settings.put("hibernate.pool_size", "100");
//	            registryBuilder.applySettings(settings);
//	            registry = serviceRegistry.configure().build();
//	            MetadataSources sources = new MetadataSources(registry)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.User.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.Item.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UserItem.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.Account.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UserAccountObject.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.ItemAccountObject.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UsersInstitutionIdsObject.class)
//	            		.addAnnotatedClass(com.v1.miBudget.entities.UsersItemsObject.class);
//	            sessionFactory = sources.buildMetadata().buildSessionFactory();
//	            newSessionFactory = true;
			}
		} catch (HibernateException e) {
			System.out.println("Error trying to create sessionFactory");
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
			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
			if (newSessionFactory) 
				System.out.println("Returning new sessionFactory...");
			else if (!newSessionFactory && sessionFactory != null) {
				System.out.println("sessionFactory was not created due to an error.");
				return null;
			} else
				System.out.println("sessionFactory already created.");
		}
		
		return sessionFactory;
	}
	
	public static void shutdown() {
      if (registry != null) {
         StandardServiceRegistryBuilder.destroy(registry);
      }
   }
}
