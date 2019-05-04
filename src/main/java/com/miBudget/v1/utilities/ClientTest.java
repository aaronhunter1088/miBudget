package com.miBudget.v1.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;


public class ClientTest {
	
	public static AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	public static ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	public static MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
	    LOGGER = LogManager.getLogger(ClientTest.class);
	}
	public static void main(String[] args) {
		try {
			SessionFactory factory = HibernateUtilities.getSessionFactory();
			Session hibernateSession = factory.openSession();
			Transaction t = hibernateSession.beginTransaction();
			String SQL = "SELECT version()";
			String result = hibernateSession.createNativeQuery(SQL).getResultList().get(0).toString();
			LOGGER.info("MySQL version is {}\n", result);
			//t.commit();
			hibernateSession.close();
			LOGGER.info("End client test.");
			//t = hibernateSession.beginTransaction();
			//User me = new User(20);
			//@SuppressWarnings("unchecked")
			//ArrayList<?> ids = miBudgetDAOImpl.getAllInstitutionIdsFromUser(me);
			//t.commit();
			//t = hibernateSession.beginTransaction();
			//ArrayList<Item> items = new ArrayList<>();
			//Item item = itemDAOImpl.getItemFromUser(ids.get(0).toString());
			//System.out.println(item);//
			//for(int i = 0; i < ids.size(); i++) {
				//String itemTableId = (String) itemsTableIdList.get(i);
			//	Item item = itemDAOImpl.getItemFromUser(ids.get(i).toString());
			//	System.out.println(item);
				//t.commit();
				//t = hibernateSession.beginTransaction();
			//}
			//t.commit();
			
			//HibernateUtilities.shutdown();
		} catch (NullPointerException | HibernateException e) {
			LOGGER.error("Error making a connection to the database");
			StackTraceElement[] ste = e.getStackTrace();
			StringBuilder err = new StringBuilder();
			for (int i = 0; i < ste.length; i++) {
				err.append(ste[i] + "\n");
			}
			LOGGER.error(err);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}
