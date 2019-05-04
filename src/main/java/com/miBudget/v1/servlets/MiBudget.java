package com.miBudget.v1.servlets;

import java.io.*;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class MiBudget
 */
//@WebServlet("/")
public class MiBudget extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(MiBudget.class);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("--- START ---");
		LOGGER.info("Inside MiBudget doGet() servlet.");
		HttpSession session = request.getSession(true);
		LOGGER.info("session : " + session);
		if (session != null) {
			LOGGER.info("isUserLoggedIn : " + session.getAttribute("isUserLoggedIn"));
			LOGGER.info("sessionId : " + session.getId());
			if ( (Boolean)session.getAttribute("isUserLoggedIn") == Boolean.TRUE) {
				if (request.getSession(true).getId().equals(request.getAttribute("sessionId"))) {
					// user is still logged in because this session is still active.
					// redirect user to Profile.jsp page
					LOGGER.info("Session is still active. Redirecting to Profile.jsp.");
					LOGGER.info("--- END ---");
					response.sendRedirect("Profile.jsp");
				}
			}
			else {
				LOGGER.info("Sending request to index.html.");
				LOGGER.info("--- END 1---");
				response.sendRedirect("index.html");
			}
		}
		else {
			LOGGER.info("Sending request to index.html.");
			LOGGER.info("--- END 2---");
			response.sendRedirect("index.html");
		}
		
		
	}

}
