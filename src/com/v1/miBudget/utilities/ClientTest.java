package com.v1.miBudget.utilities;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.v1.miBudget.daoimplementations.AccountDAOImpl;
import com.v1.miBudget.daoimplementations.ItemDAOImpl;


public class ClientTest {
	
	public static AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	public static ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	
	public static void main(String[] args) {
		try {
			SessionFactory factory = HibernateUtilities.getSessionFactory();
			Session hibernateSession = factory.openSession();
			String SQL = "SELECT version()";
			String result = hibernateSession.createNativeQuery(SQL).getResultList().get(0).toString();
			System.out.format("MySQL version is %s\n", result);
			HibernateUtilities.shutdown();
		} catch (NullPointerException | HibernateException e) {
			System.out.println("Error making a connection to the database...\n");
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				System.out.println(ste[i]);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
