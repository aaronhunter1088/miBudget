//package com.miBudget.utilities;
//
//import com.miBudget.entities.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.HibernateException;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//
//public class HibernateUtilities {
//	private static Logger LOGGER = LogManager.getLogger(HibernateUtilities.class);
//
//	public static SessionFactory sessionFactory;
//	public static StandardServiceRegistryBuilder standardServiceRegistryBuilder;
//	public static StandardServiceRegistry serviceRegistry;
//
//	public HibernateUtilities() {
//		standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
//		serviceRegistry = standardServiceRegistryBuilder.build();
//	}
//	public SessionFactory getSessionFactory() {
//		try {
//			if (sessionFactory == null) {
//				MetadataSources sources = new MetadataSources(serviceRegistry)
//						.addAnnotatedClass(User.class)
//						.addAnnotatedClass(Item.class)
//						.addAnnotatedClass(Account.class)
//						.addAnnotatedClass(UserAccountObject.class)
//						.addAnnotatedClass(ItemAccountObject.class)
//						.addAnnotatedClass(UsersInstitutionIdsObject.class)
//						.addAnnotatedClass(UserItemsObject.class)
//						.addAnnotatedClass(Category.class)
//						.addAnnotatedClass(Rule.class);
//				sessionFactory = new Configuration(sources)
//						.configure()
//						.buildSessionFactory();
//			}
//			return sessionFactory;
//		} catch (Exception e) {
//			LOGGER.error("Error trying to create sessionFactory", e);
//			throw e;
//		}
//	}
//
//	public static void destroyServiceRegistry() {
//       if (serviceRegistry != null) {
//          StandardServiceRegistryBuilder.destroy(serviceRegistry);
//          LOGGER.info("serviceRegistry destroyed.");
//       }
//    }
//
//	/**
//	 * HibernateUtilities.shutdown() does the following:
//	 * sessionFactory.close();
//	 * StandardServiceRegistryBuilder.destroy(serviceRegistry);
//	 *
//	 */
//	public static void shutdown() {
//		if (sessionFactory.isOpen()) {
//			//sessionFactory.getCurrentSession().close();
//			sessionFactory = null;
//			LOGGER.info("sessionFactory closed.");
//		}
//		if (serviceRegistry != null) {
//			StandardServiceRegistryBuilder.destroy(serviceRegistry);
//			LOGGER.info("serviceRegistry destroyed.");
//		}
//	}
//
//}
