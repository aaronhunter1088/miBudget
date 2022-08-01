package com.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.entities.Account;
import com.miBudget.core.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.entities.Item;
import com.miBudget.entities.User;
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
@WebServlet("/accounts")
public class Accounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String clientId = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
	private static Logger LOGGER = LogManager.getLogger(Accounts.class);
	
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info("--- START ---");
		LOGGER.info("Inside Accounts doGet() servlet");
		HttpSession requestSession = request.getSession(false);
		//LOGGER.info("request: " + !request.getSession(false));
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			
			User user = (User)request.getAttribute("user");
			LOGGER.info("current user: " + user);
			HashMap<String, Boolean> errMapForItems = new HashMap<>();
			ArrayList<String> ids = (ArrayList<String>) requestSession.getAttribute("institutionIdsList");
			ArrayList<Item> items = new ArrayList<>();
			for (String id : ids) {
				//Item item = MiBudgetState.getItemDAOImpl().getItemFromUser(id);
				//LOGGER.info("Retrieved " + item);
				//items.add(item);
			}
			String errMsg = "";
			for(int i = 0; i < items.size(); i++) {
				ItemGetRequest getReq = new ItemGetRequest(items.get(i).getAccessToken());
				Response<ItemGetResponse> getRes = client().service().itemGet(getReq).execute();
				if (getRes.isSuccessful()) {
					ItemStatus itemStatus = getRes.body().getItem();
					ErrorResponse err = itemStatus.getError();
					//TODO: refactor to get the ErrorType and match it against available ErrorCodes
					// Item_Error, code Item_Login_Required
					// Can specify different messages by confirming the exact error
					if (err != null) {
						if (err.getErrorType() == ErrorType.ITEM_ERROR) {
							LOGGER.info("There is an Item_Error: ");
							LOGGER.info(err.toString());
							errMapForItems.put(items.get(i).getInstitutionId(), true);
							errMsg = err.getErrorMessage();
							errMsg = errMsg.substring(0, errMsg.indexOf("(")-1) + errMsg.substring(errMsg.indexOf(")")+1, errMsg.length());
							errMsg = errMsg.substring(0, errMsg.indexOf(".")+2) + "Click the update button to restore any banks in a bad state.";
						} 
					} else {
						LOGGER.info("No error for this item: " + items.get(i).toString());
						errMapForItems.put(items.get(i).getInstitutionId(), false);
					}
				} else {
					LOGGER.info("ItemGetResponse failed.");
					LOGGER.info(getRes.errorBody());
				}
			}
			if (errMsg.equals("") ) requestSession.setAttribute("change", "This text will change after using the Plaid Link Initializer.");
			else requestSession.setAttribute("change", errMsg);
			requestSession.setAttribute("ErrMapForItems", errMapForItems);
			LOGGER.info("Redirecting to Accounts.jsp.");
			LOGGER.info(Constants.end);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/Accounts.jsp");
			dispatcher.forward( request, response );
			//response.sendRedirect("Accounts.jsp");
		} else {
			LOGGER.info("requestSession: " + requestSession );
			LOGGER.info("isUserLoggedIn: " + requestSession.getAttribute("isUserLoggedIn") );
			LOGGER.info("Redirecting to Login.html");
			LOGGER.info("--- END ---");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("WEB-INF/index.jsp");
			dispatcher.forward( request, response );
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info("--- START ---");
		LOGGER.info("Inside the Accounts doPost() servlet.");
		HttpSession requestSession = request.getSession(false);  
        if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true){  
        	LOGGER.info("stored requestSessionId: " + requestSession.getAttribute("requestSessionId"));
        	requestSession.setAttribute("institutionIdsAndAccounts", new HashMap<String, List<Account>>() );
        	//RequestDispatcher view = request.getRequestDispatcher("/Profile.html");
        	//view.forward(request, response); // doesn't take the session
        	LOGGER.info("--- END ---");
        	response.sendRedirect("Accounts.jsp");
        }  
        else {   
        	LOGGER.info("User is not logged in");
        	LOGGER.info("--- END ---");
            response.sendRedirect("Login.html");
        }  
	}
	
	 
	
    
}
