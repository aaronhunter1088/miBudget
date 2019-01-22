package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.miBudget.v1.entities.Category;
import com.miBudget.v1.entities.User;
import com.google.gson.Gson;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;

/**
 * Servlet implementation class Services
 */
@WebServlet("/Services")
public class Services extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Services() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside Services doGet servlet.");
		HttpSession session = request.getSession(false);
		MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
		
		String method = request.getParameter("method");
		if (session == null)
			System.out.println("Session is null");
		User user = (User) session.getAttribute("user");
		
		// Lists for requests
		ArrayList<Category> categoriesList = null;
		
		if (method != null) {
			System.out.println("Method: " + method);
			switch (method) {
				case "getAllCategories" :   categoriesList = miBudgetDAOImpl.getAllCategories(user);
											response.setStatus(HttpServletResponse.SC_OK);
											break;
			}
			// Convert list to json string
			Gson gson = new Gson();
			String catListAsJson = gson.toJson(categoriesList);
			System.out.println(catListAsJson);
			
			// Save json string to response
			response.getWriter().append(catListAsJson);
		}
		System.out.println("--- END ---");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
