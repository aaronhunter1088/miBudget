package com.v1.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateUser
 */
@WebServlet("/updateuser")
public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUser() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    
    private List<String> getInstitutionIds() {
    	System.out.println("Returning all institution ids from db for this user.");
		ArrayList<String> testList = new ArrayList<>();
		testList.add("testId1");
		testList.add("testId2");
		return testList;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Working. Store institution_id in a list and store in db for user");
		System.out.println("posting to UpdateUser");
		System.out.println("selected institution_id: " + request.getParameter("institution_id"));
		System.out.println("selected institution_name: " + request.getParameter("institution_name"));
		
	}

}
