package com.miBudget.v1.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miBudget.v1.entities.Account;

/**
 * Servlet implementation class CAT
 */
@WebServlet("/CAT")
public class CAT extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
			response.sendRedirect("CategoriesAndTransactions.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("--- START ---");
		System.out.println("Inside the Categories and Transactions or, CAT doPost() servlet.");
		HttpSession session = request.getSession(false);
		
		String acctName = null;
		int transactionsRequested = 0;
		if (session != null && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			// A valid user is requesting transactions
			if (request.getParameter("formId").equals("transactions")) {
				acctName = request.getParameter("currentAccount");
				transactionsRequested = Integer.valueOf(request.getParameter("numberOfTrans"));
				System.out.println("acctName: " + acctName);
				System.out.println("transactions requested: " + transactionsRequested);
				System.out.println("--- END ---");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().append("\naccountName: " + acctName + "\ntransactionsReq: " + transactionsRequested);
			}
		}
		else {
			System.out.println("--- END ---");
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			response.getWriter().append("No response set");
		}
	}

}
