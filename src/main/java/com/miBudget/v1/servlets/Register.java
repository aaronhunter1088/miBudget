package com.miBudget.v1.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.MappingException;

import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.User;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside the Register doGet() servlet.");
		String btnSelected = request.getParameter("btnSelected");
		response.reset();
		if (btnSelected.equals("Cancel")) {
			System.out.println("Redirecting to Index.html.");
			System.out.println("--- END ---");
			getServletContext().getRequestDispatcher("/index.html").forward(request, response);
		} else if (btnSelected.equals("Register")) {
			System.out.println("Redirecting to Register.html.");
			System.out.println("--- END ---");
			getServletContext().getRequestDispatcher("/Register.html").forward(request, response);
		} else {
			System.out.println("Redirecting to Login.html.");
			System.out.println("--- END ---");
			getServletContext().getRequestDispatcher("/Login.html").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- START ---");
		System.out.println("Inside the Register servlet.");
		
		
		
		String firstname = request.getParameter("Firstname");
		String lastname = request.getParameter("Lastname");
		String userProvidedCellphone = request.getParameter("Cellphone");
		// New
		String email = request.getParameter("Email");
		String password=request.getParameter("Password");
		// New
		String passwordRepeat = request.getParameter("Password-repeat");
		// New
		boolean validated = Boolean.parseBoolean(request.getParameter("Validated")); // used to do server side validation if user turned off JavaScript
		System.out.println("validated: " + validated);
		boolean isRegistered = false;
		HttpSession requestSession = null;
		int count = 0;
		
		
		// If validated comes back as false, need to validate fields
		// This only checks if the input fits a specific format.
		if (!validated) {
			// JavaScript may be off on user's browser
			// Need to validate fields
			System.out.println("Performing server-side validation");
			
			// Firstname
			if (firstname.equals("")) { System.out.println("First name is not valid. Please check its value.");
			} else { count++; } // 1
			
			// Lastname
			if (lastname.equals("")) { System.out.println("Last name is not valid. Please check its value.");
			} else { count++; } // 2
			
			// Cellphone
			if (userProvidedCellphone.equals("") || userProvidedCellphone.length() != 10 || (!((Long)Long.parseLong(userProvidedCellphone) instanceof Long))    ){
				// if cellphone is blank, is not 10 digits or if it's not an Integer
				System.out.println("Cellphone is not valid. Please check its value.");
			} else { count++; } // 3
			
			// Email
			if (email.indexOf("@") <= -1 || email.indexOf(".") <= -1) { System.out.println("Email is not valid. Please check its value.");
			} else { count++; } // 4
			
			// Password
			// Password-repeat
			if ((password.equals("") || passwordRepeat.equals("")) && !password.equals(passwordRepeat)) {
				// if password or passwordRepeat are blank AND if they don't equal eachother
				System.out.println("Password or PasswordRepeat are blank or do not match. Please check the values.");
			} else { count++; } // 5
			
			// Check count
			if (count == 5) { validated = true; }
		} // end server-side validation

		try {
			
			// check if user is not in list of current users
			// Create a service to get all users in DB
			// Create a list object to save new service call to retrieve all users
			System.out.println("Before list is populated...");
//			session.beginTransaction();
			List<String> allCellphonesList = miBudgetDAOImpl.getAllCellphones();
			
			// Create a new user
//			User user = new User(allUsersListByCellphone.size()+1, firstname, lastname, cellphone, password);
			User regUser = new User(firstname, lastname, userProvidedCellphone, password, email);
			System.out.println("user created...");
			System.out.println(regUser);
			for (String currentCellphone : allCellphonesList) {
				if (currentCellphone.equals(userProvidedCellphone)) {
					System.out.println("a 'new user' is attempting to create a new account but they already exists.");
					isRegistered = true;
				}
			}
			System.out.println("isRegistered: " + isRegistered);
			if (isRegistered && validated) {
				System.out.println("A registered user tried to re-Register with valid inputs. Redirecting to Login page.");
				System.out.println("isRegistered: " + isRegistered);
				request.setAttribute("Cellphone", userProvidedCellphone);
				request.setAttribute("Password", password);
//				response.sendRedirect("Login.html");
				getServletContext().getRequestDispatcher("/Login.jsp")
					.forward(request, response);
//				response.sendRedirect(request.getContextPath() + "/Login.jsp");
				// TODO: Eventually change this logic to instead of redirecting, to straight logging in and redirecting to Welcome.jsp
			}  else if (isRegistered && !validated) {
				System.out.println("A registered user tried to re-Register with invalid inputs! Redirecting to Login page.");
				request.setAttribute("Cellphone", userProvidedCellphone);
				request.setAttribute("Password", password);
				getServletContext().getRequestDispatcher("/Login.jsp")
					.forward(request, response);
				// TODO: Display message to user before or after informing them of the validation results.
			} else if (!isRegistered && !validated) {
				System.out.println("An unregistered user tried to Register but provided invalid inputs! Redirecting to Register page.");
				request.setAttribute("Firstname", firstname);
				request.setAttribute("Lastname", lastname);
				request.setAttribute("Cellphone", userProvidedCellphone);
				request.setAttribute("Email", email);
				request.setAttribute("Password", password);
				request.setAttribute("PasswordRepeat", passwordRepeat);
				getServletContext().getRequestDispatcher("/Register.jsp")
					.forward(request, response);
				// TODO: Print out a message to the user from the validation results.
			} else if (!isRegistered && validated) {
				System.out.println("An unregistered user is attempting to Register. They have valid inputs! Redirecting to Profile page.");
				// use MiBudgetDAOImpl to save user
				int verify = miBudgetDAOImpl.addUserToDatabase(regUser);
				if (verify == 0)
					System.out.println("Failed to add user to database.");
				else {
					System.out.println("User added to database!");
					System.out.println(regUser.getFirstName() + " default categories saved.");
				}
				requestSession = request.getSession(true);
				
				System.out.println("requestSessionId: " + requestSession.getId());
//				requestSession.setAttribute("requestSession", requestSession);
//				requestSession.setAttribute("requestSessionId", requestSession.getId());
//				requestSession.setAttribute("isUserLoggedIn", true);
//				requestSession.setAttribute("Firstname", firstname);
//				requestSession.setAttribute("Lastname", lastname);
//				requestSession.setAttribute("Cellphone", userProvidedCellphone); // Don't need
//				requestSession.setAttribute("Accounts", accounts); // Replaced with acctsAndInstitutionIdMap
//				requestSession.setAttribute("User", user); // add user object to requestSession object
				
				HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
				ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(regUser);
				
				requestSession.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
				requestSession.setAttribute("institutionIdsList", institutionIdsList);
				requestSession.setAttribute("institutionIdsListSize", institutionIdsList.size());
				requestSession.setAttribute("session", requestSession); // just a check
				requestSession.setAttribute("sessionId", requestSession.getId()); // just a check
				requestSession.setAttribute("isUserLoggedIn", true); // just a check
				requestSession.setAttribute("Firstname", regUser.getFirstName());
				requestSession.setAttribute("Lastname", regUser.getLastName());
				requestSession.setAttribute("user", regUser); 
				requestSession.setAttribute("accountsSize", 0);
				requestSession.setAttribute("isUserLoggedIn", true);
				
				System.out.println("Redirecting to Profile.jsp");
				System.out.println("--- END ---");
				getServletContext().getRequestDispatcher("/Profile.jsp")
					.forward(request, response);
				//response.sendRedirect("Welcome.jsp");
			}
		} catch (MappingException e) {
			System.out.println("Failed to redirect user...");
			System.out.println(e);
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		} 
	}
}
