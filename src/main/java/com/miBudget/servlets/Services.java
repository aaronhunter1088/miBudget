package com.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.core.MiBudgetState;
import com.miBudget.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.entities.Category;
import com.miBudget.entities.Transaction;
import com.miBudget.entities.User;
import com.miBudget.entities.UserAccountObject;
import com.google.gson.Gson;

/**
 * Servlet implementation class Services
 */
@WebServlet("/Services")
public class Services extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Services.class);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Services() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String changeToJsonString(Object src) {
    	
    	// Convert list to json string
		Gson gson = new Gson();
		String srcAsJson = gson.toJson(src);
		LOGGER.info("Object as Json String");
		//LOGGER.info(srcAsJson);
		if (srcAsJson.equals("{}")) {
			// empty map. return blank string
			return "";
		}
		return srcAsJson;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.info(Constants.start);
		LOGGER.info("Inside Services doGet servlet.");
		HttpSession session = request.getSession(false);
		
		String method = request.getParameter("method");
		if (session == null)
			LOGGER.info("Session is null");
		User user = (User) session.getAttribute("user");
		
		// Lists for requests
		List<Category> categoriesList = null;
		List<Transaction> transactionsList = null;
		Map<String, ArrayList<UserAccountObject>> acctsAndInstitutionIdMap = null;
		String res = new String();
		
		if (method != null) {
			LOGGER.info("Method: {}", method);
			switch (method) {
				case "getAllCategories" :   categoriesList = MiBudgetState.getMiBudgetDAOImpl().getAllCategories(user);
											res = changeToJsonString(categoriesList);
											break;
				case "getAcctsAndInstitutionIdMap" : acctsAndInstitutionIdMap = MiBudgetState.getMiBudgetDAOImpl().getAcctsAndInstitutionIdMap(user);
													 res = changeToJsonString(acctsAndInstitutionIdMap);
													 break;
				case "getTransactions" : transactionsList = MiBudgetState.getMiBudgetDAOImpl().getTransactions(request);
										 res = changeToJsonString(transactionsList);
										 break;
				case "getAllUsersByCellPhone" : getAllUsersByCellphone(session);

				case "clearTransactions" : res = MiBudgetState.getMiBudgetDAOImpl().clearTransactions(session);
										   break;

				default : LOGGER.error("Unknown method: " + method);
			}

			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			// Save json string to response
			response.getWriter().append(res);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		LOGGER.info(Constants.end);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}

	public List<String> getAllUsersByCellphone(HttpSession session) {
		return null;
	}
}
