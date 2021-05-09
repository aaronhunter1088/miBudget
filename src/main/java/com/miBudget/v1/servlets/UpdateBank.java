package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.request.ItemPublicTokenCreateRequest;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemPublicTokenCreateResponse;
import com.plaid.client.response.ItemStatus;

import retrofit2.Response;

/**
 * Servlet implementation class UpdateBank
 */
@WebServlet("/updatebank")
public class UpdateBank extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private final String clientId = "5ae66fb478f5440010e414ae";
	//private final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(UpdateBank.class);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateBank() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public PlaidClient client() {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		LOGGER.info("Inside UpdateBank doGet()...");
		HttpSession session = request.getSession(false);
		LOGGER.info("Updating ErrMapForItems...");
		String institutionId = request.getParameter("institutionId");
		// update ErrMapForItems
	    @SuppressWarnings("unchecked")
		HashMap<String, Boolean> errMapForItems = (HashMap<String, Boolean>) session.getAttribute("ErrMapForItems");
	    errMapForItems.put(institutionId, false);
	    session.setAttribute("ErrMapForItems", errMapForItems);
		LOGGER.info("Returning to Profile.jsp");
		LOGGER.info("UpdateBank Response: Good");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info("Inside UpdateBank doPost()...");
		HttpSession session = null;
		LOGGER.info("Retrieving public token...");
		String institutionId = request.getParameter("institutionId");
		String accessToken = itemDAOImpl.getAccessToken(institutionId);
		ItemGetRequest itemReq = new ItemGetRequest(accessToken);
		Response<ItemGetResponse> itemRes = client().service().itemGet(itemReq).execute();
		ItemStatus itemStatus = null;
		if (itemRes.isSuccessful()) {
			itemStatus = itemRes.body().getItem();
			LOGGER.info("itemStatus: " + itemStatus);
		}
		//SandboxPublicTokenCreateRequest req = new SandboxPublicTokenCreateRequest(accessToken, Arrays.asList(Product.TRANSACTIONS));
		ItemPublicTokenCreateRequest req = new ItemPublicTokenCreateRequest(accessToken);
		Response<ItemPublicTokenCreateResponse> res = client().service().itemPublicTokenCreate(req).execute();
		if (res.isSuccessful()) {
			session = request.getSession(false);
			// Might need to add logic to update ErrMapForItems
			//User user = (User) session.getAttribute("user");
			
			// update ErrMapForItems
		    @SuppressWarnings("unchecked")
			HashMap<String, Boolean> errMapForItems = (HashMap<String, Boolean>) session.getAttribute("ErrMapForItems");
		    errMapForItems.put(institutionId, false);
		    session.setAttribute("ErrMapForItems", errMapForItems);
		    session.setAttribute("change", "You successfully re-authorized your bank! It's good to go.");
		    LOGGER.info("public_token: " + res.body().getPublicToken());
			LOGGER.info("Public token retrieved. Returning to Profile.jsp");
			LOGGER.info("UpdateBank Response: Good");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/text");
			response.getWriter().append(res.body().getPublicToken().trim());
			response.getWriter().flush();
			
		}
		
	}
}
