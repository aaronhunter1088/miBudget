package com.v1.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.response.ErrorResponse;
import com.plaid.client.response.ErrorResponse.ErrorType;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemStatus;
import com.v1.miBudget.daoimplementations.AccountDAOImpl;
import com.v1.miBudget.daoimplementations.ItemDAOImpl;
import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.v1.miBudget.entities.Item;
import com.v1.miBudget.entities.User;

import retrofit2.Response;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\nInside Profile doGet() servlet");
		HttpSession requestSession = request.getSession(false);
		System.out.println("request: " + request.getSession(false));
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			System.out.println("Attempting to log in...");
			User user = (User) request.getAttribute("user");
			requestSession.setAttribute("change", "This text will change after using the Plaid Link Initializer.");


			// Make a call to ItemGetResponse. It returns ItemStatus object.
			// That ItemStatus has an ErrorResponse that could have error code 
			// We can check for a code equal to ITEM_LOGIN_REQUIRED
			// Store results for all Items in a Map of institutionId : true/false
			// true for login required, false for not required
			// On Profile.jsp, we display an update icon if true for that Item.
			HashMap<String, Boolean> errMapForItems = new HashMap<>();
			ArrayList<String> ids = (ArrayList<String>) requestSession.getAttribute("institutionIdsList");
			ArrayList<Item> items = new ArrayList<>();
			for(int i = 0; i < ids.size(); i++) {
				Item item = itemDAOImpl.getItemFromUser(ids.get(i));
				System.out.println(item);
				items.add(item);
			}
//			
			for(int i = 0; i < items.size(); i++) {
				ItemGetRequest getReq = new ItemGetRequest(items.get(i).getAccessToken());
				Response<ItemGetResponse> getRes = client().service().itemGet(getReq).execute();
				if (getRes.isSuccessful()) {
					ItemStatus itemStatus = getRes.body().getItem();
					ErrorResponse err = itemStatus.getError();
					if (err != null) {
						if (err.getErrorType() == ErrorType.ITEM_ERROR) {
							System.out.println("There is an Item_Error");
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
			requestSession.setAttribute("ErrMapForItems", errMapForItems);
			
			response.sendRedirect("Profile.jsp");
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
		
		System.out.println("\nInside the Profile doPost() servlet.");
		HttpSession requestSession = request.getSession(false);  
        if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true){  
        	System.out.println("stored requestSessionId: " + requestSession.getAttribute("requestSessionId"));
        	//RequestDispatcher view = request.getRequestDispatcher("/Profile.html");
        	//view.forward(request, response); // doesn't take the session
        	response.sendRedirect("Profile.jsp");
        	
        }  
        else {   
        	System.out.println("User is not logged in");
            response.sendRedirect("Login.html");
        }  
	}
	
	 
	
    
}
