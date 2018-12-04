package com.v1.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.request.ItemPublicTokenCreateRequest;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemPublicTokenCreateResponse;
import com.plaid.client.response.ItemStatus;
import com.v1.miBudget.daoimplementations.ItemDAOImpl;
import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import retrofit2.Response;

/**
 * Servlet implementation class UpdateBank
 */
@WebServlet("/updatebank")
public class UpdateBank extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private final String clientId = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nInside UpdateBank doPost()...");
		System.out.println("Retrieving public token...");
		String institutionId = request.getParameter("institutionId");
		String accessToken = itemDAOImpl.getAccessToken(institutionId);
		ItemGetRequest itemReq = new ItemGetRequest(accessToken);
		Response<ItemGetResponse> itemRes = client().service().itemGet(itemReq).execute();
		ItemStatus item = null;
		if (itemRes.isSuccessful()) {
			item = itemRes.body().getItem();
		}
		//SandboxPublicTokenCreateRequest req = new SandboxPublicTokenCreateRequest(accessToken, Arrays.asList(Product.TRANSACTIONS));
		ItemPublicTokenCreateRequest req = new ItemPublicTokenCreateRequest(accessToken);
		Response<ItemPublicTokenCreateResponse> res = client().service().itemPublicTokenCreate(req).execute();
		if (res.isSuccessful()) {
			System.out.println("public_token: " + res.body().getPublicToken());
			System.out.println("Public token retrieved. Returning to Profile.jsp");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/text");
			response.getWriter().append(res.body().getPublicToken().trim());
			response.getWriter().flush();
		}
	}
}
