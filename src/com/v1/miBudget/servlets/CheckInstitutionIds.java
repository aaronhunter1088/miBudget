package com.v1.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.v1.miBudget.entities.User;

/**
 * Servlet implementation class CheckInstitutionIds
 */
@WebServlet("/CheckInstitutionIds")
public class CheckInstitutionIds extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckInstitutionIds() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("Inside CheckInstitutionIds doPost");
		boolean exit = checkInstitutionIds(request, (User)request.getSession(false).getAttribute("User") );
		if (exit) {
			System.out.println("Finished with CheckInstitutionIds doPost");
			System.out.println("Institution check failed.");
			// set response
			return;
		} else {
			System.out.println("Finished with CheckInstitutionIds doPost");
			System.out.println("Institution check passed.");
			// set response
		}
	}
	
	public boolean checkInstitutionIds(HttpServletRequest request, User user) {
		String institution_idIncoming = request.getParameter("institution_id");
		ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
		boolean exit = false;
		Iterator<String> iter = institutionIdsList.iterator();
		while (iter.hasNext()) {
			String id = iter.next();
			if (id.equals(institution_idIncoming)) {
				System.out.println(institution_idIncoming + " has already been added. We cannot add it again.");
				// exit = true;
				return exit = true;
			}
			System.out.println(id + " - This id did not match the one selected.");
		}
		return exit;
	}

}
