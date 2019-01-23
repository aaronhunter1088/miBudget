package com.miBudget.v1.servlets;

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
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside the Profile doGet() servlet.");
		HttpSession requestSession = request.getSession(false);
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			System.out.println("Redirecting to Profile.jsp");
			System.out.println("--- END ---");
			response.sendRedirect("Profile.jsp");
		} else {
			// User is not logged in or the requestSession is null
			System.out.println("Redirecting to Login.html");
			System.out.println("--- END ---");
			response.sendRedirect("Login.html");
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("\nInside the Profile doPost() servlet.");
		HttpSession session = request.getSession(false);  
        if(session != null && (Boolean) session.getAttribute("isUserLoggedIn") == true ) {
        	System.out.println("requestSessionId: " + session.getId());
        	System.out.println("NumberOfAccounts: " + (Integer) session.getAttribute("accountsSize"));
        	System.out.println("Redirecting to Profile.jsp");
        	System.out.println("--- END ---");
        	getServletContext().getRequestDispatcher("/Profile.jsp").forward(request, response);
        }  
        else { 
        	System.out.println("User may not be logged in/Session may have ended");
        	System.out.println("Redirecting to Login.html");
        	System.out.println("--- END ---");
            response.sendRedirect("Login.html");
        }
	}
}
