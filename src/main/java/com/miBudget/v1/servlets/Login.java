package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UsersItemsObject;


/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside the Login doGet() servlet.");
		String btnSelected = request.getParameter("btnSelected");
		System.out.println("btnSelected: " + btnSelected);
		if (btnSelected != null) {
			if (btnSelected.equals("Cancel")) {
				System.out.println("Redirecting to index.html.");
				System.out.println("--- END ---");
				getServletContext().getRequestDispatcher("/index.html").forward(request, response);
			}
		} else {
			System.out.println("Redirecting to Login.html.");
			System.out.println("--- END ---");
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	//@SuppressWarnings("null")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside Login doPost() servlet.");
		String cellphone = request.getParameter("Cellphone");
		String password = request.getParameter("Password");
		boolean loginCredentials = false;
		List<User> allUsersList = null;
		HttpSession session = request.getSession(false);

		allUsersList = miBudgetDAOImpl.getAllUsers();
		User loginUser = new User(cellphone, password);
		System.out.println("loginUser: " + loginUser);
		
		// Validate user
		System.out.println("validating user credentials...");
		System.out.println("cellphone: " + cellphone);
		System.out.println("password: " + password);
		// for every user in the list
		
		for (User user : allUsersList) {
			if (loginUser.equals(user)) {
				System.out.println(loginUser + " matches " + user);
				System.out.println("Registered user. Logging in");
				loginUser = user;
				loginCredentials = true;
			} else if (loginUser.getCellphone().equals(user.getCellphone()) &&
					   loginUser.getPassword().equals(user.getPassword()) ) {
				System.out.println(loginUser + " matches " + user);
				System.out.println("Registered user. Logging in");
				loginUser = user;
				loginCredentials = true;
			} else {
				System.out.println(loginUser + " is not a match to " + user);
			}
		}
		// if logInUser does not have a first name, then they are a new user
		// else, logInUser will be fully populated and this is a returning user
		if (loginCredentials == true) {
			ArrayList<String> accountIdsList = (ArrayList<String>) accountDAOImpl.getAllAccountsIds(loginUser);
			loginUser.setAccountIds(accountIdsList);
			if (loginUser.getCategories().size() == 0) loginUser.createCategories();
			ArrayList<UsersItemsObject> allUsersItemsList = itemDAOImpl.getAllUserItems(loginUser);
			
			// Populate accounts and institutions map
			HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
			for (UsersItemsObject uiObj : allUsersItemsList) {
				Item item = itemDAOImpl.getItem(uiObj.getItemTableId() );
				acctsAndInstitutionIdMap.put(item.getInstitutionId(), accountDAOImpl.getAllAccountsForItem(uiObj.getItemTableId()) );
			}
			System.out.println("acctsAndInstitutionIdMap");
			for(String id : acctsAndInstitutionIdMap.keySet()) {
				System.out.println("key: " + id);
				for (Account a : acctsAndInstitutionIdMap.get(id)) {
					System.out.println("value: " + a);
				}
			}
			
			//loginUser.setCategories(miBudgetDAOImpl.getAllCategories(loginUser));
			
			// Populate errors map
			
			int accounts = accountIdsList.size();
			session = request.getSession(true);
			if (session == null || session.isNew()) {
				System.out.println("session is null. setting session");
				System.out.println("requestSessionId: " + session.getId());
			} else {
				System.out.println("Session already exists... but they're just logging in so get new session");
				session.invalidate();
				session = request.getSession(true);
			}
			ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(loginUser);
			session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
			session.setAttribute("institutionIdsList", institutionIdsList);
			session.setAttribute("institutionIdsListSize", institutionIdsList.size());
			session.setAttribute("session", session); // just a check
			session.setAttribute("sessionId", session.getId()); // just a check
			session.setAttribute("isUserLoggedIn", true); // just a check
			session.setAttribute("Firstname", loginUser.getFirstName());
			session.setAttribute("Lastname", loginUser.getLastName());
			session.setAttribute("user", loginUser); 
			session.setAttribute("accountsSize", accounts);
			session.setAttribute("isUserLoggedIn", true);
		}
		
		
//			for (User user : allUsersList) {
//			System.out.println("user from allUsersList: " + user.getCellphone() + ", " + user.getPassword());
//			// if a registered user's cellphone and password are equal to the user's input
//			if (user.getCellphone().equals(cellphone) && user.getPassword().equals(password)) {
//				// registered user
//				System.out.println("Registered user. Logging in");
//				ArrayList<String> accountIdsList = (ArrayList<String>) accountDAOImpl.getAllAccountsIds(user);
//				user.setAccountIds(accountIdsList);
//				int accounts = accountIdsList.size();
//				loginCredentials = true;
//				session = request.getSession(true);
//				if (session == null || session.isNew()) {
//					System.out.println("Session is null");
//					System.out.println("requestSessionId: " + session.getId());
////					requestSession.setMaxInactiveInterval(0);
////					requestSession.setAttribute("requestSession", request.getSession(true));
//					ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
//					session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
//					session.setAttribute("institutionIdsList", institutionIdsList);
//					session.setAttribute("institutionIdsListSize", institutionIdsList.size());
//					session.setAttribute("sessionId", session.getId());
//					session.setAttribute("session", session); // just a check
//					session.setAttribute("isUserLoggedIn", true);
//					session.setAttribute("Firstname", user.getFirstName());
//					session.setAttribute("Lastname", user.getLastName());
//					session.setAttribute("user", user); // user.toJsonArray()
//					session.setAttribute("accountsSize", accounts);
//					for(String account_id : User.getAccountIds(user)) {
//						System.out.println("accounts when logging in: " + account_id);
//					}
//				} else {
//					System.out.println("Session already exists... but they're just logging in so get new session");
//					session = request.getSession(true);
////					requestSession.setMaxInactiveInterval(0); // just a check
//					ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
//					session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
//					session.setAttribute("institutionIdsList", institutionIdsList);
//					session.setAttribute("institutionIdsListSize", institutionIdsList.size());
//					session.setAttribute("session", session); // just a check
//					session.setAttribute("sessionId", session.getId()); // just a check
//					session.setAttribute("isUserLoggedIn", true); // just a check
//					session.setAttribute("Firstname", user.getFirstName());
//					session.setAttribute("Lastname", user.getLastName());
//					session.setAttribute("user", user); 
//					session.setAttribute("accountsSize", accounts);
//				}
//			} 
//		} // end for all
		
		if (loginCredentials == true) {
			System.out.println("Redirecting to Profile.jsp");
			System.out.println("--- END ---");
			response.sendRedirect("Profile.jsp");
		} else {
			System.out.println("Redirecting to Register.jsp");
			System.out.println("--- END ---");
			response.sendRedirect("Register.jsp");
		}
	}
}
