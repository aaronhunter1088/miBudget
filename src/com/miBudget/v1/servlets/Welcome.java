package com.v1.miBudget.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

/**
 * Servlet implementation class Welcome
 */
@WebServlet("/Welcome")
public class Welcome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doPost(request, response);
		response.sendRedirect("Welcome.jsp");
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("Inside the Welcome servlet.");
		HttpSession session = request.getSession(false);  
        if(session != null && (Boolean) session.getAttribute("isUserLoggedIn") == true ) {
        	System.out.println("requestSessionId: " + session.getId());
        	System.out.println("NumberOfAccounts: " + (Integer) session.getAttribute("accountsSize"));
        	//System.out.println("Accounts: " + session.getAttribute("listOfAccountIds"));
        	getServletContext().getRequestDispatcher("/Welcome.jsp").forward(request, response);
//        	response.sendRedirect("Welcome.jsp");
        }  
        else { 
        	System.out.println("User may not be logged in/Session may have ended");
            response.sendRedirect("Login.html");
        }
	}
}
