package com.v1.miBudget.utilities;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class ClientTest {

	public static void main(String[] args) {
		Driver driver = null;
		Connection conn = null;
		try {
			driver = DriverManager.getDriver("jdbc:mysql://localhost:3306/mibudget");
			DriverManager.registerDriver(driver);
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mibudget", "michaelball", "rootpassword");
		} catch (SQLException e) {
			System.out.println("Error creating driver.");
		}
		try {
			//Session session = HibernateUtilities.getSessionFactory().openSession();
			//Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mibudget", "michaelball", "rootpassword");
			System.out.println("conn: " + conn.getWarnings().toString());
			System.out.println("conn: " + conn.getSchema());
			System.out.println("conn: " + conn.getClientInfo().toString());
			String SQL = "SELECT version()";
			//String result = session.createNativeQuery(SQL).getSingleResult().toString();
			Statement stmt = conn.createStatement();
			String result = stmt.executeQuery(SQL).toString();
			System.out.format("MySQL version is %s\n", result);
			System.out.println("stmt: " + stmt);
			System.out.println("Test completed. Passed.");
		} catch (SQLException e) {
			System.out.println("Error making a connection to the database...\n" +
					"Try using: " + String.valueOf(driver));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
