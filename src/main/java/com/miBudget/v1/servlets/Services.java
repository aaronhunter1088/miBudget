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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.v1.entities.Category;
import com.miBudget.v1.entities.Transaction;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UserAccountObject;
import com.google.gson.Gson;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("--- START ---");
		LOGGER.info("Inside Services doGet servlet.");
		HttpSession session = request.getSession(false);
		MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
		
		String method = request.getParameter("method");
		if (session == null)
			LOGGER.info("Session is null");
		User user = (User) session.getAttribute("user");
		
		// Lists for requests
		ArrayList<Category> categoriesList = null;
		ArrayList<Transaction> transactionsList = null;
		HashMap<String, ArrayList<UserAccountObject>> acctsAndInstitutionIdMap = null;
		String res = new String();
		
		if (method != null) {
			LOGGER.info("Method: {}", method);
			switch (method) {
				case "getAllCategories" :   categoriesList = miBudgetDAOImpl.getAllCategories(user);
											res = changeToJsonString(categoriesList);
											break;
				case "getAcctsAndInstitutionIdMap" : acctsAndInstitutionIdMap = miBudgetDAOImpl.getAcctsAndInstitutionIdMap(user);
													 res = changeToJsonString(acctsAndInstitutionIdMap);
													 break;
				case "getTransactions" : transactionsList = miBudgetDAOImpl.getTransactions(request);
										 res = changeToJsonString(transactionsList);
										 break;


			}
			
			
			// Save json string to response
			response.getWriter().append(res);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		LOGGER.info("--- END ---");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
