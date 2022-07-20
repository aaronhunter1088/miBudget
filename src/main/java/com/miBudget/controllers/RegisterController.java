package com.miBudget.controllers;

import com.miBudget.entities.Account;
import com.miBudget.entities.Transaction;
import com.miBudget.entities.User;
import com.miBudget.core.MiBudgetState;
import com.miBudget.utilities.DateAndTimeUtility;
import com.miBudget.utilities.MiBudgetError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.miBudget.utilities.Constants.end;
import static com.miBudget.utilities.Constants.start;

@Controller
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegisterController extends MiBudgetController {
    private static Logger LOGGER = LogManager.getLogger(RegisterController.class);

    public RegisterController() {
        super();
    }

    @RequestMapping(path="/test", method=RequestMethod.GET)
    public ResponseEntity<String> testMe() {
        return ResponseEntity.ok("Register works");
    }

    @RequestMapping(path="/signup", method=RequestMethod.POST)
    public void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(start);
        LOGGER.info("Inside RegisterController:signup");
        String firstname = request.getParameter("firstName");
        String lastname = request.getParameter("lastName");
        String userProvidedCellphone = request.getParameter("cellphone");
        // New: for password reset and news only.
        String email = request.getParameter("email");
        String password=request.getParameter("password");
        String passwordRepeat = request.getParameter("passwordRepeat");

        boolean validated = Boolean.parseBoolean(request.getParameter("validated")); // used to do server side validation if user turned off JavaScript
        LOGGER.info("validated: " + validated);
        boolean isRegistered = false;
        HttpSession requestSession = null;
        int count = 0;

        // If validated comes back as false, need to validate fields
        // This only checks if the input fits a specific format.
        if (!validated) {
            // JavaScript may be off on user's browser
            // Need to validate fields
            LOGGER.info("Performing server-side validation");

            // Firstname
            if (firstname.equals("")) { LOGGER.info("First name is not valid. Please check its value.");
            } else { count++; } // 1

            // Lastname
            if (lastname.equals("")) { LOGGER.info("Last name is not valid. Please check its value.");
            } else { count++; } // 2

            // Cellphone
            if (userProvidedCellphone.equals("") || userProvidedCellphone.length() != 10 || (!((Long)Long.parseLong(userProvidedCellphone) instanceof Long))    ){
                // if cellphone is blank, is not 10 digits or if it's not an Integer
                LOGGER.info("Cellphone is not valid. Please check its value.");
            } else { count++; } // 3

            // Email
            if (email.indexOf("@") <= -1 || email.indexOf(".") <= -1) { LOGGER.info("Email is not valid. Please check its value.");
            } else { count++; } // 4

            // Password
            // Password-repeat
            if ((password.equals("") || passwordRepeat.equals("")) && !password.equals(passwordRepeat)) {
                // if password or passwordRepeat are blank AND if they don't equal eachother
                LOGGER.info("Password or PasswordRepeat are blank or do not match. Please check the values.");
            } else { count++; } // 5

            // Check count
            if (count == 5) { validated = true; }
        } // end server-side validation
        else {
            try {
                // check if user is not in list of current users
                // Create a service to get all users in DB
                // Create a list object to save new service call to retrieve all users
                LOGGER.info("Before list is populated...");
                List<String> allCellphonesList = MiBudgetState.getMiBudgetDAOImpl().getAllCellphones();

                // Create a new user
                // User user = new User(allUsersListByCellphone.size()+1, firstname, lastname, cellphone, password);
                User regUser = new User(firstname, lastname, userProvidedCellphone, password, email);
                LOGGER.info("user created...");
                LOGGER.info(regUser);
                for (String cellphone : allCellphonesList) {
                    if (cellphone.equals(userProvidedCellphone)) {
                        LOGGER.info("a 'new user' is attempting to create a new account but they already exists.");
                        isRegistered = true;
                    }
                }
                LOGGER.info("isRegistered: " + isRegistered);
                if (isRegistered && validated) {
                    LOGGER.info("A registered user tried to re-Register with valid inputs. Redirecting to Login page.");
                    LOGGER.info("isRegistered: " + isRegistered);
                    request.setAttribute("Cellphone", userProvidedCellphone);
                    request.setAttribute("Password", password);
//				response.sendRedirect("Login.html");
                    request.getServletContext().getRequestDispatcher("view/Login.jsp")
                            .forward(request, response);
//				response.sendRedirect(request.getContextPath() + "/Login.jsp");
                    // TODO: Eventually change this logic to instead of redirecting, to straight logging in and redirecting to Welcome.jsp
                } else if (isRegistered && !validated) {
                    LOGGER.info("A registered user tried to re-Register with invalid inputs! Redirecting to Login page.");
                    LOGGER.debug("TODO: Fix redirecting");
                    request.setAttribute("Cellphone", userProvidedCellphone);
                    request.setAttribute("Password", password);
                    request.getServletContext().getRequestDispatcher("view/Login.jsp")
                            .forward(request, response);
                    // TODO: Display message to user before or after informing them of the validation results.
                } else if (!isRegistered && !validated) {
                    LOGGER.info("An unregistered user tried to Register but provided invalid inputs! Redirecting to Register page.");
                    request.setAttribute("Firstname", firstname);
                    request.setAttribute("Lastname", lastname);
                    request.setAttribute("Cellphone", userProvidedCellphone);
                    request.setAttribute("Email", email);
                    request.setAttribute("Password", password);
                    request.setAttribute("PasswordRepeat", passwordRepeat);
                    request.getServletContext().getRequestDispatcher("view/Register.jsp")
                            .forward(request, response);
                    // TODO: Print out a message to the user from the validation results.
                } else if (!isRegistered && validated) {
                    LOGGER.info("An unregistered user is attempting to Register. They have valid inputs! Redirecting to Profile page.");
                    // use MiBudgetState.getMiBudgetDAOImpl() to save user
                    int verify = MiBudgetState.getMiBudgetDAOImpl().addUserToDatabase(regUser);
                    if (verify == 0)
                        LOGGER.info("User added to database!");
                    else {
                        LOGGER.warn("Failed to add user to database.");
                        throw new MiBudgetError("Failed to add user to database.");
                    }
                    requestSession = request.getSession(true);

                    LOGGER.info("requestSessionId: " + requestSession.getId());
//				requestSession.setAttribute("requestSession", requestSession);
//				requestSession.setAttribute("requestSessionId", requestSession.getId());
//				requestSession.setAttribute("isUserLoggedIn", true);
//				requestSession.setAttribute("Firstname", firstname);
//				requestSession.setAttribute("Lastname", lastname);
//				requestSession.setAttribute("Cellphone", userProvidedCellphone); // Don't need
//				requestSession.setAttribute("Accounts", accounts); // Replaced with acctsAndInstitutionIdMap
//				requestSession.setAttribute("User", user); // add user object to requestSession object

                    HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
                    ArrayList<String> institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(regUser);
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
                    requestSession.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
                    requestSession.setAttribute("getTransactions", new JSONObject());
                    requestSession.setAttribute("transactionsList", new JSONArray());
                    requestSession.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
                    LOGGER.info("Redirecting to Profile.jsp");
                    LOGGER.info(end);
//                    RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("view/Profile.jsp");
//                    dispatcher.forward(request, response);
                    //getServletContext().getRequestDispatcher("/Profile.jsp")
                    //	.forward(request, response);
                    //response.sendRedirect("Welcome.jsp");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().append("Success: Redirecting to Welcome.jsp");
                }
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().append("Fail: Redirecting to Register.html");
            }
        }
        LOGGER.info(end);
    }
}
