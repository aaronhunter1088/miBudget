package com.miBudget.v1.servlets;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.v1.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class MiBudget
 */
@WebServlet("/logout")
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
		LOGGER.info(Constants.start);
		LOGGER.info("Inside Logout doPost() servlet");
		HttpSession session = request.getSession(true);
		if (session != null && (Boolean) session.getAttribute("isUserLoggedIn") == true) {
			session.setAttribute("isUserLoggedIn", false);
			LOGGER.info("requestSessionId: " + session.getId() + " invalidated!");
			session.invalidate();
		} else { // there should not be an else needed here.
			LOGGER.info("Redirecting to index.html");
		}
		LOGGER.info(Constants.end);
		response.sendRedirect("index.html");
	}
	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request, response);
//	}
}
