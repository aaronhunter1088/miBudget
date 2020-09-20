package com.miBudget.v1.servlets;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.miBudget.v1.utilities.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.Location;
import com.miBudget.v1.entities.Transaction;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UserAccountObject;
import com.miBudget.v1.processors.TransactionsProcessor;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.TransactionsGetResponse;

import retrofit2.Response;


/**
 * Servlet implementation class CAT
 */
@WebServlet("/cat")
public class CAT extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	
	private final String clientId = "5ae66fb478f5440010e414ae";
	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
	public static Logger LOGGER = null;
	static  {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(CAT.class);
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
    public CAT() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession requestSession = request.getSession(false);
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/WEB-INF/view/CategoriesAndTransactions.jsp" );
			dispatcher.forward( request, response );
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info(Constants.start);
		LOGGER.info("Inside the Categories and Transactions or, CAT doPost() servlet.");
		HttpSession session = request.getSession(false);
		if (session.getId() != session.getAttribute("sessionId")) {
			LOGGER.info("not the same session");
			session = request.getSession();
		} else {
			LOGGER.info("valid active session");
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		User user = (User) session.getAttribute("user");
		String acctId = null;
		int transactionsRequested = 0;
		Transaction transaction = null;
		ArrayList<Transaction> transactions = null;
		JSONObject jsonObject = null;
		StringBuilder customTextForResponse = new StringBuilder();
				
		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			// Save a new Category: methodName = Save Category
			// Update an existing Category: methodName = Update Category
			// Delete an existing Category: methodName = Delete Category
			// Get Transactions: methodName = get transactions
			//final Transaction transaction = (Transaction) request.getParameter("transactionObject");
			String methodName = request.getParameter("methodName");
			// A valid user is requesting transactions
			//if (request.getParameter("formName").equals("transactions")) {
			if (methodName.equals("get transactions")) {
				java.sql.Date startDate = null, endDate = null;
				TransactionsProcessor transactionsProcessor = new TransactionsProcessor();
				String acctName = request.getParameter("currentAccount");
				int itemTableId = 0;
				// get all users accounts
				ArrayList<UserAccountObject> usersAccts = accountDAOImpl.getAllUserAccountObjectsFromUserAndItemTableId(user, 0);
				for (UserAccountObject uao : usersAccts) {
					if (uao.getNameOfAccount().equals(acctName)) {
						itemTableId = uao.getItemTableId();
						acctId = uao.getAccountId();
						break;
					}
				}
				try {
					transactionsRequested = Integer.valueOf(request.getParameter("numberOfTrans"));
				} catch (Exception e) { transactionsRequested = 50; }
				if (transactionsRequested > 50) transactionsRequested = 50;
				
				String accessToken = itemDAOImpl.getItem(itemTableId).getAccessToken();
				Response<TransactionsGetResponse> transactionsGetResponse = null;
				try {
					//startDate = (java.sql.Date) sdf.parse(request.getParameter("FromDate"));
					String fromDate = request.getParameter("fromDate");
					LOGGER.debug("requested from date: {}", fromDate);
					startDate = transactionsProcessor.getStartDate(fromDate);
					//endDate = (java.sql.Date) sdf.parse(request.getParameter("ToDate"));
					endDate = transactionsProcessor.getEndDate(request.getParameter("toDate"));
					LOGGER.info("StartDate: " + startDate);
					LOGGER.info("EndDate: " + endDate);
					transactionsGetResponse = transactionsProcessor.getTransactions(accessToken, acctId, transactionsRequested, startDate, endDate);
				} catch ( ParseException | NullPointerException e) {
					LOGGER.warn("Failed to read in FromDate or ToDate. Will set default dates...");
				} 
				if (startDate == null && endDate == null) {
					endDate = transactionsProcessor.createSqlEndDate();
					startDate = transactionsProcessor.createSqlStartDate();
					try {
						transactionsGetResponse = transactionsProcessor.getTransactions(accessToken, acctId, transactionsRequested, startDate, endDate);
					} catch (ParseException e) {
						LOGGER.warn("Failed to get transactions because: {}", e.getMessage());
					}
				}
				methodName = request.getParameter("methodName");
				LOGGER.info("accessToken: {}", accessToken);
				LOGGER.info("acctName: {}", acctName);
				LOGGER.info("transactions requested: {}", transactionsRequested);
				LOGGER.info("methodName: {}", methodName);
				LOGGER.info("endDate: {}", endDate);
				LOGGER.info("startDate: {}", startDate);
				
				List<com.miBudget.v1.entities.Transaction> finalTransactionsList = new ArrayList<>();

				if (transactionsGetResponse.isSuccessful()) {
					LOGGER.info("get transactions was successful");
					LOGGER.info("raw: {}", ((TransactionsGetResponse)transactionsGetResponse.body()).toString());
					LOGGER.info("count: {}", transactionsGetResponse.body().getTransactions().size());
					List<TransactionsGetResponse.Transaction> plaidTransactions = transactionsGetResponse.body().getTransactions();
					
					for (TransactionsGetResponse.Transaction transactionGetResponse : plaidTransactions) {
						// Make a new miBudget location from object
						Location miBudgetLocation = convertLocation(transactionGetResponse.getLocation());
						Date transactionDate = null;
						try {
							transactionDate = sdf.parse(transactionGetResponse.getDate());
						} catch (ParseException pe) { LOGGER.error("Failed to parse transactionDate"); }
						finalTransactionsList.add(new com.miBudget.v1.entities.Transaction(
								transactionGetResponse.getTransactionId(),
								transactionGetResponse.getAccountId(),
								transactionGetResponse.getName(),
								transactionGetResponse.getAmount(),
								miBudgetLocation,
								transactionGetResponse.getCategory(),
								transactionDate)
						);
					}
					// Return to UI a JsonObject with one property, Transactions, which is a List of Transaction objects
					jsonObject = new JSONObject().put("Transactions", finalTransactionsList);
					session.setAttribute("getTransactions", jsonObject);
					customTextForResponse.append(jsonObject);
					session.setAttribute("usersTransactions", finalTransactionsList);
					LOGGER.info("usersTransactions: {}", jsonObject.toString());
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().append(customTextForResponse);
				} else {
					LOGGER.error("raw: {}", transactionsGetResponse.raw());
					LOGGER.error("error body: {}", transactionsGetResponse.errorBody());
					LOGGER.error("code: {}", transactionsGetResponse.code());
				}
				//response.getWriter().append("\naccountName: " + acctName + "\ntransactionsReq: " + transactionsRequested + "\nmethodName: " + methodName);
			}
			else if (methodName.equals("ignore")) {
				LOGGER.info("methodName: {}", methodName);
				String transactionId = request.getParameter("transaction");
				transactions = (ArrayList<Transaction>) session.getAttribute("usersTransactions");
				for (Transaction t : transactions) {
					if (t.getTransactionId().equals(transactionId)) {
						transaction = t;
						ArrayList<Transaction> ignoredTransactions = user.getIgnoredTransactions() == null ? new ArrayList<>() : user.getIgnoredTransactions();
						ignoredTransactions.add(transaction);
						user.setIgnoredTransactions(ignoredTransactions);
						LOGGER.info("ignoring this transaction: {}", transaction);
						break;
					}
				}
				transactions.remove(transaction);
				session.setAttribute("usersTransactions", transactions);
				response.setStatus(HttpServletResponse.SC_OK);
				jsonObject = new JSONObject().append("usersTransactions", transactions);
				response.getWriter().append(jsonObject.toString());
			}
			else if (methodName.equals("bill")) {}
			else if (methodName.equals("income")) {}
			else if (methodName.equals("save")) {}
		}
		else {
			LOGGER.error("Something is wrong with the session.");
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			response.getWriter().append("Something is wrong with the session.");
		}
		LOGGER.info("--- END ---");
	}

	private Transaction createNewTransaction(org.json.simple.JSONObject object) {
		return new Transaction((String)object.get("transactionid"), (String)object.get("accountid"), (String)object.get("name"), (double)object.get("amount"),
								(Location) object.get("location"), (ArrayList<String>)object.get("categories"), (Date)object.get("date"));
	}
	
	public Location convertLocation(TransactionsGetResponse.Transaction.Location location) {
		Location loc = new Location(
			location.getAddress(),
			location.getCity(),
			location.getState(),
			location.getZip()
		);
		return loc;
	}
}
