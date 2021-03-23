package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.time.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.v1.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.Transaction;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UsersItemsObject;
import com.miBudget.v1.utilities.DateAndTimeUtility;


/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Login.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info(Constants.start);
		LOGGER.info("Inside the Login doGet() servlet.");
		LOGGER.info("Redirecting to Login.html.");
		LOGGER.info(Constants.end);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	//@SuppressWarnings("null")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info(Constants.start);
		LOGGER.info("Inside Login doPost() servlet.");
		String cellphone = request.getParameter("Cellphone");
		String password = request.getParameter("Password");
		boolean loginCredentials = false;
		List<User> allUsersList = null;
		HttpSession session = request.getSession(false);
		//Instant instantNow = Instant.now();

		allUsersList = miBudgetDAOImpl.getAllUsers();
		User loginUser = new User(cellphone, password);
		LOGGER.info("loginUser: " + loginUser);
		
		// Validate user
		LOGGER.info("validating user credentials...");
		LOGGER.info("cellphone: " + cellphone);
		LOGGER.info("password: " + password);
		// for every user in the list
		
		for (User activeUser : allUsersList) {
			if (loginUser.equals(activeUser)) {
				LOGGER.info(loginUser + " matches " + activeUser);
				LOGGER.info("Registered user. Logging in");
				loginUser = activeUser;
				loginCredentials = true;
				break;
			} else if (loginUser.getCellphone().equals(activeUser.getCellphone()) &&
					   loginUser.getPassword().equals(activeUser.getPassword()) ) {
				LOGGER.info("loginUser matches " + activeUser);
				LOGGER.info("Registered user. Logging in");
				loginUser = activeUser;
				loginCredentials = true;
				break;
			} else {
				LOGGER.info(loginUser + " is not a match to " + activeUser);
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
			int accountsTotal = 0;
			LOGGER.info("acctsAndInstitutionIdMap");
			for(String id : acctsAndInstitutionIdMap.keySet()) {
				LOGGER.info("key: " + id);
				for (Account a : acctsAndInstitutionIdMap.get(id)) {
					LOGGER.info("value: " + a);
					accountsTotal++;
				}
			}
			
			//loginUser.setCategories(miBudgetDAOImpl.getAllCategories(loginUser));
			
			// Populate errors map
			
			LOGGER.info("acctsAndInstitutionIdMap size: " + acctsAndInstitutionIdMap.size());
			if (accountsTotal == 0) acctsAndInstitutionIdMap = new HashMap<String, ArrayList<Account>>();
			session = request.getSession(true);
			if (session == null || session.isNew()) {
				LOGGER.info("session is null. setting session");
				LOGGER.info("requestSessionId: " + session.getId());
			} else {
				LOGGER.info("Session already exists... but they're just logging in so get new session");
				session.invalidate();
				session = request.getSession(true);
			}
			// Update time
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
			session.setAttribute("accountsSize", accountsTotal);
			session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
			session.setAttribute("getTransactions", new JSONObject());
			session.setAttribute("transactionsList", new JSONArray());
			session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
			session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
			LOGGER.info("Redirecting to Profile.jsp");
			LOGGER.info(Constants.end);
			// call Profile.doGet here
			RequestDispatcher dispatcher = request.getRequestDispatcher( "/WEB-INF/view/Profile.jsp" );
			dispatcher.forward(request, response);
		} else {
			LOGGER.info("Redirecting to Register.jsp");
			LOGGER.info(Constants.end);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/WEB-INF/view/Register.jsp" );
			dispatcher.forward( request, response );
		}
	}
}
