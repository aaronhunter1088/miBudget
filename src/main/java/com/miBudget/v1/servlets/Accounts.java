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
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.User;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.response.ErrorResponse;
import com.plaid.client.response.ErrorResponse.ErrorType;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemStatus;

import retrofit2.Response;

/**
 * Servlet implementation class Profile
 */
@SuppressWarnings("unused")
@WebServlet("/Accounts")
public class Accounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	
	private final String clientId = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside Accounts doGet() servlet");
		HttpSession requestSession = request.getSession(false);
		//System.out.println("request: " + !request.getSession(false));
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			
			User user = (User) request.getAttribute("user");
			HashMap<String, Boolean> errMapForItems = new HashMap<>();
			ArrayList<String> ids = (ArrayList<String>) requestSession.getAttribute("institutionIdsList");
			ArrayList<Item> items = new ArrayList<>();
			for (String id : ids) {
				Item item = itemDAOImpl.getItemFromUser(id);
				System.out.println("Retreived " + item);
				items.add(item);
			}
			String errMsg = "";
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
							errMsg = err.getErrorMessage();
							errMsg = errMsg.substring(0, errMsg.indexOf("(")-1) + errMsg.substring(errMsg.indexOf(")")+1, errMsg.length());
							errMsg = errMsg.substring(0, errMsg.indexOf(".")+2) + "Click the update button to restore any banks in a bad state.";
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
			if (errMsg.equals("") == true) requestSession.setAttribute("change", "This text will change after using the Plaid Link Initializer.");
			else requestSession.setAttribute("change", errMsg);
			requestSession.setAttribute("ErrMapForItems", errMapForItems);
			System.out.println("Loading the Profile.jsp page...");
			response.sendRedirect("Accounts.jsp");
		} else {
			System.out.println("requestSession: " + requestSession );
			System.out.println("isUserLoggedIn: " + requestSession.getAttribute("isUserLoggedIn") );
		}
		
		//doPost(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside the Accounts doPost() servlet.");
		HttpSession requestSession = request.getSession(false);  
        if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true){  
        	System.out.println("stored requestSessionId: " + requestSession.getAttribute("requestSessionId"));
        	requestSession.setAttribute("acctsAndInstitutionIdMap", new HashMap<String, List<com.miBudget.v1.entities.Account>>() );
        	//RequestDispatcher view = request.getRequestDispatcher("/Profile.html");
        	//view.forward(request, response); // doesn't take the session
        	response.sendRedirect("Accounts.jsp");
        	
        }  
        else {   
        	System.out.println("User is not logged in");
            response.sendRedirect("Login.html");
        }  
	}
	
	 
	
    
}
