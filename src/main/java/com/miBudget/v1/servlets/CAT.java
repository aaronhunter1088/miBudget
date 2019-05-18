package com.miBudget.v1.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Servlet implementation class CAT
 */
@WebServlet("/CAT")
public class CAT extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger LOGGER = null;
	static  {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(CAT.class);
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
		// TODO Auto-generated method stub
		HttpSession requestSession = request.getSession(false);
		if (requestSession != null && (Boolean)requestSession.getAttribute("isUserLoggedIn") == true) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/WEB-INF/view/CategoriesAndTransactions.jsp" );
			dispatcher.forward( request, response );
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		LOGGER.info("--- START ---");
		LOGGER.info("Inside the Categories and Transactions or, CAT doPost() servlet.");
		HttpSession session = request.getSession(false);
		
		String acctName = null;
		int transactionsRequested = 0;
		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			// A valid user is requesting transactions
			if (request.getParameter("formId").equals("transactions")) {
				acctName = request.getParameter("currentAccount");
				transactionsRequested = Integer.valueOf(request.getParameter("numberOfTrans"));
				LOGGER.info("acctName: " + acctName);
				LOGGER.info("transactions requested: " + transactionsRequested);
				LOGGER.info("--- END ---");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().append("\naccountName: " + acctName + "\ntransactionsReq: " + transactionsRequested);
			}
		}
		else {
			LOGGER.info("--- END ---");
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			response.getWriter().append("No response set");
		}
	}

}
