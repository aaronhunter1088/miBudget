package com.v1.miBudget.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.hibernate.Session;

public class ClientTest {

	public static void main(String[] args) {
		
		try {
			//Session session = HibernateUtilities.getSessionFactory().openSession();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miBudget", "root", "rootpassword");
			String SQL = "SELECT version()";
			//String result = session.createNativeQuery(SQL).getSingleResult().toString();
			Statement stmt = conn.createStatement();
			String result = stmt.executeQuery(SQL).toString();
			System.out.format("MySQL version is %s\n", result);
			System.out.println("stmt: " + stmt);
			System.out.println("Test completed. Passed.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
