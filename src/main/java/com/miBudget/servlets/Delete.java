package com.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.daoimplementations.AccountDAOImpl;
import com.miBudget.daoimplementations.ItemDAOImpl;
import com.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.entities.Account;
import com.miBudget.entities.Item;
import com.miBudget.entities.User;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemRemoveRequest;
import com.plaid.client.response.ItemRemoveResponse;

import retrofit2.Response;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private final String clientId = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Delete.class);
	}
	
    public final PlaidClient client() {
		// Use builder to create a client
		PlaidClient client = PlaidClient.newBuilder()
				.clientIdAndSecret(clientId, secretD)
				.publicKey("") // optional. only needed to call endpoints that require a public key
				.developmentBaseUrl() // or equivalent, depending on which environment you're calling into
				.build();
		return client;
    }   
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
		LOGGER.info("--- START ---");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		LOGGER.info("--- END ---");
	}

	
	public HashMap<String, Object> deleteAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String accountId = request.getParameter("accountId");
		ArrayList<String> list = new ArrayList<>();
		list.add(accountId);
		Account account = accountDAOImpl.getAllAccounts(list).get(0);
		if (!account.getAccountId().equals(accountId)) LOGGER.error("Wrong account received!!");
		LOGGER.info("Attempting to delete: " + account);
		HttpSession session = request.getSession(false);  
		HashMap<String, Object> deleteResponse = new HashMap<String, Object>();
		
