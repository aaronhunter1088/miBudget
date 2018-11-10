package com.v1.miBudget.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.v1.miBudget.daoimplementations.AccountDAOImpl;
import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.v1.miBudget.entities.User;
import com.v1.miBudget.utilities.HibernateUtilities;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final SessionFactory factory = HibernateUtilities.getSessionFactory();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doPost(request, response);
		String btnSelected = request.getParameter("btnSelected");
		System.out.println("btnSelected: " + btnSelected);
		if (btnSelected.equals("Cancel")) {
			System.out.println("\nInside the Login servlet doGet(). Redirecting to index.html.");
			getServletContext().getRequestDispatcher("/index.html").forward(request, response);
		} else {
			System.out.println("\nInside the Login servlet doGet(). Redirecting to Login.html.");
			getServletContext().getRequestDispatcher("/Login.html").forward(request, response);
		}
		
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("null")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("\nInside the Login servlet.");
		String cellphone = request.getParameter("Cellphone");
		String password = request.getParameter("Password");
		boolean loginCredentials = false;
		List<User> allUsersList = null;
		HttpSession session = request.getSession(false);
		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miBudget", "root", "root");
//			mysqlSession = factory.openSession();
//			System.out.println("Session opened...");
//			if (mysqlSession.isOpen())
//    			System.out.println("open after creation");
			allUsersList = MiBudgetDAOImpl.getAllUsers();
//			if (mysqlSession.isOpen())
//    			System.out.println("open after getting allUsersList");
//			else
//				System.out.println("... we literally just closed it and you're checking again...");
			
//			PreparedStatement statement = conn.prepareStatement(
//					"SELECT * FROM users");
//			ResultSet rs = (ResultSet) statement.executeQuery();
//			while (rs.next()) {
//				if (rs.getString("Cellphone").equals(cellphone) && rs.getString("Password").equals(password) ) {
//					HttpSession requestSession = request.getSession();
//					requestSession.setAttribute("Firstname", rs.getString("Firstname"));
//					requestSession.setAttribute("Lastname", rs.getString("Lastname"));
//					loginCredentials = true;
//				} 
//			} 
		} catch (Exception e) {
			System.out.println(e);
		} 
		// Validate user
		System.out.println("validating user credentials...");
		System.out.println("cellphone: " + cellphone);
		System.out.println("password: " + password);
		// for every user in the list
		forAll:
		for (User user : allUsersList) {
			System.out.println("user from allUsersList: " + user.getCellphone() + ", " + user.getPassword());
			// if a registered user's cellphone and password are equal to the user's input
			if (user.getCellphone().equals(cellphone) && user.getPassword().equals(password)) {
				// registered user
				System.out.println("Registered user. Logging in");
				List<String> accountIdsList = AccountDAOImpl.getAllAccountsIds(user);
				user.setAccountIds(accountIdsList);
				int accounts = accountIdsList.size();
				loginCredentials = true;
				session = request.getSession(true);
				if (session == null || session.isNew()) {
					System.out.println("Session is null");
					System.out.println("requestSessionId: " + session.getId());
//					requestSession.setMaxInactiveInterval(0);
//					requestSession.setAttribute("requestSession", request.getSession(true));
					session.setAttribute("sessionId", session.getId());
					session.setAttribute("session", session); // just a check
					session.setAttribute("isUserLoggedIn", true);
					session.setAttribute("Firstname", user.getFirstName());
					session.setAttribute("Lastname", user.getLastName());
					session.setAttribute("user", user); // user.toJsonArray()
					session.setAttribute("NoOfAccts", accounts);
					for(String account_id : User.getAccountIds(user)) {
						System.out.println("accounts when logging in: " + account_id);
					}
				} else {
					System.out.println("Session already exists... but they're just logging in so get new session");
					session = request.getSession(true);
//					requestSession.setMaxInactiveInterval(0); // just a check
					session.setAttribute("session", session); // just a check
					session.setAttribute("sessionId", session.getId()); // just a check
					session.setAttribute("isUserLoggedIn", true); // just a check
					session.setAttribute("Firstname", user.getFirstName());
					session.setAttribute("Lastname", user.getLastName());
					session.setAttribute("user", user); 
					session.setAttribute("NoOfAccts", accounts);
				}
			} 
		} // end for all
		
		// if no user is found, must be a new user
		// but what if user accidentally typed in cellphone wrong
		// the user will be annoyed that they have to navigate back to the page
		// it should warn the user: hey, i couldn't find a match. try again. 
		// redirect to sign up page
		System.out.println("loginCredentials: " + loginCredentials);
		if (loginCredentials == true && (Boolean)session.getAttribute("isUserLoggedIn") == true) {
			session.setAttribute("isUserLoggedIn", true);
			getServletContext().getRequestDispatcher("/Welcome.jsp").forward(request, response);
//			response.sendRedirect("Welcome.jsp");
		} else {
			response.sendRedirect("Register.jsp");
		}
	}
}
