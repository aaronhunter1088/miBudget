package com.miBudget.v1.servlets;

import java.io.IOException;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.v1.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.v1.utilities.DateAndTimeUtility;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Profile.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info("--- START ---");
		LOGGER.info("Inside the Profile doGet() servlet.");
		HttpSession session = request.getSession(false);
		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			LOGGER.info("Redirecting to Profile.jsp");
			LOGGER.info("--- END ---");
			// Update time
			session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/WEB-INF/view/Profile.jsp" );
			dispatcher.forward( request, response );
		} else {
			// User is not logged in or the session is null
			LOGGER.info("Redirecting to Login.html");
			LOGGER.info("--- END ---");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "login.html" );
			dispatcher.forward( request, response );
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info(Constants.start);
		LOGGER.info("Inside the Profile doPost() servlet.");
		HttpSession session = request.getSession(false);  
        if(session != null && (Boolean) session.getAttribute("isUserLoggedIn") == true ) {
        	LOGGER.info("requestSessionId: " + session.getId());
        	LOGGER.info("NumberOfAccounts: " + (Integer) session.getAttribute("accountsSize"));
        	LOGGER.info("Redirecting to Profile.jsp");

        	// Update time
        	session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
        	getServletContext().getRequestDispatcher("/WEB-INF/view/Profile.jsp").forward(request, response);
			LOGGER.info(Constants.end);
        }  
        else { 
        	LOGGER.info("User may not be logged in/Session may have ended");
        	LOGGER.info("Redirecting to Login.html");
        	LOGGER.info(Constants.end);
            response.sendRedirect("Login.html");
        }
	}
	
	
}
