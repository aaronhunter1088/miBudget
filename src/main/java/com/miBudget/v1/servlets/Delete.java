package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.request.ItemRemoveRequest;
import com.plaid.client.response.ErrorResponse;
import com.plaid.client.response.ErrorResponse.ErrorType;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemRemoveResponse;
import com.plaid.client.response.ItemStatus;

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
    public final PlaidClient client() {
		// Use builder to create a client
		PlaidClient client = PlaidClient.newBuilder()
				.clientIdAndSecret(clientId, secret)
				.publicKey("") // optional. only needed to call endpoints that require a public key
				.sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
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
			Response<ItemRemoveResponse> itemRemoveRes =  client().service()
					.itemRemove(new ItemRemoveRequest(item.getAccessToken()))
					.execute();
					// The Item has been removed and the access token is now invalid
			boolean isRemoved = false;
			if (itemRemoveRes.isSuccessful()) {
				isRemoved = itemRemoveRes.body().getRemoved();
			} else {
				System.out.println(itemRemoveRes.errorBody().string());
				return "FAIL: item's access token was not invalidated. Request failed.";
			}
			System.out.println(item.getAccessToken() + " was invalidated?: " + isRemoved);
			if (isRemoved == true) {
				//ArrayList<UsersItemsObject> usersItemsList = itemDAOImpl.getAllUserItems((User)session.getAttribute("user"));
				@SuppressWarnings("unchecked")
				HashMap<Integer, ArrayList<Account>> acctsAndInstitutionIdMap = (HashMap<Integer, ArrayList<Account>>) 
						session.getAttribute("acctsAndInstitutionIdMap");
				acctsAndInstitutionIdMap.remove(item.getItemTableId());
				// update session values
				int numberOfAccounts = accountDAOImpl.getAccountIdsFromUser(user).size();
				ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
				session.setAttribute("institutionIdsList", institutionIdsList);
				session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
				session.setAttribute("institutionIdsListSize", institutionIdsList.size());
				session.setAttribute("accountsSize", numberOfAccounts);
			} else {
				return "FAIL: access token was not invalidated for " + item.getInsitutionId();
			}
		}
		
		
		// Need to call /item/delete to invalidate access-token for Item
		return "deleteBank: SUCCESS";
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Inside the Delete doPost method");
		System.out.println("currentId: " + request.getParameter("idCopy"));
		System.out.println("deleting request: " + request.getParameter("delete"));
		HttpSession session = request.getSession(false);
		// Perform the following logic:
		if (request.getParameter("delete").equals("bank")) {
			String deleteResponse = deleteBank(request, response);
			System.out.println("deleteResponse: " + deleteResponse);
			HashMap<String, Boolean> errMapForItems = new HashMap<>();
			ArrayList<String> ids = (ArrayList<String>) session.getAttribute("institutionIdsList");
			ArrayList<Item> items = new ArrayList<>();
			for(int i = 0; i < ids.size(); i++) {
				Item item = itemDAOImpl.getItemFromUser(ids.get(i));
				System.out.println(item);
				items.add(item);
			}
			if (deleteResponse.contains("SUCCESS")) {
				
					
				for(int i = 0; i < items.size(); i++) {
					ItemGetRequest getReq = new ItemGetRequest(items.get(i).getAccessToken());
					Response<ItemGetResponse> getRes = client().service().itemGet(getReq).execute();
					if (getRes.isSuccessful()) {
						ItemStatus itemStatus = getRes.body().getItem();
						ErrorResponse err = itemStatus.getError();
						if (err != null) {
							if (err.getErrorType() == ErrorType.ITEM_ERROR) {
								System.out.print("There is an Item_Error: ");
								System.out.println(err.toString());
								errMapForItems.put(items.get(i).getInsitutionId(), true);
							} 
						} else {
							System.out.println("No error for this item: " + items.get(i).toString());
							errMapForItems.put(items.get(i).getInsitutionId(), false);
						}
					} else {
						System.out.println("ItemGetResponse failed.");
						System.out.println(getRes.errorBody());
					}
				}
				session.setAttribute("change", "You have successfully deleted your bank.");
				session.setAttribute("ErrMapForItems", errMapForItems);
				response.setContentType("application/html");
				response.setStatus(HttpServletResponse.SC_OK);
				response.sendRedirect("Profile.jsp");
			} else {
				session.setAttribute("change", "There was an issue deleting your bank: " + deleteResponse);
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
