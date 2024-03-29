//package com.miBudget.servlets;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
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
//import com.miBudget.core.Constants;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.HibernateException;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import com.miBudget.entities.Item;
//import com.miBudget.entities.User;
//import com.miBudget.entities.UserItemsObject;
//import com.plaid.client.PlaidClient;
//import com.plaid.client.request.AccountsGetRequest;
//import com.plaid.client.request.ItemGetRequest;
//import com.plaid.client.request.ItemPublicTokenExchangeRequest;
//import com.plaid.client.response.Account;
//import com.plaid.client.response.AccountsGetResponse;
//import com.plaid.client.response.ErrorResponse;
//import com.plaid.client.response.ErrorResponse.ErrorType;
//import com.plaid.client.response.ItemGetResponse;
//import com.plaid.client.response.ItemPublicTokenExchangeResponse;
//import com.plaid.client.response.ItemStatus;
//
//import retrofit2.Response;
//
//
///**
// * Servlet implementation class Authenticate
// */
//@WebServlet("/authenticate") // rename to /get_access_token. this is what the example shows
//public class Authenticate extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	private final String client_id = "5ae66fb478f5440010e414ae";
//	//private final String secret = "0e580ef72b47a2e4a7723e8abc7df5";
//	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
//
//	private static Logger LOGGER = LogManager.getLogger(Authenticate.class);
//
//	public PlaidClient client() {
//		// Use builder to create a client
//		PlaidClient client = PlaidClient.newBuilder()
//				  .clientIdAndSecret(client_id, secretD)
//				  .publicKey("") // optional. only needed to call endpoints that require a public key
//				  .developmentBaseUrl() // or equivalent, depending on which environment you're calling into
//				  .build();
//		return client;
//	}
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public Authenticate() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//    /*
//     * TODO: Update items_account table
//     * Create an items_account object
//     */
//
////    public int addAccountsToAccountsTableDatabase(List<com.miBudget.entities.Account> accountsRequested, Item item, User user) {
//////		Iterator<com.miBudget.entities.Account> iter = accounts.iterator();
////    	int verify = 0;
////		for (com.miBudget.entities.Account account : accountsRequested) {
//////    		com.miBudget.entities.Account account = iter.next();
////    		// update account availableBalance, currentBalance, limit, currencyCode, item, figure out how to unmask mask to reveal last 4 of account
////			verify = MiBudgetState.getAccountDAOImpl().addAccountObjectToAccountsTableDatabase(account);
////			verify = MiBudgetState.getAccountDAOImpl().addAccountObjectToUsersAccountsTable(account, user);
////    		verify = MiBudgetState.getAccountDAOImpl().addAccountIdToItemsAccountsTable(item.getId(), account.getAccountId());
////
////		}
////    	if (verify == 0)
////    		return 0;
////    	return 1;
////    }
//
//    /**
//     * This method takes the institution_id and saves it to the
//     * database with the appropriate user.
//     * @param ins_id
//     * @param user
//     * @return
//     */
//    public int addInstitutionIdToUsersInstitutionIdsTableInDatabase(String ins_id, User user) {
//    	int verify = MiBudgetState.getMiBudgetDAOImpl().addInstitutionIdToDatabase(ins_id, user);
//    	return verify;
//    }
//
//    public int addItemToUsersItemsInDatabase(ArrayList<UserItemsObject> usersItemsList, Item item, User user) {
//    	try {
////    		itemTableId = itemDAOImpl.getItemTableIdForItemId(item.getItemId());
//    		long itemTableId = item.getId();
//
//    		boolean brokenLoop = false;
//    		int verify = 0;
//    		if (usersItemsList.size() == 0) {
//    			verify = MiBudgetState.getMiBudgetDAOImpl().addItemToUsersItemsTable(itemTableId, user);
//    		} else {
//    			for (UserItemsObject uiObj : usersItemsList) {
//        			if (uiObj.getItem__id() == item.getId()) {
//        				// we cannot add a bank again to a specific users list of items
//        				brokenLoop = true;
//        				break;
//        			}
//        		}
//    			if (!brokenLoop)
//    				verify = MiBudgetState.getMiBudgetDAOImpl().addItemToUsersItemsTable(itemTableId, user);
//    		}
//
//    		if (verify == 1)
//    			return 1;
//    		if (verify == 0)
//    			return 0;
//    		return 1; // good
//    	} catch (HibernateException e) {
//    		LOGGER.error("\nFailed to start SQL Session\n");
//    		LOGGER.error(e.getMessage());
//    		LOGGER.error(e.getStackTrace());
//    	} catch (IllegalStateException e) {
//    		LOGGER.error("\nSession/Entity Manager is closed.\n");
//    		LOGGER.error(e.getMessage());
//    		LOGGER.error(e.getStackTrace());
//    	}
//    	return 0; // 0 == bad
//    }
//
//    public List<String> getAllItemIds() {
//    	List<String> allItemIdsList = new ArrayList<>();
//		allItemIdsList = MiBudgetState.getItemDAOImpl().getAllItemIds();
//		return allItemIdsList;
//    }
//
//    /**
//     * This method returns ALL the items added to the database.
//     * @return
//     */
//    public ArrayList<Item> getAllItems() {
//    	ArrayList<Item> allItemsList = new ArrayList<>();
//    	allItemsList = MiBudgetState.getItemDAOImpl().getAllItems();
//    	return allItemsList;
//    }
//
//    /**
//     * This method returns ALL the items for a specific user
//     * @return
//     */
//    public ArrayList<UserItemsObject> getAllUsersItems(User user) {
//    	ArrayList<UserItemsObject> usersItemsList = new ArrayList<>();
//    	usersItemsList = MiBudgetState.getItemDAOImpl().getAllUserItems(user);
//    	return usersItemsList;
//    }
//
////    public int addItemToDatabase(Item item) {
////    	try {
////
////    		// This logic does not make sense anymore.
//////    		for (Item i : allItems) {
//////        		if (i.getItemTableId() == item.getItemTableId()) {
//////        			LOGGER.info("next item: " + i);
//////        			LOGGER.info("Item already added to database!");
//////        			return 0; // bad
//////        		}
//////        	}
////    		LOGGER.info("before save...");
////        	int verify = MiBudgetState.getItemDAOImpl().addItemToDatabase(item);
////        	if (verify == 0)
////        		return 0;
////        	return 1;
////    	} catch (HibernateException e) {
////    		LOGGER.error("\nFailed to start SQL Session\n");
////    		e.printStackTrace(System.out);
////    	} catch (IllegalStateException e) {
////    		LOGGER.error("\nSession/Entity Manager is closed.\n");
////    		e.printStackTrace(System.out);
////    	}
////    	return 0; // 0 == bad
////
////    	// for every Item in the list, if our item we are attempting to add
////    	// is the same, do not add it.
////    }
//
//    /**
//     * The reason this method returns a String is because we ultimately
//     * want authenticate to be successful. So if the response is good,
//     * and we were able to do all the processing on the response that
//     * we needed to do, we return the String "SUCCESS". If somewhere
//     * it fails to do the logic, we return the String "FAIL" along
//     * with a message describing where we failed.
//     * @param request
//     * @param response
//     * @return
//     * @throws ServletException
//     * @throws IOException
//     */
//	private String authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException
//	{
//		LOGGER.info("Inside authenticate()...");
//		HttpSession session = request.getSession(false);
//		if (session.getId() != session.getAttribute("sessionId")) {
//			LOGGER.info("not the same session");
//			session = request.getSession();
//			return "FAIL: this is not the same session...";
//		} else {
//			LOGGER.info("valid active session");
//		}
//
//		// Public token exchange request and response
//		String public_token = request.getParameter("public_token");
//		LOGGER.info("public_token: " + public_token);
//		ItemPublicTokenExchangeRequest publicTokenExchangeRequest = new ItemPublicTokenExchangeRequest(public_token);
//		Response<ItemPublicTokenExchangeResponse> publicTokenExchangeResponse =
//				client().service().itemPublicTokenExchange(publicTokenExchangeRequest).execute();
//		/**LOGGER.info("publicTokenExchangeResponseBody: " + publicTokenExchangeResponse.body().toString());
//		// access token notes: An access_token is used to access product data for an Item
//		LOGGER.info("publicTokenExchangeResponseAccessToken: " + publicTokenExchangeResponse.body().getAccessToken());
//		LOGGER.info("accessTokenLength: " + publicTokenExchangeResponse.body().getAccessToken().length());
//		// item_id notes: An item_id is used to identify an Item in a webhook.
//		LOGGER.info("publicTokenExchangeResponseItemId: " + publicTokenExchangeResponse.body().getItemId());
//		LOGGER.info("itemIdLength: " + publicTokenExchangeResponse.body().getItemId().length());
//		// request_id notes: used to keep track of user's session and what they did (i believe)
//		LOGGER.info("publicTokenExchangeResponseRequestId: " + publicTokenExchangeResponse.body().getRequestId()); */
//
//		// Get itemId and accessToken
//		String accessToken = "";
//		String itemId = "";
//		if (publicTokenExchangeResponse.isSuccessful()) {
//			accessToken = publicTokenExchangeResponse.body().getAccessToken();
//			itemId = publicTokenExchangeResponse.body().getItemId();
//		}
//
//		// META DATA - Accounts the user selected
//		String accountsRequested = request.getParameter("accounts");
//		String institutionId = request.getParameter("institution_id");
//		String institutionName = request.getParameter("institution_name");
//		String linkSessionId = request.getParameter("link_session_id");
//
//		LOGGER.info("accountsRequested: " + accountsRequested);
//		LOGGER.info("institutionId: " + institutionId);
//		LOGGER.info("instutionName: " + institutionName);
//		LOGGER.info("linkSessionId: " + linkSessionId);
//
//		// Parse accountsRequested; store in accountIdsRequestedList
//		JSONParser jsonParser = new JSONParser();
//		JSONArray accountsRequestedJsonArray = null;
//		try { accountsRequestedJsonArray = (JSONArray) jsonParser.parse(accountsRequested); }
//		catch (Exception e) { e.printStackTrace(System.out); }
//		LOGGER.info("number of accounts requested: " + accountsRequestedJsonArray.size());
//		ArrayList<com.miBudget.entities.Account> accountsRequestedList = new ArrayList<>();
//		ArrayList<String> accountIdsRequestedList = new ArrayList<>();
//		JSONObject jsonObject = null;
//		for(int i = 0; i < accountsRequestedJsonArray.size(); i++) {
//			try {
//				jsonObject = (JSONObject) jsonParser.parse(accountsRequestedJsonArray.get(i).toString());
//				accountIdsRequestedList.add(jsonObject.get("id").toString());
//			} catch (org.json.simple.parser.ParseException e) {
//				e.printStackTrace(System.out);
//			}
//		}
//		accountIdsRequestedList.forEach(id -> {
//			LOGGER.info(id);
//		});
//
//		// Make call to AccountsGetResponse to receive additional information about the requested accounts
//		AccountsGetRequest acctsGetReq = new AccountsGetRequest(accessToken);
//		acctsGetReq.clientId = client_id;
//		acctsGetReq.secret = secretD;
//		acctsGetReq.withAccountIds(accountIdsRequestedList);
//		Response<AccountsGetResponse> acctsGetRes =
//				client().service().accountsGet(acctsGetReq).execute(); //.withAccountIds(accountIdsList)
//		LOGGER.info("acctsGetRes body: " + acctsGetRes.body().toString());
//		LOGGER.info("acctsGetRes code: " + acctsGetRes.code());
//		LOGGER.info("acctsGetRes msg: " + acctsGetRes.message());
//		LOGGER.info("acctsGetRes raw : " + acctsGetRes.raw());
//		LOGGER.info("acctsGetRes err: " + acctsGetRes.errorBody());
//		if (acctsGetRes.message().equals("Bad Request") || acctsGetRes.body().getAccounts().size() != accountsRequestedJsonArray.size() ) {
//			LOGGER.info("acctsGetRes errString: " + acctsGetRes.errorBody().string());
//			System.out.printf("/accounts/get endpoint retrieved %d accounts\n", acctsGetRes.body().getAccounts().size());
//			acctsGetRes.body().getAccounts().forEach(acct -> {
//				LOGGER.info(acct);
//			});
//			return "FAIL: " + acctsGetRes.errorBody().toString();
//		}
//		if (acctsGetRes.isSuccessful()) {
//			LOGGER.info("AccountsGetResponse : " + acctsGetRes.code());
//			List<Account> accountsFromRes = acctsGetRes.body().getAccounts();
//			accountsFromRes.forEach(acct -> {
//				LOGGER.info("acctsGetRes account: " + ((Account)acct));
//			});
//
//			for (String id : accountIdsRequestedList) {
//				for (Account getResA : accountsFromRes) {
//					if (id.equals(getResA.getAccountId()) ) {
//						com.miBudget.entities.Account acctObj = new com.miBudget.entities.Account();
//						//LOGGER.info("Id's matched!");
//						LOGGER.info(id + " == " + getResA.getAccountId());
//						acctObj.setAccountId(getResA.getAccountId());
//						acctObj.setAvailableBalance(getResA.getBalances().getAvailable() != null ? getResA.getBalances().getAvailable() : 0.00);
//						acctObj.setCurrentBalance(getResA.getBalances().getCurrent() != null ? getResA.getBalances().getCurrent() : 0.00);
//						acctObj.set_limit(getResA.getBalances().getLimit() != null ? getResA.getBalances().getLimit() : 0.00);
//						acctObj.setCurrencyCode(getResA.getBalances().getIsoCurrencyCode() != null ? getResA.getBalances().getIsoCurrencyCode() : "Unknown Currency");
//						acctObj.setMask(getResA.getMask());
//						acctObj.setAccountName(getResA.getName());
//						acctObj.setOfficialName(getResA.getOfficialName() != null ? getResA.getOfficialName() : "");
//						acctObj.set_type(getResA.getType());
//						acctObj.setSubType(getResA.getSubtype());
//						LOGGER.info("Adding acctObj to accountsList...");
//						accountsRequestedList.add(acctObj);
//					}
//				} // end for each getResA
//			} // end for each requested id
//		} // end if acctsGetRes.isSuccessful()
//		else {
//			return "FAIL: " + acctsGetRes.errorBody().toString();
//		}
//
//		// Print each account in full
//		AtomicInteger an = new AtomicInteger(1);
//		LOGGER.info("miBudget accounts, accounts requested after parsing...");
//		accountsRequestedList.forEach(account -> {
//			LOGGER.info(an.getAndAdd(1) + ") " + account.toString());
//		});
//
//		// Check to see if user is attempting to re-add a bank or
//		// re-adding accounts
//		User user = (User) session.getAttribute("user");
//		ArrayList<String> currentAccountIdsList = new ArrayList<>();
//		currentAccountIdsList.addAll(user.getAccountIds());
//		ArrayList<com.miBudget.entities.Account> usersCurrentAccountsList = (ArrayList<com.miBudget.entities.Account>) MiBudgetState.getAccountDAOImpl().getAllAccounts(user.getAccountIds());
//		boolean duplicateBank = false;
//		boolean duplicateAcct = false;
//		ArrayList<String> institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(user);
//		for(String id : institutionIdsList) {
//			if (id.equals(institutionId)) {
//				LOGGER.info(institutionId + " has already been added. We cannot add it again. Checking "
//											 + "to see if the user is adding accounts back.");
//				duplicateBank = true;
//				break;
//			} else
//				LOGGER.info(id + " does not match with " + institutionId);
//		}
//		int before = accountsRequestedList.size();
//		LOGGER.info("accountsRequestedList size before possible removal is " + before);
//		ArrayList<com.miBudget.entities.Account> acctsToRemove = new ArrayList<>();
//		req:
//		for(com.miBudget.entities.Account reqA : accountsRequestedList) {
//			for (com.miBudget.entities.Account addedA : usersCurrentAccountsList) {
//				if (reqA.getMask().equals(addedA.getMask()) ) {
//					String name = reqA.getAccountName();
//					LOGGER.info(name + " has already been added. We cannot add it again. ");
//					duplicateAcct = true;
//					acctsToRemove.add(reqA);
//					continue req;
//				}
//			}
//		}
//		for(com.miBudget.entities.Account a : acctsToRemove) {
//			accountsRequestedList.remove(a);
//			accountIdsRequestedList.remove(a.getAccountId());
//		}
//		int after = accountsRequestedList.size();
//		LOGGER.info("accountsRequestedList size is now " + after);
//		LOGGER.info("accountIdsRequestedList size: " + accountIdsRequestedList.size());
//
//		if (duplicateAcct && after > 0) {
//			LOGGER.info("You requested " + before + " but we can only add " + after + ".");
//			// Can continue with authenticate
//		} else if (duplicateBank && after == 0) {
//			LOGGER.info("Trying to add duplicate bank...");
//			int numberOfAccounts = MiBudgetState.getAccountDAOImpl().getAccountIdsFromUser(user).size();
//			request.setAttribute("NoOfAccts", numberOfAccounts);
//			response.setStatus(HttpServletResponse.SC_CONFLICT); // TODO: Implement as some 2xx. 4xx is for invalid requests. We don't have that, we just restrict the logic.
//			response.setContentType("application/text");
//			response.getWriter().append(institutionId + " has already been added. We cannot add it again.");
//			LOGGER.info("NOT ALLOWED: " + institutionId + " has already been added. We cannot add it again.");
//			return "FAIL: Cannot add duplicate bank.";
//		} else if (duplicateBank && duplicateAcct) {
//			LOGGER.info("Trying to add duplicate account(s)...");
//			int numberOfAccounts = MiBudgetState.getAccountDAOImpl().getAccountIdsFromUser(user).size();
//			request.setAttribute("NoOfAccts", numberOfAccounts);
//			response.setStatus(HttpServletResponse.SC_CONFLICT); // TODO: Implement as some 2xx. 4xx is for invalid requests. We don't have that, we just restrict the logic.
//			response.setContentType("application/text");
//			response.getWriter().append(institutionId + " has already been added. We cannot add it again.");
//			LOGGER.info("NOT ALLOWED: " + institutionId + " has already been added. We cannot add it again.");
//			return "FAIL: Cannot add bank or duplicate accounts.";
//		}
//		// If both the call to exchange the public token for an access token AND
//		// If the call to get accounts information is Successful
//		if (publicTokenExchangeResponse.isSuccessful() && acctsGetRes.isSuccessful()) {
//			LOGGER.info("Responses code:\n\tPublic Token Exchange: " + publicTokenExchangeResponse.code() + "\n\tAccountsGet: " + acctsGetRes.code());
//			try {
//			    if (user.getFirstName() == null) {}
//		    } catch (NullPointerException e) {
//			    return "FAIL: No user!";
//		    }
//
//			// Create bank object
//		    Item bankToAdd = new Item(itemId, accessToken, institutionId);
//		    LOGGER.info("created bankToAdd: " + bankToAdd);
//
//		    int verify = 0;
//		    if (!duplicateBank) {
//
//		    	/** TODO: change attribute name from CreatedItem a list of items.
//		    	 * Eventually I will need to create a list of items and add that list.
//		    	*/
//		    	session.setAttribute("CreatedItem", bankToAdd);
//
//			    // Add bank to items table
//			    LOGGER.info("Adding item to database");
//			    //verify = addItemToDatabase(bankToAdd);
//			    if (verify == 0)
//				    return "FAIL: did not add " + bankToAdd + " to database.";
//			    else {
//				    LOGGER.info(bankToAdd + " was added to the database.");
//				    bankToAdd.setId(MiBudgetState.getItemDAOImpl().getItemTableIdForItemId(bankToAdd.getItemId())); // until this point, itemTableId is not set
//				    LOGGER.info("updated bankToAdd: " + bankToAdd);
//			    }
//
//			    // Add created Item to users_items table
//			    verify = addItemToUsersItemsInDatabase(getAllUsersItems(user), bankToAdd, user);
//			    if (verify == 0)
//				    return "FAIL: did not add UsersItemsObject to UsersItems in database.";
//			    else
//				    LOGGER.info("UsersItemsObject was added to UsersItems table in database");
//
//			    // Add institutionId to users_institution_ids table
//			    verify = addInstitutionIdToUsersInstitutionIdsTableInDatabase(institutionId, user);
//			    if (verify == 0)
//				    return "FAIL: did not add " + bankToAdd.getInstitutionId() + " to UsersInstitutions table.";
//			    else {
//				    LOGGER.info(bankToAdd.getInstitutionId() + " added to UsersInstitutions table in database");
//			    }
//		    } else {
//		    	bankToAdd.setId(MiBudgetState.getItemDAOImpl().getItemTableIdUsingInsId(bankToAdd.getInstitutionId())); // until this point, itemTableId is not set
//			    LOGGER.info("updated bankToAdd: " + bankToAdd); // since bank already exists, get itemTableId and set it for this bankToAdd object
//		    }
////		    bankToAdd = itemDAOImpl.getItemFromUser(institutionId);
//		    // Add accounts to users profile
////		    int itemTableId = itemDAOImpl.getItemTableIdForItemId(bankToAdd.getItemId());
//		    accountsRequestedList.forEach(account -> {
//			    account.setItemId(bankToAdd.getId());
//		    });
//		    // Need the above lines to set item_table_id for the accounts. they all have the same item
//		    // But need to set to proper item_table_id that was set.
//		    //verify = addAccountsToAccountsTableDatabase(accountsRequestedList, bankToAdd, user);
//		    if (verify == 0)
//			    return "FAIL: did not add accounts to user's profile.";
//		    else
//			    LOGGER.info("Accounts added to user's profile.");
//
//
//		    // Add accountsList to requestSession
//		    user.setAccountIds( (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAllAccountsIds(user)); // update accountIds
//		    usersCurrentAccountsList = (ArrayList<com.miBudget.entities.Account>) MiBudgetState.getAccountDAOImpl().getAllAccounts(user.getAccountIds());
//		    usersCurrentAccountsList.forEach(account -> {
//			    LOGGER.info(account);
//		    });
//
//		    // Create a Map of itemIds, and list of appropriate accounts
//		    @SuppressWarnings("unchecked")
//		    HashMap<String, ArrayList<com.miBudget.entities.Account>> institutionIdsAndAccounts =
//		  	    (HashMap<String, ArrayList<com.miBudget.entities.Account>>) session.getAttribute("institutionIdsAndAccounts");
//		    ArrayList<com.miBudget.entities.Account> newAccountsList = new ArrayList<>();
//		    newAccountsList = institutionIdsAndAccounts.get(institutionId);
//		    if (newAccountsList == null) newAccountsList = new ArrayList<com.miBudget.entities.Account>();
//		    LOGGER.info("institutionIdsAndAccounts");
//		    for(String insId : institutionIdsAndAccounts.keySet()) {
//			    LOGGER.info("key: " + insId);
//			    for (com.miBudget.entities.Account acct : institutionIdsAndAccounts.get(insId)) {
//				    LOGGER.info("\t" + acct);
//			    }
//		    }
//		    accountsRequestedList.forEach(account -> {
//				LOGGER.info(an.getAndAdd(1) + ") " + account.toString());
//			});
//		    LOGGER.info("Adding " + institutionId + " and the following accounts to institutionIdsAndAccounts");
//		    for (com.miBudget.entities.Account account : accountsRequestedList) {
//			    LOGGER.info("account: " + account);
//			    newAccountsList.add(account);
//		    }
//
//		    Object resultOfPutInMap = institutionIdsAndAccounts.put(institutionId, newAccountsList);
//		    LOGGER.info("resultOfPutInMap: " + resultOfPutInMap);
//		    //if (resultOfPutInMap != null) LOGGER.info("Some item and its accounts were just overwritten!!");
//		    //else LOGGER.info("Added the Integer and Accounts pairing to the acctsAndInsitutionIdMap");
//		    session.setAttribute("institutionIdsAndAccounts", institutionIdsAndAccounts);
//
//		    institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(user);
//		    session.setAttribute("institutionIdsList", institutionIdsList);
//		    session.setAttribute("institutionIdsListSize", institutionIdsList.size());
//
//		    // Accounts list is all accounts in users profile
//		    session.setAttribute("accountsSize", usersCurrentAccountsList.size());
//
//		    user.setAccountIds(accountIdsRequestedList);
//            session.setAttribute("user", user);
//		    session.setAttribute("listOfAccountIds", accountIdsRequestedList);
//		    session.setAttribute("listOfAccount", usersCurrentAccountsList);
//		    String strAccounts = (accountsRequestedList.size() == 1) ? " account!" : " accounts!";
//		    session.setAttribute("change", "You have successfully loaded " + accountsRequestedList.size() + strAccounts);
//
//		    institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(user);
//		    ArrayList<Item> items = new ArrayList<>();
//		    for (String id : institutionIdsList) {
//		    	Item item = MiBudgetState.getItemDAOImpl().getItemFromUser(id);
//			    LOGGER.info(item);
//			    items.add(item);
//		    }
//
//		    // Populate ErrMapForItems
//		    HashMap<String, Boolean> errMapForItems = new HashMap<>();
//		    for (Item item : items) {
//		    	ItemGetRequest getReq = new ItemGetRequest(item.getAccessToken());
//			    Response<ItemGetResponse> getRes = client().service().itemGet(getReq).execute();
//			    if (getRes.isSuccessful()) {
//				    ItemStatus itemStatus = getRes.body().getItem();
//				    ErrorResponse err = itemStatus.getError();
//				    if (err != null) {
//					    if (err.getErrorType() == ErrorType.ITEM_ERROR) {
//						    LOGGER.info("There is an Item_Error");
//						    LOGGER.info(err.toString());
//						    errMapForItems.put(item.getInstitutionId(), true);
//					    }
//				    } else {
//					    LOGGER.info("No error for this " + item);
//					    errMapForItems.put(item.getInstitutionId(), false);
//				    }
//			    } else {
//				    LOGGER.info("ItemGetResponse failed.");
//				    LOGGER.info(getRes.errorBody());
//			    }
//		    }
//		    session.setAttribute("ErrMapForItems", errMapForItems);
//	    } // end if publicTokenExchange and acctsGet is successful
//		return "SUCCESS";
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info(Constants.start);
//		LOGGER.info("Inside the Authenticate doPost() servlet.");
//		String authResponse = authenticate(request, response);
//		LOGGER.info("authenticate response: " + authResponse);
//
//		if (authResponse.equals("SUCCESS")) {
//			// add session back to response obj
//			response.setContentType("application/html");
//			response.setStatus(HttpServletResponse.SC_OK);
//		}
//		else {
//			LOGGER.warn(authResponse);
//			response.setStatus(HttpServletResponse.SC_CONFLICT); // TODO: Implement as some 2xx. 4xx is for invalid requests. We don't have that, we just restrict the logic.
//		}
//		LOGGER.info(Constants.end);
//		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/Accounts.jsp");
//		dispatcher.forward( request, response );
//	}
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info("--- START ---");
//		LOGGER.info("Inside the Authenticate doGet() servlet.");
//		LOGGER.info("--- END ---");
//	}
//
//}
