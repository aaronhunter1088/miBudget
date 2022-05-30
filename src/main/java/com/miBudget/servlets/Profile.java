package com.miBudget.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.entities.User;
import com.miBudget.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.utilities.DateAndTimeUtility;

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
		LOGGER.info(Constants.start);
		LOGGER.info("Inside the Profile doGet() servlet.");
		HttpSession session = request.getSession(false);
		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			LOGGER.info("Redirecting to Profile.jsp");
			LOGGER.info(Constants.end);
			// Update time
			session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
			session.setAttribute("change", "This text will change after the user take actions");
			boolean setupMode = ((User)session.getAttribute("user")).isSetupMode();
			LOGGER.debug("is in SetupMode?: " + setupMode);
			if (setupMode)
			{
				session.setAttribute("change", "Once you finish adding accounts, and creating categories, your budget will appear here.");
			}
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/WEB-INF/view/Profile.jsp" );
			dispatcher.forward( request, response );
		} else {
			// User is not logged in or the session is null
			LOGGER.info("Redirecting to Login.html");
			LOGGER.info(Constants.end);
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
        	session.setAttribute("change", "This text will change after the user take actions");
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