//		String institutionId = request.getParameter("currentId");
		User user = (User)session.getAttribute("user"); 
		Item item = itemDAOImpl.getItem(Integer.parseInt(request.getParameter("itemTableId")));
		ArrayList<String> accountIdsList = (ArrayList<String>)accountDAOImpl.getAccountIdsFromUser(item);
		LOGGER.info("accountIdsList size: " + accountIdsList.size());
		accountIdsList.remove(accountId);
		user.setAccountIds(accountIdsList);
		LOGGER.info("accountIdsList size: " + accountIdsList.size());
		
		boolean usersInstitutionIdsResult = false;
		boolean usersItemsResult = false;
		int itemsResult = 0;
		
		// Reference Tables
		if (accountIdsList.size() < 1) {
			usersInstitutionIdsResult = itemDAOImpl.deleteFromUsersInstitutionIds(item, user);
			usersItemsResult = itemDAOImpl.deleteFromUsersItems(item);
		}
		boolean usersAccountsResult = accountDAOImpl.deleteAccountFromUsersAccounts(accountId, item, user);
		boolean itemsAccountsResult = accountDAOImpl.deleteFromItemsAccounts(item, accountId);
		LOGGER.info("usersInstitutionIdsResult: " + usersInstitutionIdsResult);
		LOGGER.info("usersItemsResult: " + usersItemsResult);
		LOGGER.info("usersAccountsResult: " + usersAccountsResult);
		LOGGER.info("itemsAccountsResult: " + itemsAccountsResult);
		
		// Main Tables
		boolean accountsResult = accountDAOImpl.deleteAccountFromDatabase(account);
		if (usersInstitutionIdsResult == true && usersItemsResult == true) {
			itemsResult = itemDAOImpl.deleteItemFromDatabase(item);
			
		}
		
		deleteResponse.put("usersInstitutionIdsResult", usersInstitutionIdsResult);
		deleteResponse.put("usersAccountsResult", usersAccountsResult);
		deleteResponse.put("usersItemsResult", usersItemsResult); 
		deleteResponse.put("itemsAccountsResult", itemsAccountsResult);
		deleteResponse.put("accountsResult", accountsResult);
		deleteResponse.put("itemsResult", itemsResult == 1 ? true : false);
		deleteResponse.put("name", account.getOfficialName().equals(null) ? account.getAccountName() : account.getOfficialName());
		
		return deleteResponse;
	}
	public String deleteBank(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);  
		String institutionId = request.getParameter("currentId");
		LOGGER.info("Attempting to delete bank: " + institutionId);
		Item item = itemDAOImpl.getItemFromUser(institutionId);
		//Item item = itemDAOImpl.createItemFromInstitutionId(institutionId);
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
		} else { LOGGER.info("The item was successfully deleted."); }
		if (verify == 1) {
			Response<ItemRemoveResponse> itemRemoveRes =  client().service()
					.itemRemove(new ItemRemoveRequest(item.getAccessToken()))
					.execute();
					// The Item has been removed and the access token is now invalid
			boolean isRemoved = false;
			if (itemRemoveRes.isSuccessful()) {
				isRemoved = itemRemoveRes.body().getRemoved();
			} else {
				LOGGER.info(itemRemoveRes.errorBody().string());
				return "FAIL: item's access token was not invalidated. Request failed.";
			}
			LOGGER.info(item.getAccessToken() + " was invalidated?: " + isRemoved);
			if (isRemoved == true) {
				//ArrayList<UsersItemsObject> usersItemsList = itemDAOImpl.getAllUserItems((User)session.getAttribute("user"));
				@SuppressWarnings("unchecked")
				HashMap<Integer, ArrayList<Account>> acctsAndInstitutionIdMap = (HashMap<Integer, ArrayList<Account>>) 
						session.getAttribute("acctsAndInstitutionIdMap");
				acctsAndInstitutionIdMap.remove(item.get_id());
				// update session values
				int numberOfAccounts = accountDAOImpl.getAccountIdsFromUser(user).size();
				ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
				session.setAttribute("institutionIdsList", institutionIdsList);
				session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
				session.setAttribute("institutionIdsListSize", institutionIdsList.size());
				session.setAttribute("accountsSize", numberOfAccounts);
			} else {
				return "FAIL: access token was not invalidated for " + item.getInstitutionId();
			}
		}
		
		
		// Need to call /item/delete to invalidate access-token for Item
		return "deleteBank: SUCCESS";
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "unlikely-arg-type", "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		LOGGER.info(Constants.start);
		LOGGER.info("Inside the Delete doPost() servlet");
		LOGGER.info("currentId: " + request.getParameter("currentId"));
		LOGGER.info("requesting to delete: " + request.getParameter("delete"));
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("user"); 
		String institutionId = request.getParameter("currentId");
		int itemTableId = 0;
		Item item = null;
		// Perform the following logic:
		if (request.getParameter("delete").equals("bank")) {
			String deleteResponse = deleteBank(request, response);
			LOGGER.info("deleteResponse: " + deleteResponse);
			HashMap<String, Boolean> errMapForItems = new HashMap<>();
			ArrayList<String> ids = (ArrayList<String>) session.getAttribute("institutionIdsList");
			ArrayList<Item> items = new ArrayList<>();
			for(int i = 0; i < ids.size(); i++) {
				LOGGER.info(item);
				items.add(item);
			}
			if (deleteResponse.contains("SUCCESS")) {
				session.setAttribute("change", "You have successfully deleted your bank.");
				session.setAttribute("ErrMapForItems", errMapForItems);
			} else {
				session.setAttribute("change", "There was an issue deleting your bank: " + deleteResponse);
			}
			
		}
		else if (request.getParameter("delete").equals("account")) {
			itemTableId = Integer.parseInt(request.getParameter("itemTableId"));
			item = itemDAOImpl.getItem(itemTableId);
			String accountId = request.getParameter("accountId");
			ArrayList<String> acctIdToDeleteList = new ArrayList<>();
			acctIdToDeleteList.add(accountId);
			Account account = accountDAOImpl.getAllAccounts(acctIdToDeleteList).get(0);
			HashMap<String, Object> deleteAccountResponse = deleteAccount(request, response);
			
			HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = 
					(HashMap<String, ArrayList<Account>>) session.getAttribute("acctsAndInstitutionIdMap");
			ArrayList<Account> newAcctList = new ArrayList<>();
			newAcctList = acctsAndInstitutionIdMap.get(institutionId);
			newAcctList.remove(account);
			acctsAndInstitutionIdMap.put(institutionId, newAcctList);
			LOGGER.info("newAcctList size: " + newAcctList.size());
			
			LOGGER.info("Updated acctsAndInstitutionIdMap");
			for (String key : acctsAndInstitutionIdMap.keySet()) {
				LOGGER.info("key: " + key);
				for (Account a : acctsAndInstitutionIdMap.get(key)) {
					LOGGER.info("\t" + a);
				}
			}
			
			if (deleteAccountResponse.get("usersInstitutionIdsResult") == Boolean.TRUE) {
				LOGGER.info("Boolean result is true");
				ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
				session.setAttribute("institutionIdsList", institutionIdsList);
				session.setAttribute("institutionIdsListSize", institutionIdsList.size());
						
				acctsAndInstitutionIdMap.remove(item.get_id());
			} else {
				LOGGER.info("Boolean result is true ELSE");
				ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
				session.setAttribute("institutionIdsList", institutionIdsList);
				session.setAttribute("institutionIdsListSize", institutionIdsList.size());
						
				acctsAndInstitutionIdMap.remove(item.get_id());
			}
			
			// If still has 1 or more accounts for an item
			// insIdsList, acctsAndInsIdMap, and accounts sizes don't change
			session.setAttribute("change", "You have successfully deleted your " + deleteAccountResponse.get("name") + " account.");
			session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
			
			session.setAttribute("accountsSize", (Integer)session.getAttribute("accountsSize")-1);

		}
		LOGGER.info(Constants.end);
		//response.setContentType("application/html");
		//response.sendRedirect("Accounts.jsp");
		response.setStatus(HttpServletResponse.SC_OK);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/WEB-INF/view/Accounts.jsp" );
		dispatcher.forward( request, response );
	}

}
