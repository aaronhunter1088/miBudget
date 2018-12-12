package com.miBudget.v1.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForgotPassword
 */
@WebServlet("/ForgotPassword")
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputSelection = request.getParameter("InputSelection");
		if (!inputSelection.equals(null)) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mibudget", "root", "root");
				PreparedStatement statement = conn.prepareStatement(
						"SELECT Cellphone FROM mibudget.register_users");
				ResultSet rs = (ResultSet) statement.executeQuery();
				while (rs.next()) {
					if (rs.getString("Cellphone").equals(inputSelection) ) {
						// send user password
						response.sendRedirect("Login.jsp");
					} 
				} 
			} catch (Exception e) {
				System.out.println(e.getMessage());
				response.sendRedirect("index.jsp");
			}
		}
		
	}

}
