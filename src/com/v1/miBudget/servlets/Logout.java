package com.v1.miBudget.servlets;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MiBudget
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Inside Logout servlet");
		HttpSession requestSession = request.getSession(true);
//		HttpSession requestSession = (HttpSession) request.getAttribute("requestSession");
		if (requestSession.getAttribute("isUserLoggedIn") == null)
			requestSession.setAttribute("isUserLoggedIn", false); // just a check
		if (requestSession != null && (Boolean) requestSession.getAttribute("isUserLoggedIn") == true) {
			requestSession.setAttribute("isUserLoggedIn", false);
			System.out.println("requestSessionId: " + requestSession.getId() + " invalidated!");
			requestSession.invalidate();
			response.sendRedirect("index.html");
		} else { // there should not be an else needed here. 
			System.out.println("Redirecting to index.html");
			response.sendRedirect("index.html");
		}
	}
	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request, response);
//	}
}
