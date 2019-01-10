package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UsersItemsObject;
import com.miBudget.v1.utilities.HibernateUtilities;


/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final SessionFactory factory = HibernateUtilities.getSessionFactory();
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doPost(request, response);
		String btnSelected = request.getParameter("btnSelected");
		System.out.println("btnSelected: " + btnSelected);
		if (btnSelected.equals("Cancel")) {
			System.out.println("\nInside the Login servlet doGet(). Redirecting to index.html.");
			getServletContext().getRequestDispatcher("/index.html").forward(request, response);
		} else {
			System.out.println("\nInside the Login servlet doGet(). Redirecting to Login.html.");
			getServletContext().getRequestDispatcher("/Login.html").forward(request, response);
		}
		
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	//@SuppressWarnings("null")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("\nInside Login doPost()...");
		String cellphone = request.getParameter("Cellphone");
		String password = request.getParameter("Password");
		boolean loginCredentials = false;
		List<User> allUsersList = null;
		HttpSession session = request.getSession(false);

		// TODO: change variable to a single user which is received using cellphone AND password
		allUsersList = miBudgetDAOImpl.getAllUsers();
		// Validate user
		System.out.println("validating user credentials...");
		System.out.println("cellphone: " + cellphone);
		System.out.println("password: " + password);
		// for every user in the list
		
		ArrayList<UsersItemsObject> usersItemsList = itemDAOImpl.getAllUserItems(allUsersList.get(0));
		HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
		Iterator usersItemsIter = usersItemsList.iterator();
		while (usersItemsIter.hasNext()) {
			int itemTableId = ( (UsersItemsObject) usersItemsIter.next()).getItemTableId();
			Item item = itemDAOImpl.getItem(itemTableId);
			ArrayList<Account> list = accountDAOImpl.getAllOfItemsAccounts(itemTableId);
			acctsAndInstitutionIdMap.put(item.getInsitutionId(), list);
		}
		System.out.println("acctsAndInstitutionIdMap");
		for(String id : acctsAndInstitutionIdMap.keySet()) {
			System.out.println("\nkey: " + id);
			for (Account a : acctsAndInstitutionIdMap.get(id)) {
				System.out.println("value: " + a);
			}
		}
		
		
		
		// TODO: change logic to be for ONE USER. We don't need to search for a specific user like this
		for (User user : allUsersList) {
			System.out.println("user from allUsersList: " + user.getCellphone() + ", " + user.getPassword());
			// if a registered user's cellphone and password are equal to the user's input
			if (user.getCellphone().equals(cellphone) && user.getPassword().equals(password)) {
				// registered user
				System.out.println("Registered user. Logging in");
				ArrayList<String> accountIdsList = (ArrayList<String>) accountDAOImpl.getAllAccountsIds(user);
				user.setAccountIds(accountIdsList);
				int accounts = accountIdsList.size();
				loginCredentials = true;
				session = request.getSession(true);
				if (session == null || session.isNew()) {
					System.out.println("Session is null");
					System.out.println("requestSessionId: " + session.getId());
//					requestSession.setMaxInactiveInterval(0);
//					requestSession.setAttribute("requestSession", request.getSession(true));
					ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
					session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
					session.setAttribute("institutionIdsList", institutionIdsList);
					session.setAttribute("institutionIdsListSize", institutionIdsList.size());
					session.setAttribute("sessionId", session.getId());
					session.setAttribute("session", session); // just a check
					session.setAttribute("isUserLoggedIn", true);
					session.setAttribute("Firstname", user.getFirstName());
					session.setAttribute("Lastname", user.getLastName());
					session.setAttribute("user", user); // user.toJsonArray()
					session.setAttribute("accountsSize", accounts);
					for(String account_id : User.getAccountIds(user)) {
						System.out.println("accounts when logging in: " + account_id);
					}
				} else {
					System.out.println("Session already exists... but they're just logging in so get new session");
					session = request.getSession(true);
//					requestSession.setMaxInactiveInterval(0); // just a check
					ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
					session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
					session.setAttribute("institutionIdsList", institutionIdsList);
					session.setAttribute("institutionIdsListSize", institutionIdsList.size());
					session.setAttribute("session", session); // just a check
					session.setAttribute("sessionId", session.getId()); // just a check
					session.setAttribute("isUserLoggedIn", true); // just a check
					session.setAttribute("Firstname", user.getFirstName());
					session.setAttribute("Lastname", user.getLastName());
					session.setAttribute("user", user); 
					session.setAttribute("accountsSize", accounts);
				}
			} 
		} // end for all
		
		// if no user is found, must be a new user
		// but what if user accidentally typed in cellphone wrong
		// the user will be annoyed that they have to navigate back to the page
		// it should warn the user: hey, i couldn't find a match. try again. 
		// redirect to sign up page
		System.out.println("loginCredentials: " + loginCredentials);
		if (loginCredentials == true && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			session.setAttribute("isUserLoggedIn", true);
			//getServletContext().getRequestDispatcher("/Welcome.jsp").forward(request, response);
		
			response.sendRedirect("Welcome.jsp");
		} else {
			response.sendRedirect("Register.jsp");
		}
	}
}
