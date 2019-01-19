package com.miBudget.v1.servlets;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.google.gson.Gson;
import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UsersItemsObject;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.AccountsGetRequest;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.Account;
import com.plaid.client.response.AccountsGetResponse;
import com.plaid.client.response.ErrorResponse;
import com.plaid.client.response.ErrorResponse.ErrorType;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.ItemStatus;

import retrofit2.Response;


/**
 * Servlet implementation class Authenticate
 */
@WebServlet("/authenticate") // rename to /get_access_token. this is what the example shows
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	
	private final String client_id = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5";
	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
	public PlaidClient client() {
		// Use builder to create a client
		PlaidClient client = PlaidClient.newBuilder()
				  .clientIdAndSecret(client_id, secretD)
				  .publicKey("") // optional. only needed to call endpoints that require a public key
				  .developmentBaseUrl() // or equivalent, depending on which environment you're calling into
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
    
    /*
     * TODO: Update items_account table
     * Create an items_account object
     */
    
    public int addAccountsToAccountsTableDatabase(List<com.miBudget.v1.entities.Account> accountsRequested, Item item, User user) {
//		Iterator<com.miBudget.v1.entities.Account> iter = accounts.iterator();
    	int verify = 0;
		for (com.miBudget.v1.entities.Account account : accountsRequested) {
//    		com.miBudget.v1.entities.Account account = iter.next();
    		// update account availableBalance, currentBalance, limit, currencyCode, item, figure out how to unmask mask to reveal last 4 of account
			verify = accountDAOImpl.addAccountObjectToAccountsTableDatabase(account);
			verify = accountDAOImpl.addAccountObjectToUsersAccountsTable(account, user);
    		verify = accountDAOImpl.addAccountIdToItemsAccountsTable(item.getItemTableId(), account.getAccountId());
    		
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
    
    public int addItemToUsersItemsInDatabase(ArrayList<UsersItemsObject> usersItemsList, Item item, User user) {
    	try {
//    		itemTableId = itemDAOImpl.getItemTableIdForItemId(item.getItemId());
    		int itemTableId = item.getItemTableId();
    		
    		boolean brokenLoop = false;
    		int verify = 0;
    		if (usersItemsList.size() == 0) {
    			verify = miBudgetDAOImpl.addItemToUsersItemsTable(itemTableId, user);
    		} else {
    			for (UsersItemsObject uiObj : usersItemsList) {
        			if (uiObj.getItemTableId() == item.getItemTableId()) {
        				// we cannot add a bank again to a specific users list of items
        				brokenLoop = true;
        				break;
        			}
        		}
    			if (!brokenLoop)
    				verify = miBudgetDAOImpl.addItemToUsersItemsTable(itemTableId, user);
    		}
    		
    		if (verify == 1)
    			return 1;
    		if (verify == 0)
    			return 0;
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
		allItemIdsList = itemDAOImpl.getAllItemIds();
		return allItemIdsList;
    }
    
    /**
     * This method returns ALL the items added to the database.
     * @return
     */
    public ArrayList<Item> getAllItems() {
    	ArrayList<Item> allItemsList = new ArrayList<>();
    	allItemsList = itemDAOImpl.getAllItems();
    	return allItemsList;
    }
    
    /**
     * This method returns ALL the items for a specific user
     * @return
     */
    public ArrayList<UsersItemsObject> getAllUsersItems(User user) {
    	ArrayList<UsersItemsObject> usersItemsList = new ArrayList<>();
    	usersItemsList = itemDAOImpl.getAllUserItems(user);
    	return usersItemsList;
    }
    
    public int addItemToDatabase(Item item) {
    	try {
    		
    		// This logic does not make sense anymore. 
//    		for (Item i : allItems) {
//        		if (i.getItemTableId() == item.getItemTableId()) {
//        			System.out.println("next item: " + i);
//        			System.out.println("Item already added to database!");
//        			return 0; // bad
//        		}
//        	}
    		System.out.println("before save...");
        	int verify = itemDAOImpl.addItemToDatabase(item);
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
		System.out.println("\nInside authenticate()...");	
		HttpSession session = request.getSession(false);
		if (session.getId() != session.getAttribute("sessionId")) {
			System.out.println("not the same session");
			session = request.getSession();
			return "FAIL: this is not the same session...";
		} else {
			System.out.println("valid active session");
		}
		
		// Public token exchange request and response
		String public_token = request.getParameter("public_token");
		System.out.println("public_token: " + public_token);
		ItemPublicTokenExchangeRequest publicTokenExchangeRequest = new ItemPublicTokenExchangeRequest(public_token);
		Response<ItemPublicTokenExchangeResponse> publicTokenExchangeResponse =
				client().service().itemPublicTokenExchange(publicTokenExchangeRequest).execute();
		/**System.out.println("publicTokenExchangeResponseBody: " + publicTokenExchangeResponse.body().toString());
		// access token notes: An access_token is used to access product data for an Item
		System.out.println("publicTokenExchangeResponseAccessToken: " + publicTokenExchangeResponse.body().getAccessToken());
		System.out.println("accessTokenLength: " + publicTokenExchangeResponse.body().getAccessToken().length());
		// item_id notes: An item_id is used to identify an Item in a webhook.
		System.out.println("publicTokenExchangeResponseItemId: " + publicTokenExchangeResponse.body().getItemId());
		System.out.println("itemIdLength: " + publicTokenExchangeResponse.body().getItemId().length());
		// request_id notes: used to keep track of user's session and what they did (i believe)
		System.out.println("publicTokenExchangeResponseRequestId: " + publicTokenExchangeResponse.body().getRequestId()); */
		
		// Get itemId and accessToken
		String accessToken = "";
		String itemId = "";
		if (publicTokenExchangeResponse.isSuccessful()) {
			accessToken = publicTokenExchangeResponse.body().getAccessToken();
			itemId = publicTokenExchangeResponse.body().getItemId();
		}
		
		// META DATA - Accounts the user selected
		String accountsRequested = request.getParameter("accounts");
		String institutionId = request.getParameter("institution_id");
		String institutionName = request.getParameter("institution_name");
		String linkSessionId = request.getParameter("link_session_id");
		
		System.out.println("accountsRequested: " + accountsRequested);
		System.out.println("institutionId: " + institutionId);
		System.out.println("instutionName: " + institutionName);
		System.out.println("linkSessionId: " + linkSessionId);
		
		// Parse accountsRequested; store in accountIdsRequestedList
		JSONParser jsonParser = new JSONParser();
		JSONArray accountsRequestedJsonArray = null;
		try { accountsRequestedJsonArray = (JSONArray) jsonParser.parse(accountsRequested); }
		catch (Exception e) { e.printStackTrace(System.out); }
		System.out.println("number of accounts requested: " + accountsRequestedJsonArray.size());
		ArrayList<com.miBudget.v1.entities.Account> accountsRequestedList = new ArrayList<>();
		ArrayList<String> accountIdsRequestedList = new ArrayList<>();
		JSONObject jsonObject = null;
		for(int i = 0; i < accountsRequestedJsonArray.size(); i++) {
			try {
				jsonObject = (JSONObject) jsonParser.parse(accountsRequestedJsonArray.get(i).toString());
				accountIdsRequestedList.add(jsonObject.get("id").toString());
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace(System.out);
			}
		}
		accountIdsRequestedList.forEach(id -> {
			System.out.println(id);
		});
		
		// Make call to AccountsGetResponse to receive additional information about the requested accounts
		AccountsGetRequest acctsGetReq = new AccountsGetRequest(accessToken);
		acctsGetReq.clientId = client_id;
		acctsGetReq.secret = secretD;
		acctsGetReq.withAccountIds(accountIdsRequestedList);
		Response<AccountsGetResponse> acctsGetRes = 
				client().service().accountsGet(acctsGetReq).execute(); //.withAccountIds(accountIdsList)
		System.out.println("acctsGetRes body: " + acctsGetRes.body().toString());
		System.out.println("acctsGetRes code: " + acctsGetRes.code());
		System.out.println("acctsGetRes msg: " + acctsGetRes.message());
		System.out.println("acctsGetRes raw : " + acctsGetRes.raw());
		System.out.println("acctsGetRes err: " + acctsGetRes.errorBody());
		if (acctsGetRes.message().equals("Bad Request") || acctsGetRes.body().getAccounts().size() != accountsRequestedJsonArray.size() ) {
			System.out.println("acctsGetRes errString: " + acctsGetRes.errorBody().string());
			System.out.printf("/accounts/get endpoint retrieved %d accounts\n", acctsGetRes.body().getAccounts().size());
			acctsGetRes.body().getAccounts().forEach(acct -> {
				System.out.println(acct);
			});
			return "FAIL: " + acctsGetRes.errorBody().toString();
		}
		if (acctsGetRes.isSuccessful()) {
			System.out.println("AccountsGetResponse : " + acctsGetRes.code());
			List<Account> accountsFromRes = acctsGetRes.body().getAccounts();
			accountsFromRes.forEach(acct -> {
				System.out.println("acctsGetRes account: " + ((Account)acct));
			});

			for (String id : accountIdsRequestedList) {
				for (Account getResA : accountsFromRes) {
					if (id.equals(getResA.getAccountId()) ) {
						com.miBudget.v1.entities.Account acctObj = new com.miBudget.v1.entities.Account();
						//System.out.println("Id's matched!");
						System.out.println(id + " == " + getResA.getAccountId());
						acctObj.setAccountId(getResA.getAccountId());
						acctObj.setAvailableBalance(getResA.getBalances().getAvailable() != null ? getResA.getBalances().getAvailable() : 0.00);
						acctObj.setCurrentBalance(getResA.getBalances().getCurrent() != null ? getResA.getBalances().getCurrent() : 0.00);
						acctObj.setLimit(getResA.getBalances().getLimit() != null ? getResA.getBalances().getLimit() : 0.00);
						acctObj.setCurrencyCode(getResA.getBalances().getIsoCurrencyCode() != null ? getResA.getBalances().getIsoCurrencyCode() : "Unknown Currency");
						acctObj.setMask(getResA.getMask());
						acctObj.setNameOfAccount(getResA.getName());
						acctObj.setOfficialName(getResA.getOfficialName() != null ? getResA.getOfficialName() : "");
						acctObj.setType(getResA.getType());
						acctObj.setSubType(getResA.getSubtype());
						System.out.println("Adding acctObj to accountsList...");
						accountsRequestedList.add(acctObj);
					} 
				} // end for each getResA
			} // end for each requested id
		} // end if acctsGetRes.isSuccessful()
		else {
			return "FAIL: " + acctsGetRes.errorBody().toString();
		}
		
		// Print each account in full
		AtomicInteger an = new AtomicInteger(1);
		System.out.println("miBudget accounts, accounts requested after parsing...");
		accountsRequestedList.forEach(account -> {
			System.out.println(an.getAndAdd(1) + ") " + account.toString());
		});
		
		// Check to see if user is attempting to re-add a bank or
		// re-adding accounts
		User user = (User) session.getAttribute("user");
		ArrayList<String> currentAccountIdsList = new ArrayList<>();
		currentAccountIdsList.addAll(user.getAccountIds());
		ArrayList<com.miBudget.v1.entities.Account> usersCurrentAccountsList = (ArrayList<com.miBudget.v1.entities.Account>) accountDAOImpl.getAllAccounts(user.getAccountIds());
		boolean duplicateBank = false;
		boolean duplicateAcct = false;
		ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
		for(String id : institutionIdsList) {
			if (id.equals(institutionId)) {
				System.out.println(institutionId + " has already been added. We cannot add it again. Checking"
						+ "\nto see if the user is adding accounts back.");
				duplicateBank = true;
				break;
			} else
				System.out.println(id + " does not match with " + institutionId);
		}
		int before = accountsRequestedList.size();
		System.out.println("accountsRequestedList size before possible removal is " + before);
		ArrayList<com.miBudget.v1.entities.Account> acctsToRemove = new ArrayList<>();
		req:
		for(com.miBudget.v1.entities.Account reqA : accountsRequestedList) {
			for (com.miBudget.v1.entities.Account addedA : usersCurrentAccountsList) {
				if (reqA.getMask().equals(addedA.getMask()) ) {
					String name = reqA.getNameOfAccount();
					System.out.println(name + " has already been added. We cannot add it again. ");
					duplicateAcct = true;
					acctsToRemove.add(reqA);
					continue req;
				}
			}
		}
		for(com.miBudget.v1.entities.Account a : acctsToRemove) {
			accountsRequestedList.remove(a);
			accountIdsRequestedList.remove(a.getAccountId());
		}
		int after = accountsRequestedList.size();
		System.out.println("accountsRequestedList size is now " + after);
		System.out.println("accountIdsRequestedList size: " + accountIdsRequestedList.size());

		if (duplicateAcct) {
			System.out.println("You requested " + before + " but we can only add " + after + ".");
			// Can continue with authenticate
		} else if (duplicateBank) {
			System.out.println("Trying to add duplicate bank...");
			int numberOfAccounts = accountDAOImpl.getAccountIdsFromUser(user).size();
			request.getSession(false).setAttribute("NoOfAccts", numberOfAccounts);
			response.setStatus(HttpServletResponse.SC_CONFLICT); // TODO: Implement as some 2xx. 4xx is for invalid requests. We don't have that, we just restrict the logic. 
			response.setContentType("application/text");
			response.getWriter().append(institutionId + " has already been added. We cannot add it again.");
			System.out.println("NOT ALLOWED: " + institutionId + " has already been added. We cannot add it again.");
			return "FAIL: Cannot add bank or duplicate accounts.";
		} else if (duplicateBank && duplicateAcct) {
			System.out.println("Trying to add duplicate account(s)...");
			int numberOfAccounts = accountDAOImpl.getAccountIdsFromUser(user).size();
			request.getSession(false).setAttribute("NoOfAccts", numberOfAccounts);
			response.setStatus(HttpServletResponse.SC_CONFLICT); // TODO: Implement as some 2xx. 4xx is for invalid requests. We don't have that, we just restrict the logic. 
			response.setContentType("application/text");
			response.getWriter().append(institutionId + " has already been added. We cannot add it again.");
			System.out.println("NOT ALLOWED: " + institutionId + " has already been added. We cannot add it again.");
			return "FAIL: Cannot add bank or duplicate accounts.";
		}
		// If both the call to exchange the public token for an access token AND
		// If the call to get accounts information is Successful
		if (publicTokenExchangeResponse.isSuccessful() && acctsGetRes.isSuccessful()) {
			System.out.println("Responses code:\n\tPublic Token Exchange: " + publicTokenExchangeResponse.code() + "\n\tAccountsGet: " + acctsGetRes.code());
			try {
			    if (user.getFirstName().equals(null)) {}
		    } catch (NullPointerException e) {
			    return "FAIL: No user!";
		    }
			
			// Create bank object
		    Item bankToAdd = new Item(itemId, accessToken, institutionId);
		    System.out.println("created bankToAdd: " + bankToAdd);
		    
		    int verify = 0;
		    if (!duplicateBank) {
		    	
		    	/** TODO: change attribute name from CreatedItem a list of items.
		    	 * Eventually I will need to create a list of items and add that list.
		    	*/ 
		    	session.setAttribute("CreatedItem", bankToAdd);
			  
			    // Add bank to items table  
			    System.out.println("Adding item to database");
			    verify = addItemToDatabase(bankToAdd);
			    if (verify == 0)
				    return "FAIL: did not add " + bankToAdd + " to database.";
			    else {
				    System.out.println(bankToAdd + " was added to the database.");
				    bankToAdd.setItemTableId(itemDAOImpl.getItemTableIdForItemId(bankToAdd.getItemId())); // until this point, itemTableId is not set
				    System.out.println("updated bankToAdd: " + bankToAdd);
			    }
			    
			    // Add created Item to users_items table
			    verify = addItemToUsersItemsInDatabase(getAllUsersItems(user), bankToAdd, user);
			    if (verify == 0)
				    return "FAIL: did not add UsersItemsObject to UsersItems in database.";
			    else
				    System.out.println("UsersItemsObject was added to UsersItems table in database");
			    
			    // Add institutionId to users_institution_ids table
			    verify = addInstitutionIdToUsersInstitutionIdsTableInDatabase(institutionId, user);
			    if (verify == 0)
				    return "FAIL: did not add " + bankToAdd.getInstitutionId() + " to UsersInstitutions table.";
			    else {
				    System.out.println(bankToAdd.getInstitutionId() + " added to UsersInstitutions table in database");
			    }
		    } else {
		    	bankToAdd.setItemTableId(itemDAOImpl.getItemTableIdUsingInsId(bankToAdd.getInstitutionId())); // until this point, itemTableId is not set
			    System.out.println("updated bankToAdd: " + bankToAdd); // since bank already exists, get itemTableId and set it for this bankToAdd object
		    }
//		    bankToAdd = itemDAOImpl.getItemFromUser(institutionId);
		    // Add accounts to users profile
//		    int itemTableId = itemDAOImpl.getItemTableIdForItemId(bankToAdd.getItemId());
		    accountsRequestedList.forEach(account -> {
			    account.setItemTableId(bankToAdd.getItemTableId());
		    });
		    // Need the above lines to set item_table_id for the accounts. they all have the same item
		    // But need to set to proper item_table_id that was set.
		    verify = addAccountsToAccountsTableDatabase(accountsRequestedList, bankToAdd, user);
		    if (verify == 0)
			    return "FAIL: did not add accounts to user's profile.";
		    else
			    System.out.println("Accounts added to user's profile.");
		    
		    
		    // Add accountsList to requestSession
		    user.setAccountIds( (ArrayList<String>)accountDAOImpl.getAllAccountsIds(user)); // update accountIds
		    usersCurrentAccountsList = (ArrayList<com.miBudget.v1.entities.Account>) accountDAOImpl.getAllAccounts(user.getAccountIds());
		    usersCurrentAccountsList.forEach(account -> {
			    System.out.println(account);
		    });
		    System.out.println();
		    
		    // Create a Map of itemIds, and list of appropriate accounts
		    @SuppressWarnings("unchecked")
		    HashMap<String, ArrayList<com.miBudget.v1.entities.Account>> acctsAndInstitutionIdMap = 
		  	    (HashMap<String, ArrayList<com.miBudget.v1.entities.Account>>) session.getAttribute("acctsAndInstitutionIdMap");
		    ArrayList<com.miBudget.v1.entities.Account> newAccountsList = new ArrayList<>();
		    newAccountsList = acctsAndInstitutionIdMap.get(institutionId);
		    
		    System.out.println("acctsAndInstitutionIdMap");
		    for(String insId : acctsAndInstitutionIdMap.keySet()) {
			    System.out.println("key: " + insId);
			    for (com.miBudget.v1.entities.Account acct : acctsAndInstitutionIdMap.get(insId)) {
				    System.out.println("\t" + acct);
			    }
			    System.out.println();
		    }
		    System.out.println("Adding " + institutionId + " and the following accounts to acctsAndInstitutionIdMap");
		    for (com.miBudget.v1.entities.Account account : accountsRequestedList) {
			    System.out.println("account: " + account);
			    newAccountsList.add(account);
		    }
		    
		    Object resultOfPutInMap = acctsAndInstitutionIdMap.put(institutionId, newAccountsList);
		    System.out.println("resultOfPutInMap: " + resultOfPutInMap);
		    //if (resultOfPutInMap != null) System.out.println("Some item and its accounts were just overwritten!!");
		    //else System.out.println("Added the Integer and Accounts pairing to the acctsAndInsitutionIdMap");
		    session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
		  
		    institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
		    session.setAttribute("institutionIdsList", institutionIdsList);
		    session.setAttribute("institutionIdsListSize", institutionIdsList.size());
		  
		    // Accounts list is all accounts in users profile
		    session.setAttribute("accountsSize", usersCurrentAccountsList.size());
		  
		    user.setAccountIds(accountIdsRequestedList);
            session.setAttribute("user", user);
		    session.setAttribute("listOfAccountIds", accountIdsRequestedList);
		    session.setAttribute("listOfAccount", usersCurrentAccountsList);
		    String strAccounts = (accountsRequestedList.size() == 1) ? " account!" : " accounts!";
		    session.setAttribute("change", "You have successfully loaded " + accountsRequestedList.size() + strAccounts);
		    
		    institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
		    ArrayList<Item> items = new ArrayList<>();
		    for (String id : institutionIdsList) {
		    	Item item = itemDAOImpl.getItemFromUser(id);
			    System.out.println(item);
			    items.add(item);
		    }
		    
		    // Populate ErrMapForItems
		    HashMap<String, Boolean> errMapForItems = new HashMap<>();
		    for (Item item : items) {
		    	ItemGetRequest getReq = new ItemGetRequest(item.getAccessToken());
			    Response<ItemGetResponse> getRes = client().service().itemGet(getReq).execute();
			    if (getRes.isSuccessful()) {
				    ItemStatus itemStatus = getRes.body().getItem();
				    ErrorResponse err = itemStatus.getError();
				    if (err != null) {
					    if (err.getErrorType() == ErrorType.ITEM_ERROR) {
						    System.out.println("There is an Item_Error");
						    System.out.println(err.toString());
						    errMapForItems.put(item.getInstitutionId(), true);
					    } 
				    } else {
					    System.out.println("No error for this " + item);
					    errMapForItems.put(item.getInstitutionId(), false);
				    }
			    } else {
				    System.out.println("ItemGetResponse failed.");
				    System.out.println(getRes.errorBody());
			    }
		    }
		    session.setAttribute("ErrMapForItems", errMapForItems);
	    } // end if publicTokenExchange and acctsGet is successful
		return "SUCCESS";
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("\nInside Authenticate servlet doPost.");		
		String authResponse = authenticate(request, response);
		System.out.println("Authenticate response: " + authResponse);
		
		if (authResponse.equals("SUCCESS")) {
			// add session back to response obj
			response.setContentType("application/html");
			response.setStatus(HttpServletResponse.SC_OK);
//			getServletContext().getRequestDispatcher("/Profile.jsp").forward(request, response);
			//response.sendRedirect("Profile.jsp");
			System.out.println("--- END ---");
			return;
		} else {
			System.out.println(authResponse);
			response.setStatus(HttpServletResponse.SC_CONFLICT); // TODO: Implement as some 2xx. 4xx is for invalid requests. We don't have that, we just restrict the logic. 
			response.setContentType("application/text");
			System.out.println("--- END ---");
			return;
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside the Profile doGet() servlet.");
		System.out.println("--- END ---");
	}

}
