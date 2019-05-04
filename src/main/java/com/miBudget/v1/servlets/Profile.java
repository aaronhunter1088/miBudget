package com.miBudget.v1.servlets;

import java.io.IOException;
import java.time.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class Welcome
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Profile.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("--- START ---");
		LOGGER.info("Inside the Profile doGet() servlet.");
		HttpSession session = request.getSession(false);
		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			LOGGER.info("Redirecting to Profile.jsp");
			LOGGER.info("--- END ---");
			// Update time
			session.setAttribute("instantNow", Instant.now());
			response.sendRedirect("Profile.jsp");
		} else {
			// User is not logged in or the session is null
			LOGGER.info("Redirecting to Login.html");
			LOGGER.info("--- END ---");
			response.sendRedirect("Login.html");
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("--- START ---");
		LOGGER.info("\nInside the Profile doPost() servlet.");
		HttpSession session = request.getSession(false);  
        if(session != null && (Boolean) session.getAttribute("isUserLoggedIn") == true ) {
        	LOGGER.info("requestSessionId: " + session.getId());
        	LOGGER.info("NumberOfAccounts: " + (Integer) session.getAttribute("accountsSize"));
        	LOGGER.info("Redirecting to Profile.jsp");
        	LOGGER.info("--- END ---");
        	// Update time
        	session.setAttribute("instantNow", Instant.now());
        	getServletContext().getRequestDispatcher("/Profile.jsp").forward(request, response);
        }  
        else { 
        	LOGGER.info("User may not be logged in/Session may have ended");
        	LOGGER.info("Redirecting to Login.html");
        	LOGGER.info("--- END ---");
            response.sendRedirect("Login.html");
        }
	}
}
