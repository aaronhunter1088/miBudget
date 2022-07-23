package com.miBudget.database.test;

import com.miBudget.utilities.HibernateUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HibernateConnectionTest {
	
	private static Logger LOGGER = LogManager.getLogger(HibernateConnectionTest.class);

	@Test
	public void testHibernateUtilities() {
		try {
			HibernateUtilities hibernate = new HibernateUtilities();
			SessionFactory factory = hibernate.getSessionFactory();
			Session hibernateSession = factory.openSession();
			String SQL = "SELECT version()";
			String result = hibernateSession.createNativeQuery(SQL).getResultList().get(0).toString();
			LOGGER.info("MySQL version is {}", result);
			LOGGER.info("Test completed. Passed.");
			HibernateUtilities.shutdown();
		}
		catch (NullPointerException npe) {
			LOGGER.error("Error making a connection to the database...", npe);
		}
		catch (HibernateException e) {
			LOGGER.error("class: {} error: {}", e.getClass(), e.getMessage());
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}

