package com.v1.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.v1.miBudget.daoimplementations.AccountDAOImpl;
import com.v1.miBudget.daoimplementations.ItemDAOImpl;
import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.v1.miBudget.entities.Account;
import com.v1.miBudget.entities.Item;
import com.v1.miBudget.entities.User;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Delete() {
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

	public String deleteBank(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);  
		String institutionId = request.getParameter("idCopy");
		System.out.println("Attempting to delete bank: " + institutionId);
		Item item = itemDAOImpl.createItemFromInstitutionId(institutionId);
		if (item == null) {
			return "FAIL: error creating the item.";
		}
		User user = (User)session.getAttribute("user"); 
		
		int verify = itemDAOImpl.deleteBankReferencesFromDatabase(item, user);
		if (verify == 0) {
			return "FAIL: did not delete bank references from the database.";
		}
		ArrayList<String> accountIdsList = (ArrayList<String>)accountDAOImpl.getAccountIdsFromUser(item);
		ArrayList<Account> accounts = (ArrayList<Account>) accountDAOImpl.getAllAccounts(accountIdsList);
		verify = accountDAOImpl.deleteAccountsFromDatabase(accounts);
		if (verify == 0) {
			return "FAIL: did not delete the accounts from the accounts table.";
		}
		verify = itemDAOImpl.deleteItemFromDatabase(item);
		if (verify == 0) {
			return "FAIL: did not delete the item from the items table.";
		}
		if (verify == 1) {
			// update session values
			int numberOfAccounts = accountDAOImpl.getAccountIdsFromUser(user).size();
			ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
			session.setAttribute("institutionIdsList", institutionIdsList);
			session.setAttribute("institutionIdsListSize", institutionIdsList.size());
			session.setAttribute("accountsSize", numberOfAccounts);
		}
		return "SUCCESS";
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Inside the Delete doPost method");
		System.out.println("currentId: " + request.getParameter("idCopy"));
		System.out.println("deleting request: " + request.getParameter("delete"));
		
		// Perform the following logic:
		if (request.getParameter("delete").equals("bank")) {
			String deleteResponse = deleteBank(request, response);
			System.out.println("deleteResponse: " + deleteResponse);
			if (deleteResponse.equals("SUCCESS")) {
				response.setContentType("application/html");
				response.setStatus(HttpServletResponse.SC_OK);
				response.sendRedirect("Profile.jsp");
			} else {
				response.setContentType("applicaiton/html");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.sendRedirect("Profile.jsp");
			}
			
		} else if (request.getParameter("delete").equals("account")) {
			System.out.println("Attempting to delete account: " /* add accountId here */);
		}
		
		//response.sendRedirect("Profile.jsp");
	}

}
