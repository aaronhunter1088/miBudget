package com.v1.miBudget.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.AccountsGetRequest;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.Account;
import com.plaid.client.response.AccountsGetResponse;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.v1.miBudget.daoimplementations.AccountDAOImpl;
import com.v1.miBudget.daoimplementations.ItemDAOImpl;
import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.v1.miBudget.entities.Item;
import com.v1.miBudget.entities.User;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Servlet implementation class Authenticate
 */
@WebServlet("/authenticate") // rename to /get_access_token. this is what the example shows
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	
	private final String client_id = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5";
	
	public PlaidClient client() {
		// Use builder to create a client
		PlaidClient client = PlaidClient.newBuilder()
				  .clientIdAndSecret(client_id, secret)
				  .publicKey("") // optional. only needed to call endpoints that require a public key
				  .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
				  .build();
		return client;
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authenticate() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public int addAccountsToAccountsTableDatabase(List<com.v1.miBudget.entities.Account> accounts, Item item, User user) {
		Iterator<com.v1.miBudget.entities.Account> iter = accounts.iterator();
    	int verify = 0;
		while (iter.hasNext()) {
    		com.v1.miBudget.entities.Account account = iter.next();
    		// update account availableBalance, currentBalance, limit, currencyCode, item, figure out how to unmask mask to reveal last 4 of account
    		verify = AccountDAOImpl.addAccountObjectToAccountsTableDatabase(account, item, user);
    		verify = AccountDAOImpl.addAccountObjectToUsers_AccountsTable(account, user);
    	}
    	if (verify == 0)
    		return 0;
    	return 1;
    }
    
    /**
     * This method takes the institution_id and saves it to the 
     * database with the appropriate user.
     * @param inst_id
     * @param user
     * @return
     */
    public int addInstitutionIdToUsersInstitutionIdsTableInDatabase(String ins_id, User user) {
    	int verify = miBudgetDAOImpl.addInstitutionIdToDatabase(ins_id, user);
    	return verify;
    }
    
    public int addItemToUsersItemsInDatabase(Item item, User user) {
    	int itemTableId = 0;
    	try {
    		//Session mysqlSession = factory.openSession();
    		// get the item_table_ids from the database
    		itemTableId = ItemDAOImpl.getItemTableIdForItemId(item.getItemId());
    		
    		//mysqlSession = factory.openSession();
    		int verify = miBudgetDAOImpl.addItemToUsersItemsTable(itemTableId, user);
    		if (verify == 0)
    			return 0;
    		//verify = ItemDAOImpl.addItemToItemTable(item);
    		if (verify == 0)
    			return 0;
    		//System.out.println(item.getItemId() + " successfully added to " + user.getFirstname());
    		return 1; // good
    	} catch (HibernateException e) {
    		System.out.println("\nFailed to start SQL Session\n");
    		System.out.println(e.getMessage());
    		System.out.println(e.getStackTrace());
    	} catch (IllegalStateException e) {
    		System.out.println("\nSession/Entity Manager is closed.\n");
    		System.out.println(e.getMessage());
    		System.out.println(e.getStackTrace());
    	}
    	return 0; // 0 == bad
    }
    
    public List<String> getAllItemIds() {
    	List<String> allItemIdsList = new ArrayList<>();
		allItemIdsList = ItemDAOImpl.getAllItemIds();
		return allItemIdsList;
    }
    
    public List<Item> getAllItems() {
    	List<Item> allItemsList = new ArrayList<>();
    	allItemsList = ItemDAOImpl.getAllItems();
    	return allItemsList;
    }
    
    public int addItemToDatabase(List<Item> allItems, Item item) {
    	try {
    		
    		for (Item i : allItems) {
        		if (i.getItemTableId() == item.getItemTableId()) {
        			System.out.println("next item: " + i);
        			System.out.println("Item already added to database!");
        			return 0; // bad
        		}
        	}
    		// if the item does not exist in the database, add it
        	System.out.println("before save...");
        	int verify = ItemDAOImpl.addItemToDatabase(item);
        	if (verify == 0)
        		return 0;
        	return 1;
    	} catch (HibernateException e) {
    		System.out.println("\nFailed to start SQL Session\n");
    		e.printStackTrace(System.out);
    	} catch (IllegalStateException e) {
    		System.out.println("\nSession/Entity Manager is closed.\n");
    		e.printStackTrace(System.out);
    	}
    	return 0; // 0 == bad
    	
    	// for every Item in the list, if our item we are attempting to add
    	// is the same, do not add it.
    	
    	
    	
    }

    /**
     * The reason this method returns a String is because we ultimately
     * want authenticate to be successful. So if the response is good,
     * and we were able to do all the processing on the response that
     * we needed to do, we return the String "SUCCESS". If somewhere
     * it fails to do the logic, we return the String "FAIL" along 
     * with a message describing where we failed. 
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
	private String authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
		// TODO Auto-generated method stub
		System.out.println("Inside authenticate()...");	
		HttpSession session = request.getSession(false);
		if (session.getId() != session.getAttribute("sessionId")) {
			System.out.println("not the same session");
			session = request.getSession();
		} else {
			System.out.println("same session");
		}
		String public_token = request.getParameter("public_token");
		System.out.println("public_token: " + public_token);
		
		// Using some DAO, get a list of all institution_ids the user has added
		// If the current_institution_id is not in the list, proceed on, else,
		// return "FAILURE: institution_id already added"
		
		ItemPublicTokenExchangeRequest publicTokenExchangeRequest = new ItemPublicTokenExchangeRequest(public_token);
		
		Response<ItemPublicTokenExchangeResponse> publicTokenExchangeResponse =
				client().service().itemPublicTokenExchange(publicTokenExchangeRequest).execute();
		//System.out.println("publicTokenExchangeResponseBody: " + publicTokenExchangeResponse.body().toString());
		
		// access token notes: An access_token is used to access product data for an Item
		//System.out.println("publicTokenExchangeResponseAccessToken: " + publicTokenExchangeResponse.body().getAccessToken());
		//System.out.println("accessTokenLength: " + publicTokenExchangeResponse.body().getAccessToken().length());
		// item_id notes: An item_id is used to identify an Item in a webhook.
		//System.out.println("publicTokenExchangeResponseItemId: " + publicTokenExchangeResponse.body().getItemId());
		//System.out.println("itemIdLength: " + publicTokenExchangeResponse.body().getItemId().length());
		// request_id notes: used to keep track of user's session and what they did (i believe)
		//System.out.println("publicTokenExchangeResponseRequestId: " + publicTokenExchangeResponse.body().getRequestId());
		
		String accessToken = publicTokenExchangeResponse.body().getAccessToken();
		String itemID = publicTokenExchangeResponse.body().getItemId();
		
		// META DATA - Accounts the user selected
		String accountsRequested = request.getParameter("accounts");
		String institution_id = request.getParameter("institution_id");
		String institution_name = request.getParameter("institution_name");
		String link_session_id = request.getParameter("link_session_id");
		System.out.println("accountsRequested: " + accountsRequested);
		System.out.println("institution_id: " + institution_id);
		System.out.println("instution_name: " + institution_name);
		System.out.println("link_session_id: " + link_session_id);
		
		// This was wrong. But I left to show my learning.
		// Create a public_token for use with Plaid Link's update mode
//		retrofit2.Response<ItemPublicTokenCreateResponse> responseCall =
//		   plaidClient.service().itemPublicTokenCreate(
//		   new ItemPublicTokenCreateRequest(accessToken)).execute();
		JSONParser parser = new JSONParser();
		JSONArray accountsRequestedJsonArray = null;
		try {
			accountsRequestedJsonArray = (JSONArray) parser.parse(accountsRequested);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("number of accounts requested: " + accountsRequestedJsonArray.size());
		
		List<com.v1.miBudget.entities.Account> accountsList = new ArrayList<>();
		List<String> accountIdsList = new ArrayList<>();
		JSONObject jsonObject = null;
		for(int i = 0; i < accountsRequestedJsonArray.size(); i++) {
			try {
				jsonObject = (JSONObject) parser.parse(accountsRequestedJsonArray.get(i).toString());
//				accountIdsList.add((parser.parse(accountsRequestedJsonArray.get(i).toString())).toString());
				accountIdsList.add(jsonObject.get("id").toString());
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace(System.out);
			}
		}
		accountIdsList.forEach(id -> {
			System.out.println(id);
		});
//		for(int i = 0; i < jsonObjectHoldsAcctIds.size(); i++) {
//			accountIdsList.add(jsonObjectHoldsAcctIds.get(i).toString());
//		}
		
//		for(int i = 0; i < accountsRequestedJsonArray.size(); i++) {
//			JSONObject jsonObject = null;
//			try {
//				jsonObject = (JSONObject) parser.parse(accountsRequestedJsonArray.get(i).toString());
//			} catch (org.json.simple.parser.ParseException e) {
//				e.printStackTrace(System.out);
//			} 
//			acctObj = new com.v1.miBudget.entities.Account();
		
		// Make call to AccountsGetResponse to receive additional information about the requested accounts
		AccountsGetRequest acctsGetReq = new AccountsGetRequest(accessToken);
		acctsGetReq.clientId = client_id;
		acctsGetReq.secret = secret;
		acctsGetReq.withAccountIds(accountIdsList);
		Response<AccountsGetResponse> acctsGetRes = 
				client().service().accountsGet(acctsGetReq).execute(); //.withAccountIds(accountIdsList)
		System.out.println("acctsGetRes body: " + acctsGetRes.body().toString());
		System.out.println("acctsGetRes code: " + acctsGetRes.code());
		System.out.println("acctsGetRes msg: " + acctsGetRes.message());
		System.out.println("acctsGetRes raw : " + acctsGetRes.raw());
		System.out.println("acctsGetRes err: " + acctsGetRes.errorBody());
		if (acctsGetRes.message().equals("Bad Request")) {
			System.out.println("acctsGetRes errString: " + acctsGetRes.errorBody().string());
		}
		if (acctsGetRes.isSuccessful()) {
			System.out.println("AccountsGetResponse : " + acctsGetRes.code());
			List<Account> accountsFromRes = acctsGetRes.body().getAccounts();
			accountsFromRes.forEach(acct -> {
				System.out.println("acctsGetRes account: " + ((Account)acct).toString());
			});
			
			
			//Iterator<Account> iterOverAccountsGetRes = accountsFromRes.iterator();
			
			// while accountIds has an Id
			// check if there is an account to check from response
			// if yes, check if both Id's are the same
			// 		if yes, store that information in the current acctObj
			//		if no, continue with next account from response
			
			Iterator<String> iterOverAccountIdsList = accountIdsList.iterator();
			while (iterOverAccountIdsList.hasNext()) {
				String currentIdFromAccountIdsList = iterOverAccountIdsList.next().toString();
				Iterator<Account> iterOverAccountsGetRes = accountsFromRes.iterator();
				res:
				while (iterOverAccountsGetRes.hasNext()) {
					Account currentAcctFromResAcct = (Account)iterOverAccountsGetRes.next();
					if (currentIdFromAccountIdsList.equals(currentAcctFromResAcct.getAccountId()) ){
						com.v1.miBudget.entities.Account acctObj = new com.v1.miBudget.entities.Account();
						System.out.println("Id's matched!");
						System.out.println(currentIdFromAccountIdsList + " == " + currentAcctFromResAcct.getAccountId());
						acctObj.setAccountId(currentAcctFromResAcct.getAccountId());
						acctObj.setAvailableBalance(currentAcctFromResAcct.getBalances().getAvailable() != null ? currentAcctFromResAcct.getBalances().getAvailable() : 0.0);
						acctObj.setCurrentBalance(currentAcctFromResAcct.getBalances().getCurrent() != null ? currentAcctFromResAcct.getBalances().getCurrent() : 0.0);
						acctObj.setLimit(currentAcctFromResAcct.getBalances().getLimit() != null ? currentAcctFromResAcct.getBalances().getLimit() : 0.0);
						acctObj.setCurrencyCode(currentAcctFromResAcct.getBalances().getIsoCurrencyCode() != null ? currentAcctFromResAcct.getBalances().getIsoCurrencyCode() : "USD");
						acctObj.setMask(currentAcctFromResAcct.getMask());
						acctObj.setNameOfAccount(currentAcctFromResAcct.getName());
						acctObj.setOfficialName(currentAcctFromResAcct.getOfficialName() != null ? currentAcctFromResAcct.getOfficialName() : "");
						acctObj.setType(currentAcctFromResAcct.getType());
						acctObj.setSubType(currentAcctFromResAcct.getSubtype());
						System.out.println("Adding acctObj to accountsList...");
						accountsList.add(acctObj);
						break res;
					} else {
						System.out.println("Id's didn't match. Not adding anything to accountsList.");
						//continue;
					}
				} 
				System.out.println("Continuing with outer while loop.");
				//iterOverAccountIdsList.next();
			} // end while
		} // end if acctsGetRes.isSuccessful()
		else {
			return "FAIL: AccountsGetResponse failed";
		}
			
			
//			No longer needed
//			final List<String> keys = Arrays.asList("id", "name", "mask", "type", "subtype");
//			for (String key : keys) {
//				switch (key) {
//					case "id" :		 acctObj.setAccountId(jsonObject.get(key).toString());
//			    					 break;
//					case "name" :    acctObj.setNameOfAccount(jsonObject.get(key).toString());
//									 break;
//					case "mask" :    acctObj.setMask(jsonObject.get(key).toString());
//									 break;
//					case "type" :    acctObj.setType(jsonObject.get(key).toString());
//									 break;
//					case "subtype" : acctObj.setSubType(jsonObject.get(key).toString());
//									 break;
//				}
//			} // end switching on all keys
//			accountsList.add(acctObj);
			
//		} // end main loop for building my entity Account objects
		
		AtomicInteger an = new AtomicInteger(1);
		System.out.println("accountsRequested after parsing...");
		accountsList.forEach(account -> {
			System.out.println(an.getAndAdd(1) + ") " + account.toString());
		});
		
		System.out.println("Responses code:\n\tPublic Token Exchange: " + publicTokenExchangeResponse.code() + "\n\tAccountsGet: " + acctsGetRes.code());
		
		// If both the call to exchange the public token for an access token AND
		// If the call to get accounts information is Successful
		if (publicTokenExchangeResponse.isSuccessful() && acctsGetRes.isSuccessful()) {
		  
		  // Get user from request session
		  User user = (User) session.getAttribute("user");
		  try {
			  if (user.getFirstName().equals(null)) {}
		  } catch (NullPointerException e) {
			  return "FAIL: No user!";
		  }
		  // NOTE: added another obj with same name as above. 
		  // To simplify, reusing same obj here. Watch for errors.
		  accountIdsList = User.getAccountIds(user);
		  // CHECK: Print out all account_ids for current user
		  for (String account_id : accountIdsList) {
			  System.out.println("user's name: " + user.getFirstName() + " \naccount_id: " + account_id);
		  }
		  
		  Item itemToAdd = new Item(accessToken, itemID);
		  session.setAttribute("CreatedItem", itemToAdd);
		  System.out.println("created item: " + itemToAdd);
		  
		  // Add 'itemToAdd' to items table  
		  System.out.println("Adding item to database");
		  int verify = addItemToDatabase(getAllItems(), itemToAdd);
		  if (verify == 0)
			  return "FAIL: did not add item to database.";
		  else
			  System.out.println("Item added to database.");
		  
		  // Add created Item to users_items table
		  verify = addItemToUsersItemsInDatabase(itemToAdd, user);
		  if (verify == 0)
			  return "FAIL: did not add item_id to user in database.";
		  else
			  System.out.println("Item added to Users_Items table in database");
		  
		  // Add institution_id to users_institution_ids table
		  verify = addInstitutionIdToUsersInstitutionIdsTableInDatabase(institution_id, user);
		  if (verify == 0)
			  return "FAIL: did not add institution_id to table.";
		  else {
			  System.out.println("Institution_id added to users_institution_ids table in database");
			  ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
			  session.setAttribute("institutionIdsList", institutionIdsList);
		  }
		  // Add accounts to users profile
		  int itemTableId = ItemDAOImpl.getItemTableIdForItemId(itemToAdd.getItemId());
		  accountsList.forEach(account -> {
			  account.setItemTableId(itemTableId);
		  });
		  // Need the above lines to set item_table_id for the accounts. they all have the same item
		  // But need to set to proper item_table_id that was set.
		  verify = addAccountsToAccountsTableDatabase(accountsList, itemToAdd, user);
		  if (verify == 0)
			  return "FAIL: did not add accounts to user's profile.";
		  else
			  System.out.println("Accounts added to user's profile.");
		  
		  // Add accountsList to requestSession
		  ArrayList<String> listOfAccountIds = (ArrayList<String>) AccountDAOImpl.getAccountIdsFromUser(user);
		  ArrayList<com.v1.miBudget.entities.Account> listOfAccounts = (ArrayList<com.v1.miBudget.entities.Account>) AccountDAOImpl.getAllAccounts(user, listOfAccountIds);
		  listOfAccounts.forEach(account -> {
			  System.out.println(account);
			  //request.getSession(false).setAttribute("Name", account.getNameOfAccount());
			  //request.getSession(false).setAttribute("Mask", account.getMask());
			  //request.getSession(false).setAttribute("SubType", account.getSubType());
		  });
		  
		  // Accounts list is all accounts in users profile
		  int numberOfAccounts = AccountDAOImpl.getAccountIdsFromUser(user).size();
		  session.setAttribute("accountsSize", numberOfAccounts);
		  
		  // Place user object back in request
		  user.setAccountIds(listOfAccountIds);
//		  request.getSession(false).setAttribute("user", user);
//		  request.getSession(false).setAttribute("listOfAccountIds", listOfAccountIds);
		  session.setAttribute("user", user);
		  session.setAttribute("listOfAccountIds", listOfAccountIds);
		  session.setAttribute("listOfAccount", listOfAccounts);
		  String strAccounts = (acctsGetRes.body().getAccounts().size() == 1) ? " account!" : " accounts!";
		  session.setAttribute("change", "You have successfully loaded " + acctsGetRes.body().getAccounts().size() + strAccounts);
		  // update user in requestSession
		  
		  
		  //response.setContentType("application/json");
		  return "SUCCESS";
		}
//		response.sendRedirect("Profile.jsp");
		return "FAIL";
	}

	
	
	/**
	 * Return "SUCCESS" if the response is good.
	 * Return "FAIL" + message if the response fails.
	 * @param user
	 * @return
	 * @throws IOException 
	 */
	public List<Account> getAccountsForCreatedItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
		System.out.println("getAccountsForCreatedItem: meta_data: " + request.getParameter("meta_data"));
		Item createdItem = (Item) request.getSession(false).getAttribute("CreatedItem");
		String access_token = createdItem.getAccessToken();
		System.out.println("access_token: " + access_token);
		AccountsGetRequest agrequest = new AccountsGetRequest(access_token);
		agrequest.clientId = client_id;
		agrequest.secret = secret;
		// TODO: Fix
//		Response<AccountsGetResponse> agr = client().service().accountsGet(agrequest);
//		
//		// it appears we get all accounts, not just the one's we've selected in Link
//		if (agr.isSuccessful()) {
//			int code = agr.code();
//			System.out.println("accountsGetResponse code: " + code);
//			return agr.body().getAccounts();
//		} else {
//			System.out.println("Failed to get the accounts! " + agr.errorBody().string());
//			return new ArrayList<>();
//		}
		// TODO: Fix: Remove this return when Fix is done.
		return null;
		// TODO: End Fix
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside Authenticate servlet doPost.");
		// Check to see if institution selected has already been saved
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String institution_idIncoming = request.getParameter("institution_id");
		ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
		boolean exit = false;
		Iterator<String> iter = institutionIdsList.iterator();
		while (iter.hasNext()) {
			String id = iter.next();
			if (id.equals(institution_idIncoming)) {
				System.out.println(institution_idIncoming + " has already been added. We cannot add it again.");
				exit = true;
				break;
			} else
				System.out.println(id + " - This id did not match the one selected.");
		}
		if (exit) {
			System.out.println("Finished with doPost");
			System.out.println("");
			int numberOfAccounts = AccountDAOImpl.getAccountIdsFromUser(user).size();
			request.getSession(false).setAttribute("NoOfAccts", numberOfAccounts);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/text");
			
			return;
		}
		
		System.out.println("About to perform authenticate.");
		String authResponse = authenticate(request, response);
		//request.getSession(false).setAttribute("authResponse", authResponse);
		System.out.println("Authenticate response: " + authResponse);
		
		
		if (authResponse.equals("SUCCESS")) {
			// add session back to response obj
			response.setContentType("application/html");
			response.setStatus(HttpServletResponse.SC_OK);
//			getServletContext().getRequestDispatcher("/Profile.jsp").forward(request, response);
			//response.sendRedirect("Profile.jsp");
			return;
		} else {
			//response.sendRedirect("Profile.jsp");
			return;
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nInside the Profile servlet doGet().");
	}

}
