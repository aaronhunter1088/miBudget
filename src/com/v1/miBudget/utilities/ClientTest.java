package com.v1.miBudget.utilities;

import org.hibernate.Session;

public class ClientTest {

	public static void main(String[] args) {
		
		try {
			Session session = HibernateUtilities.getSessionFactory().openSession();
			String SQL = "SELECT version()";
			String result = session.createNativeQuery(SQL).getSingleResult().toString();
			System.out.format("MySQL version is %s\n", result);
			System.out.println("Test completed. Passed.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
