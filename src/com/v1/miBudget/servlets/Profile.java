package com.v1.miBudget.servlets;

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
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside Profile servlet doGet");
		//System.out.println("meta_data: " + request.getParameter("meta_data"));
		
		// call Authenticate
		
		
		
		doPost(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside the Profile servlet.");
		HttpSession requestSession = request.getSession(false);  
        if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true){  
        	System.out.println("stored requestSessionId: " + requestSession.getAttribute("requestSessionId"));
        	//RequestDispatcher view = request.getRequestDispatcher("/Profile.html");
        	//view.forward(request, response); // doesn't take the session
        	response.sendRedirect("Profile.jsp");
        	
        }  
        else {   
        	System.out.println("User is not logged in");
            response.sendRedirect("Login.html");
        }  
	}
	
	 
	
    
}
