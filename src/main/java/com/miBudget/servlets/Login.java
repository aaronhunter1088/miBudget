//package com.miBudget.servlets;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.miBudget.core.MiBudgetState;
//import com.miBudget.utilities.Constants;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.miBudget.entities.Account;
//import com.miBudget.entities.Item;
//import com.miBudget.entities.Transaction;
//import com.miBudget.entities.User;
//import com.miBudget.entities.UserItemsObject;
//import com.miBudget.utilities.DateAndTimeUtility;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import static com.miBudget.utilities.Constants.end;
//import static com.miBudget.utilities.Constants.start;
//
//
///**
// * Servlet implementation class Login
// */
//@WebServlet("/login")
////@RestController("/login")
//@CrossOrigin(origins = "*")
//public class Login {
//	private static final long serialVersionUID = 1L;
//
//	private static Logger LOGGER = LogManager.getLogger(Login.class);
//
//	@GetMapping("/getLoginPage")
//	protected void getLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info(start);
//		LOGGER.info("Inside the getLoginPage() method");
//		String btnSelected = request.getParameter("btnSelected");
//		if (btnSelected.equals(Constants.cancel)) {
//			LOGGER.info("Redirecting to index.html.");
//			request.getServletContext().getRequestDispatcher("/static/index.html").forward(request, response);
//		} else if (btnSelected.equals("Register")) {
//			LOGGER.info("Redirecting to Register.html.");
//			request.getServletContext().getRequestDispatcher("/Register.html").forward(request, response);
//		} else {
//			LOGGER.info("Redirecting to Login.html.");
//			request.getServletContext().getRequestDispatcher("/Login.html").forward(request, response);
//		}
//		LOGGER.info(end);
//	}
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info(Constants.start);
//		LOGGER.info("Inside the Login doGet() servlet.");
//		LOGGER.info("Redirecting to Login.html.");
//		LOGGER.info(Constants.end);
//		response.setStatus(HttpServletResponse.SC_OK);
//	}
//
//	//@SuppressWarnings("null")
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info(Constants.start);
//		LOGGER.info("Inside Login doPost() servlet.");
//		String cellphone = request.getParameter("Cellphone");
//		String password = request.getParameter("Password");
//		boolean loginCredentials = false;
//		List<User> allUsersList = null;
//		HttpSession session = request.getSession(false);
//		//Instant instantNow = Instant.now();
//
//		allUsersList = MiBudgetState.getMiBudgetDAOImpl().getAllUsers();
//		User loginUser = new User(cellphone, password);
//		LOGGER.info("loginUser: " + loginUser);
//
//		// Validate user
//		LOGGER.info("validating user credentials...");
//		LOGGER.info("cellphone: " + cellphone);
//		LOGGER.info("password: " + password);
//		// for every user in the list
//
//		for (User activeUser : allUsersList) {
//			if (loginUser.equals(activeUser)) {
//				LOGGER.info(loginUser + " matches " + activeUser);
//				LOGGER.info("Registered user. Logging in");
//				loginUser = activeUser;
//				loginCredentials = true;
//				break;
//			} else if (loginUser.getCellphone().equals(activeUser.getCellphone()) &&
//					   loginUser.getPassword().equals(activeUser.getPassword()) ) {
//				LOGGER.info("loginUser matches " + activeUser);
//				LOGGER.info("Registered user. Logging in");
//				loginUser = activeUser;
//				loginCredentials = true;
//				break;
//			} else {
//				LOGGER.info(loginUser + " is not a match to " + activeUser);
//			}
//		}
//		// if logInUser does not have a first name, then they are a new user
//		// else, logInUser will be fully populated and this is a returning user
//		if (loginCredentials == true) {
//			ArrayList<String> accountIdsList = (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAllAccountsIds(loginUser);
//			loginUser.setAccountIds(accountIdsList);
//			if (loginUser.getCategories().size() == 0) loginUser.createCategories();
//			ArrayList<UserItemsObject> allUsersItemsList = MiBudgetState.getItemDAOImpl().getAllUserItems(loginUser);
//
//			// Populate accounts and institutions map
//			HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
//			for (UserItemsObject uiObj : allUsersItemsList) {
//				Item item = MiBudgetState.getItemDAOImpl().getItem(uiObj.getItem__id() );
//				acctsAndInstitutionIdMap.put(item.getInstitutionId(), MiBudgetState.getAccountDAOImpl().getAllAccountsForItem(uiObj.getItem__id()) );
//			}
//			int accountsTotal = 0;
//			LOGGER.info("acctsAndInstitutionIdMap");
//			for(String id : acctsAndInstitutionIdMap.keySet()) {
//				LOGGER.info("key: " + id);
//				for (Account a : acctsAndInstitutionIdMap.get(id)) {
//					LOGGER.info("value: " + a);
//					accountsTotal++;
//				}
//			}
//
//			//loginUser.setCategories(miBudgetDAOImpl.getAllCategories(loginUser));
//
//			// Populate errors map
//
//			LOGGER.info("acctsAndInstitutionIdMap size: " + acctsAndInstitutionIdMap.size());
//			if (accountsTotal == 0) acctsAndInstitutionIdMap = new HashMap<String, ArrayList<Account>>();
//			session = request.getSession(true);
//			if (session == null || session.isNew()) {
//				LOGGER.info("session is null. setting session");
//				LOGGER.info("requestSessionId: " + session.getId());
//			} else {
//				LOGGER.info("Session already exists... but they're just logging in so get new session");
//				session.invalidate();
//				session = request.getSession(true);
//			}
//			// Update time
//        	ArrayList<String> institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(loginUser);
//			session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
//			session.setAttribute("institutionIdsList", institutionIdsList);
//			session.setAttribute("institutionIdsListSize", institutionIdsList.size());
//			session.setAttribute("session", session); // just a check
//			session.setAttribute("sessionId", session.getId()); // just a check
//			session.setAttribute("isUserLoggedIn", true); // just a check
//			session.setAttribute("Firstname", loginUser.getFirstName());
//			session.setAttribute("Lastname", loginUser.getLastName());
//			session.setAttribute("user", loginUser);
//			session.setAttribute("accountsSize", accountsTotal);
//			session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
//			session.setAttribute("getTransactions", new JSONObject());
//			session.setAttribute("transactionsList", new JSONArray());
//			session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
//			session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
//			if (session.getAttribute("change").equals("change") ) session.setAttribute("change", "Once you finish adding accounts, and creating categories, your budget will appear here.");
//			else session.setAttribute("change", "This text will change after the user take actions");
//			LOGGER.info("Redirecting to Profile.jsp");
//			LOGGER.info(Constants.end);
//			// call Profile.doGet here
//			RequestDispatcher dispatcher = request.getRequestDispatcher( "/WEB-INF/view/Profile.jsp" );
//			dispatcher.forward(request, response);
//		}
//		else {
//			LOGGER.info("Redirecting to Register.jsp");
//			LOGGER.info(Constants.end);
//			RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher( "/WEB-INF/view/Register.jsp" );
//			dispatcher.forward( request, response );
//		}
//	}
//
//	@PostMapping("/")
//	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info(Constants.start);
//		LOGGER.info("Inside login()");
//		String cellphone = request.getParameter("cellphone");
//		String password = request.getParameter("password");
//		boolean loginCredentials = false;
//		List<User> allUsersList = null;
//		HttpSession session = request.getSession(false);
//		//Instant instantNow = Instant.now();
//
//		allUsersList = MiBudgetState.getMiBudgetDAOImpl().getAllUsers();
//		User loginUser = new User(cellphone, password);
//		LOGGER.info("loginUser: " + loginUser);
//
//		// Validate user
//		LOGGER.info("validating user credentials...");
//		LOGGER.info("cellphone: " + cellphone);
//		LOGGER.info("password: " + password);
//		// for every user in the list
//
//		for (User activeUser : allUsersList) {
//			if (loginUser.equals(activeUser)) {
//				LOGGER.info(loginUser + " matches " + activeUser);
//				LOGGER.info("Registered user. Logging in");
//				loginUser = activeUser;
//				loginCredentials = true;
//				break;
//			} else if (loginUser.getCellphone().equals(activeUser.getCellphone()) &&
//					loginUser.getPassword().equals(activeUser.getPassword()) ) {
//				LOGGER.info("loginUser matches " + activeUser);
//				LOGGER.info("Registered user. Logging in");
//				loginUser = activeUser;
//				loginCredentials = true;
//				break;
//			} else {
//				LOGGER.info(loginUser + " is not a match to " + activeUser);
//			}
//		}
//		// if logInUser does not have a first name, then they are a new user
//		// else, logInUser will be fully populated and this is a returning user
//		if (loginCredentials == true) {
//			ArrayList<String> accountIdsList = (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAllAccountsIds(loginUser);
//			loginUser.setAccountIds(accountIdsList);
//			if (loginUser.getCategories().size() == 0) loginUser.createCategories();
//			ArrayList<UserItemsObject> allUsersItemsList = MiBudgetState.getItemDAOImpl().getAllUserItems(loginUser);
//
//			// Populate accounts and institutions map
//			HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
//			for (UserItemsObject uiObj : allUsersItemsList) {
//				Item item = MiBudgetState.getItemDAOImpl().getItem(uiObj.getItem__id() );
//				acctsAndInstitutionIdMap.put(item.getInstitutionId(), MiBudgetState.getAccountDAOImpl().getAllAccountsForItem(uiObj.getItem__id()) );
//			}
//			int accountsTotal = 0;
//			LOGGER.info("acctsAndInstitutionIdMap");
//			for(String id : acctsAndInstitutionIdMap.keySet()) {
//				LOGGER.info("key: " + id);
//				for (Account a : acctsAndInstitutionIdMap.get(id)) {
//					LOGGER.info("value: " + a);
//					accountsTotal++;
//				}
//			}
//
//			//loginUser.setCategories(miBudgetDAOImpl.getAllCategories(loginUser));
//
//			// Populate errors map
//
//			LOGGER.info("acctsAndInstitutionIdMap size: " + acctsAndInstitutionIdMap.size());
//			if (accountsTotal == 0) acctsAndInstitutionIdMap = new HashMap<String, ArrayList<Account>>();
//			session = request.getSession(true);
//			if (session == null || session.isNew()) {
//				LOGGER.info("session is null. setting session");
//				LOGGER.info("requestSessionId: " + session.getId());
//			} else {
//				LOGGER.info("Session already exists... but they're just logging in so get new session");
//				session.invalidate();
//				session = request.getSession(true);
//			}
//			// Update time
//			ArrayList<String> institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(loginUser);
//			session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
//			session.setAttribute("institutionIdsList", institutionIdsList);
//			session.setAttribute("institutionIdsListSize", institutionIdsList.size());
//			session.setAttribute("session", session); // just a check
//			session.setAttribute("sessionId", session.getId()); // just a check
//			session.setAttribute("isUserLoggedIn", true); // just a check
//			session.setAttribute("Firstname", loginUser.getFirstName());
//			session.setAttribute("Lastname", loginUser.getLastName());
//			session.setAttribute("user", loginUser);
//			session.setAttribute("accountsSize", accountsTotal);
//			session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
//			session.setAttribute("getTransactions", new JSONObject());
//			session.setAttribute("transactionsList", new JSONArray());
//			session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
//			session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
//			if (session.getAttribute("change").equals("change") ) session.setAttribute("change", "Once you finish adding accounts, and creating categories, your budget will appear here.");
//			else session.setAttribute("change", "This text will change after the user take actions");
//			LOGGER.info("Redirecting to Welcome.jsp");
//			LOGGER.info(Constants.end);
//			// call Profile.doGet here
//			RequestDispatcher dispatcher = request.getRequestDispatcher( "/view/Welcome.jsp" );
//			dispatcher.forward(request, response);
//		}
//		else {
//			LOGGER.info("Redirecting to Register.jsp");
//			LOGGER.info(Constants.end);
//			RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher( "/view/Register.jsp" );
//			dispatcher.forward( request, response );
//		}
//	}
//}
