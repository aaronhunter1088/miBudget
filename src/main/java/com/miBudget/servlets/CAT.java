//package com.miBudget.servlets;
//
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.*;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.miBudget.core.MiBudgetError;
//import com.miBudget.entities.*;
//import com.miBudget.core.MiBudgetState;
//import com.miBudget.utilities.Constants;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//
//import com.miBudget.processors.TransactionsProcessor;
//import com.plaid.client.PlaidClient;
//import com.plaid.client.response.TransactionsGetResponse;
//
//import retrofit2.Response;
//
///**
// * Servlet implementation class CAT
// */
//@WebServlet("/cat")
//public class CAT extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	private final String clientId = "5ae66fb478f5440010e414ae";
//	private final String secret = "0e580ef72b47a2e4a7723e8abc7df5";
//	private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
//
//	public static Logger LOGGER = LogManager.getLogger(CAT.class);
//
//    public final PlaidClient client() {
//		// Use builder to create a client
//		PlaidClient client = PlaidClient.newBuilder()
//				  .clientIdAndSecret(clientId, secretD)
//				  .publicKey("") // optional. only needed to call endpoints that require a public key
//				  .developmentBaseUrl() // or equivalent, depending on which environment you're calling into
//				  .build();
//		return client;
//	}
//
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public CAT() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		LOGGER.info(Constants.start);
//		LOGGER.info("Inside the Categories and Transactions or, CAT doGet() servlet.");
//		HttpSession requestSession = request.getSession(false);
//		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
//			requestSession.setAttribute("change", "This text will change after taking an action.");
//			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/CategoriesAndTransactions.jsp");
//			dispatcher.forward( request, response );
//			LOGGER.info("redirecting to cat.jsp");
//		} else {
//			LOGGER.error("session=null? : {} isUserLoggedIn? : {}", requestSession==null?true:false, requestSession.getAttribute("isUserLoggedIn"));
//			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("index.html");
//			dispatcher.forward( request, response );
//		}
//		LOGGER.info(Constants.end);
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	@SuppressWarnings("unchecked")
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		LOGGER.info(Constants.start);
//		LOGGER.info("Inside the Categories and Transactions or, CAT doPost() servlet.");
//		HttpSession session = request.getSession(false);
//		if (session.getId() != session.getAttribute("sessionId")) {
//			LOGGER.info("not the same session");
//			session = request.getSession();
//		} else {
//			LOGGER.info("valid active session");
//		}
//		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		User user = (User) session.getAttribute("user");
//		String acctId = null;
//		int transactionsRequested = 0;
//		Transaction transaction = null;
//		ArrayList<Transaction> transactions = null;
//		JSONObject jsonObject = null;
//
//		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true)
//		{
//			// Save a new Category: methodName = Save Category
//			// Update an existing Category: methodName = Update Category
//			// Delete an existing Category: methodName = Delete Category
//			// Get Transactions: methodName = get transactions
//			//final Transaction transaction = (Transaction) request.getParameter("transactionObject");
//			String methodName = request.getParameter("methodName");
//			String incomingTransactionId = request.getParameter("transactionId");
//			transactions = (ArrayList<Transaction>) session.getAttribute("usersTransactions");
//			for (Transaction t : transactions) {
//				if (t.getTransactionId().equals(incomingTransactionId)) {
//					transaction = t;
//					break;
//				}
//			}
//			int ignoredTransactions = -1;
//			try {
//				ignoredTransactions = Integer.parseInt(request.getParameter("ignoredTransactions"));
//				if (request.getParameter("currentAccount").equals("Ignored Transactions"))
//					ignoredTransactions = user.getIgnoredTransactions().size() == 0 ? -1 : user.getIgnoredTransactions().size();
//			}
//			catch (NumberFormatException e) {
//				LOGGER.error("get ignored transactions error");
//			}
//
//			if (methodName.equals("get transactions") && ignoredTransactions == 0)
//			{
//				java.sql.Date startDate = null, endDate = null;
//				TransactionsProcessor transactionsProcessor = new TransactionsProcessor();
//				String acctName = request.getParameter("currentAccount");
//				String mask = acctName.substring(acctName.length()-4);
//				LOGGER.debug("acctName: " + acctName);
//				LOGGER.debug("mask: " + mask);
//				long itemTableId = 0;
//				// get all users accounts
//				ArrayList<UserAccountObject> usersAccts = MiBudgetState.getAccountDAOImpl().getAllUserAccountObjectsFromUserAndItemTableId(user, 0);
//				for (UserAccountObject uao : usersAccts) {
//					LOGGER.debug("UserAccountObject: " + uao);
//					if (uao.getAccountName().equals(acctName) ||
//						uao.getMask().equals(mask)) {
//						itemTableId = uao.getItemId();
//						acctId = uao.getAccountId();
//						break;
//					}
//				}
//				try {
//					transactionsRequested = Integer.valueOf(request.getParameter("numberOfTrans"));
//				} catch (Exception e) { transactionsRequested = 50; }
//				if (transactionsRequested > 50) transactionsRequested = 50;
//
//				String accessToken = MiBudgetState.getItemDAOImpl().getItem(itemTableId).getAccessToken();
//				String fromDate = request.getParameter("FromDate");
//				LOGGER.debug("requested from date: {}", fromDate);
//				startDate = (fromDate == null || StringUtils.equals(fromDate, "")) ?
//						transactionsProcessor.createSqlStartDate() :
//						transactionsProcessor.getSqlStartDateNew(fromDate);
//				String toDate = request.getParameter("ToDate");
//				endDate = (toDate == null  || StringUtils.equals(toDate, "")) ?
//						transactionsProcessor.createSqlEndDate() :
//						transactionsProcessor.getSqlEndDateNew(toDate);
//				LOGGER.info("StartDate: " + startDate);
//				LOGGER.info("EndDate: " + endDate);
//				Response<TransactionsGetResponse> transactionsGetResponse = null;
//				try {
//					transactionsGetResponse = transactionsProcessor.getTransactions(accessToken, acctId, transactionsRequested, startDate, endDate);
//				} catch (ParseException e) {
//					LOGGER.warn("Failed to get transactions because: {}", e.getMessage());
//				} catch (MiBudgetError error)
//				{
//					// TODO: print message into change console on screen
//					session.setAttribute("change", error.getErrorCode() + ": " + error.getMessage());
//
//				}
//				LOGGER.info("accessToken: {}", accessToken);
//				LOGGER.info("acctName: {}", acctName);
//				LOGGER.info("transactions requested: {}", transactionsRequested);
//				LOGGER.info("methodName: {}", methodName);
//				LOGGER.info("endDate: {}", endDate);
//				LOGGER.info("startDate: {}", startDate);
//
//				ArrayList<Transaction> finalTransactionsList = new ArrayList<>();
//
//				if (transactionsGetResponse != null && transactionsGetResponse.isSuccessful())
//				{
//					LOGGER.info("get transactions was successful");
//					LOGGER.info("raw: {}", ((TransactionsGetResponse)transactionsGetResponse.body()).toString());
//					LOGGER.info("count: {}", transactionsGetResponse.body().getTransactions().size());
//					List<TransactionsGetResponse.Transaction> plaidTransactions = new ArrayList<>();
//					if (transactionsGetResponse.body().getTransactions().size() > 0)
//					{
//						plaidTransactions = transactionsGetResponse.body().getTransactions();
//
//						for (TransactionsGetResponse.Transaction transactionGetResponse : plaidTransactions) {
//							// Make a new miBudget location from object
//							Location miBudgetLocation = convertLocation(transactionGetResponse.getLocation());
//							LocalDate transactionDate = LocalDate.parse(transactionGetResponse.getDate());
//							finalTransactionsList.add(new Transaction(
//									transactionGetResponse.getTransactionId(),
//									transactionGetResponse.getAccountId(),
//									transactionGetResponse.getName(),
//									transactionGetResponse.getAmount(),
//									miBudgetLocation,
//									transactionGetResponse.getCategory(),
//									transactionDate)
//							);
//						}
//						// TODO: Combine transactions categories to intelligently display
//						//finalTransactionsList = combineTransactions(finalTransactionsList, user.getCategories());
//						// Return to UI a JsonObject with one property, Transactions, which is a List of Transaction objects
//						jsonObject = new JSONObject();
//						jsonObject.put("test", "value");
//						jsonObject.put("Transactions", JSONArray.toJSONString(finalTransactionsList));
//						session.setAttribute("getTransactions", jsonObject);
//						String transactionsWord = finalTransactionsList.size() == 1 ? "transaction" : "transactions";
//						session.setAttribute("change", "You have successfully loaded " + finalTransactionsList.size() + " " + transactionsWord);
//						//customTextForResponse.append(jsonObject);
//						session.setAttribute("usersTransactions", finalTransactionsList);
//						user.setTransactions(finalTransactionsList);
//						LOGGER.info("usersTransactions: {}", jsonObject.toString());
//						response.setStatus(HttpServletResponse.SC_OK);
//						//response.setContentType("text/plain");
//						//response.getWriter().write("You have successfully loaded " + finalTransactionsList.size() + transactionsWord + ".");
//
//					}
//					else
//					{
//						session.setAttribute("getTransactions", null);
//						session.setAttribute("change", "There were no transactions to load during the time frame you chose.");
//						//customTextForResponse.append(jsonObject);
//						session.setAttribute("usersTransactions", finalTransactionsList);
//						user.setTransactions(finalTransactionsList);
//						LOGGER.info("usersTransactions: {}", 0);
//						response.setStatus(HttpServletResponse.SC_OK);
//					}
//					LOGGER.info(Constants.end);
//					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/CategoriesAndTransactions.jsp");
//					dispatcher.forward( request, response );
//				}
//				else
//				{
//					LOGGER.info("failed to get transactions");
//					LOGGER.info(Constants.end);
//					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/CategoriesAndTransactions.jsp");
//					dispatcher.forward( request, response );
//				} // transactionsGet error
//			}
//			else if (methodName.equals("get transactions") && ignoredTransactions != 0)
//			{
//				session.setAttribute("usersTransactions", user.getIgnoredTransactions());
//				if (user.getIgnoredTransactions().size() == 0)
//					session.setAttribute("change", "No transactions ignored yet.");
//				else
//					session.setAttribute("change", "You have recalled your ignored transactions");
//				response.setStatus(HttpServletResponse.SC_OK);
//				LOGGER.info(Constants.end);
//				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/CategoriesAndTransactions.jsp");
//				dispatcher.forward( request, response );
//			}
//			else if (methodName.equals("ignore"))
//			{
//				LOGGER.debug("transactionId: {}", request.getParameter("transactionId"));
//				LOGGER.debug("methodName: {}", methodName);
//				transactions = (ArrayList<Transaction>) session.getAttribute("usersTransactions");
//				for (Transaction t : transactions) {
//					if (t.getTransactionId().equals(incomingTransactionId)) {
//						transaction = t;
//						List<Transaction> ignoredTransactionsList = user.getIgnoredTransactions();
//						if (!ignoredTransactionsList.contains(transaction)) {
//							ignoredTransactionsList.add(transaction);
//							transactions.remove(transaction);
//							user.setTransactions(transactions);
//						}
//						else
//						{
//							break;
//						}
//						user.setIgnoredTransactions(ignoredTransactionsList);
//						LOGGER.info("ignoring this transaction: {}", transaction);
//						break;
//					}
//				}
//				session.setAttribute("usersTransactions", transactions);
//				//session.setAttribute("ignoredTransactions", user.getIgnoredTransactions());
//				String ignoreMsg = "You have successfully ignored the transaction: " +  transaction.getName() + " with an amt of " + transaction.getAmount() + ".";
//				session.setAttribute("change", ignoreMsg);
//				response.setStatus(HttpServletResponse.SC_OK);
//				response.setContentType("plain/text");
//				//jsonObject = new JSONObject().append("usersTransactions", transactions);
//				response.getWriter().append("You have successfully ignored the transaction: " + transaction.getName() + " with an amount of " + transaction.getAmount() + ".");
//				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/CategoriesAndTransactions.jsp");
//				dispatcher.forward( request, response );
//			}
//			else if (methodName.equals("bill"))
//			{
//				LOGGER.info("methodName: {}", methodName);
//				String transactionId = request.getParameter("transactionId");
//				transactions = (ArrayList<Transaction>) session.getAttribute("usersTransactions");
//				for (Transaction t : transactions) {
//					if (t.getTransactionId().equals(transactionId)) {
//						transaction = t;
//						List<Transaction> usersBills = user.getBills();
//						if (!usersBills.contains(transaction)) {
//							usersBills.add(transaction);
//							LOGGER.info("adding transaction as bill: {}", transaction);
//						}
//						else {
//							LOGGER.info("already have this transaction as a bill: {} ", transaction);
//						}
//						user.setBills(usersBills);
//
//						break;
//					}
//				}
//				transactions.remove(transaction);
//				session.setAttribute("usersTransactions", transactions);
//				session.setAttribute("change", "You have successfully added the transaction: " + transaction + " as a bill.");
//				response.setStatus(HttpServletResponse.SC_OK);
//				jsonObject = (JSONObject) new JSONObject().put("usersTransactions", transactions);
//				response.getWriter().append(jsonObject.toString());
//			}
//			else if (methodName.equals("income")) {}
//			else if (methodName.equals("save")) {}
//		}
//		else
//		{
//			LOGGER.error("Something is wrong with the session.");
//			response.setStatus(HttpServletResponse.SC_ACCEPTED);
//			response.getWriter().append("Something is wrong with the session.");
//		}
//
//	}
//
//	public Location convertLocation(TransactionsGetResponse.Transaction.Location location)
//	{
//		Location loc = new Location(
//			location.getAddress(),
//			location.getCity(),
//			location.getState(),
//			location.getZip()
//		);
//		return loc;
//	}
//
//
//}
