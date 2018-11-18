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

import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.v1.miBudget.entities.User;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside Profile servlet doGet");
		HttpSession requestSession = request.getSession(false);
		System.out.println("request: " + request.getSession(false));
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			System.out.println("Attempting to log in...");
			User user = (User) request.getAttribute("user");
			requestSession.setAttribute("change", "This text will change after using the Plaid Link Initializer.");
			//requestSession.setAttribute("institutionIdsList", miBudgetDAOImpl.getAllInstitutionIdsFromUser(user));
			response.sendRedirect("Profile.jsp");
		} else {
			System.out.println("requestSession: " + requestSession );
			System.out.println("isUserLoggedIn: " + requestSession.getAttribute("isUserLoggedIn") );
		}
		
		//doPost(request, response);
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
