package com.miBudget.v1.servlets;

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
//@WebServlet("/")
public class MiBudget extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside MiBudget doGet() servlet.");
		HttpSession session = request.getSession(true);
		System.out.println("session : " + session);
		if (session != null) {
			System.out.println("isUserLoggedIn : " + session.getAttribute("isUserLoggedIn"));
			System.out.println("sessionId : " + session.getId());
			if ( (Boolean)session.getAttribute("isUserLoggedIn") == Boolean.TRUE) {
				if (request.getSession(true).getId().equals(request.getAttribute("sessionId"))) {
					// user is still logged in because this session is still active.
					// redirect user to Profile.jsp page
					System.out.println("Session is still active. Redirecting to Profile.jsp.");
					System.out.println("--- END ---");
					response.sendRedirect("Profile.jsp");
				}
			}
			else {
				System.out.println("Sending request to index.html.");
				System.out.println("--- END 1---");
				response.sendRedirect("index.html");
			}
		}
		else {
			System.out.println("Sending request to index.html.");
			System.out.println("--- END 2---");
			response.sendRedirect("index.html");
		}
		
		
	}

}
