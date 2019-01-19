package com.miBudget.v1.servlets;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MiBudget
 */
public class MiBudget extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside MiBudget doGet() servlet.");
		System.out.println("Redirecting to index.html.");
		System.out.println("--- END ---");
		response.sendRedirect("index.html");
	}

}
