package com.miBudget.database.test;

import com.miBudget.utilities.HibernateUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DatabaseConnectionTest {
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(DatabaseConnectionTest.class);
	}
	public static void main(String[] args) {
		try {
			SessionFactory factory = HibernateUtilities.getSessionFactory();
			Session hibernateSession = factory.openSession();
			String SQL = "SELECT version()";
			String result = hibernateSession.createNativeQuery(SQL).getResultList().get(0).toString();
			LOGGER.info("MySQL version is {}", result);
			LOGGER.info("Test completed. Passed.");
			HibernateUtilities.shutdown();
		} catch (NullPointerException e) {
			LOGGER.error("Error making a connection to the database...\n");
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				LOGGER.error(ste[i]);
			}
		} catch (HibernateException e) {
			LOGGER.error("class: {} error: {}", e.getClass(), e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}

