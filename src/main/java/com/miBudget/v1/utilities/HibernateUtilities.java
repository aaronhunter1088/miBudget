package com.miBudget.v1.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtilities {
	
	public HibernateUtilities() {}

	public static SessionFactory sessionFactory = null;
	public static StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
	public static StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
	private static boolean newSessionFactory = false;
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(HibernateUtilities.class);
	}
	
	public static void destroyServiceRegistry() {
       if (serviceRegistry != null) {
          StandardServiceRegistryBuilder.destroy(serviceRegistry);
          LOGGER.info("serviceRegistry destroyed.");
       }
    }
	
	/**
	 * HibernateUtilities.shutdown() does the following:
	 * sessionFactory.close();
	 * StandardServiceRegistryBuilder.destroy(serviceRegistry);
	 * 
	 */
	public static void shutdown() {
		if (sessionFactory.isOpen()) {
			sessionFactory.close();
			LOGGER.info("sessionFactory closed.");
		}  
		if (serviceRegistry != null) {
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
			LOGGER.info("serviceRegistry destroyed.");
		}
	}
	
	public static SessionFactory getSessionFactory() {
		try {
			if (sessionFactory == null) {
				MetadataSources sources = new MetadataSources(serviceRegistry)
	            		.addAnnotatedClass(com.miBudget.v1.entities.User.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.Item.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.Account.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.UserAccountObject.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.ItemAccountObject.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.UsersInstitutionIdsObject.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.UsersItemsObject.class)
	            		.addAnnotatedClass(com.miBudget.v1.entities.Category.class)
						.addAnnotatedClass(com.miBudget.v1.entities.Rule.class);
				sessionFactory = new Configuration(sources)
	            		.configure("hibernate.cfg.xml")
	            		.buildSessionFactory();
				newSessionFactory = true;
	         }
		} catch (HibernateException e) {
			LOGGER.error("Hibernate threw an error.");
			LOGGER.error(e.getMessage());
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				LOGGER.error(ste[i]);
			}
			//newSessionFactory = false;
		} catch (Exception e) {
			LOGGER.error("Error trying to create sessionFactory");
			LOGGER.error(e.getMessage());
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				LOGGER.error(ste[i]);
			}
			//newSessionFactory = false;
		} finally {
			if (serviceRegistry != null) {
				StandardServiceRegistryBuilder.destroy(serviceRegistry);
			}
			if (newSessionFactory) 
				LOGGER.info("Returning new sessionFactory...");
			else if (!newSessionFactory || sessionFactory != null) {
				LOGGER.error("sessionFactory was not created due to an error.");
				return null;
			} else
				LOGGER.debug("sessionFactory already created.");
		}
		
		return sessionFactory;
	}
}
