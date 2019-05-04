package com.miBudget.v1.servlets;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class MiBudget
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = null;
	static  {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Logout.class);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("-- START --");
		LOGGER.info("Inside Logout servlet");
		HttpSession requestSession = request.getSession(true);
//		HttpSession requestSession = (HttpSession) request.getAttribute("requestSession");
		if (requestSession.getAttribute("isUserLoggedIn") == null)
			requestSession.setAttribute("isUserLoggedIn", false); // just a check
		if (requestSession != null && (Boolean) requestSession.getAttribute("isUserLoggedIn") == true) {
			requestSession.setAttribute("isUserLoggedIn", false);
			LOGGER.info("requestSessionId: " + requestSession.getId() + " invalidated!");
			requestSession.invalidate();
			LOGGER.info("-- END --");
			response.sendRedirect("index.html");
		} else { // there should not be an else needed here. 
			LOGGER.info("Redirecting to index.html");
			LOGGER.info("-- END --");
			response.sendRedirect("index.html");
		}
	}
	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request, response);
//	}
}
