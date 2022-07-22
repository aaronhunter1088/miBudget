package com.miBudget.controllers;

import com.miBudget.core.MiBudgetError;
import com.miBudget.dao.UserDAO;
import com.miBudget.entities.Account;
import com.miBudget.entities.Transaction;
import com.miBudget.entities.User;
import com.miBudget.utilities.Constants;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.miBudget.utilities.Constants.end;
import static com.miBudget.utilities.Constants.start;

@Controller
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegisterController {
    private static Logger LOGGER = LogManager.getLogger(RegisterController.class);

    private final UserDAO userDAO;

    @Autowired
    public RegisterController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> testMe() {
        return ResponseEntity.ok("Register works");
    }

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LOGGER.info(start);
            LOGGER.info("RegisterController:signup");
            String usersFirstName = request.getParameter("firstName");
            String usersLastName = request.getParameter("lastName");
            String usersCellphone = request.getParameter("cellphone");
            String usersEmail = request.getParameter("email");
            String usersPassword = request.getParameter("password");
            String usersPasswordRepeat = request.getParameter("passwordRepeat");
            boolean validated = Boolean.parseBoolean(request.getParameter("validated")); // used to do server side validation if user turned off JavaScript
            LOGGER.info("firstName: {}", usersFirstName);
            LOGGER.info("lastName: {}", usersLastName);
            LOGGER.info("cellphone: {}", usersCellphone);
            LOGGER.info("email: {}", usersEmail);
            LOGGER.info("password: {}", usersPassword);
            LOGGER.info("passwordRepeat: {}", usersPasswordRepeat);
            LOGGER.info("validated: " + validated);
            User registeringUser = new User(usersFirstName, usersLastName, usersCellphone, usersPassword, usersEmail);
            List<String> allCellphones = userDAO.findAllCellphones();
            boolean existingUser = false;
            for (String cellphone : allCellphones) {
                if (cellphone.equals(registeringUser.getCellphone())) {
                    LOGGER.info("registering user already exists!");
                    existingUser = true;
                    registeringUser = userDAO.findUserByCellphone(cellphone);
                    break;
                }
            }
            LOGGER.info("existingUser: {} | validated: {}", existingUser, validated);
            if (existingUser && validated) {
                LOGGER.info("Tried to re-Register with validated inputs. Redirecting to Login page.");
                login(request, response, registeringUser);
            } else if (!existingUser && !validated) {
                LOGGER.info("Inputs are invalidated. Validating");
                if (validateUserInputs(usersFirstName, usersLastName, usersCellphone, usersEmail, usersPassword, usersPasswordRepeat)) {
                    LOGGER.info("Success: Valid inputs");
                    completeRegistration(request, response, registeringUser);
                } else {
                    LOGGER.info("Inputs are invalid");
                    LOGGER.info(Constants.end);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.getWriter().append("Fail: Redirecting to Register.html");
                    response.getWriter().flush();
                }
            } else { // not an existing user and inputs are validated
                completeRegistration(request, response, registeringUser);
            }
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info(end);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().append("Failed to register: ").append(e.getMessage());
        }
    }

    /**
     * Server-side validation of inputs for a new user
     * @param firstName
     * @param lastName
     * @param cellphone
     * @param email
     * @param password
     * @param passwordRepeat
     * @return
     */
    private boolean validateUserInputs(String firstName, String lastName, String cellphone, String email, String password, String passwordRepeat) {
        LOGGER.info("Performing server-side validation");
        // Firstname
        if (Strings.isBlank(firstName)) {
            LOGGER.error("First name is not valid. Please check its value.");
            return false;
        }
        // Lastname
        if (Strings.isBlank(lastName)) {
            LOGGER.error("Last name is not valid. Please check its value.");
            return false;
        }
        // Cellphone
        cellphone = Objects.requireNonNull(cellphone.replaceAll("-", ""));
        if (cellphone.length() != 10) {
            LOGGER.error("Cellphone is not valid. Please check its value.");
            return false;
        }
        // Email
        if (!email.contains("@") && !email.contains(".")) {
            LOGGER.error("Email is not valid. Please check its value.");
            return false;
        }
        // Password & Password-repeat
        if ((Strings.isBlank(password) || Strings.isBlank("")) && !Objects.equals(password, passwordRepeat)) {
            // if password or passwordRepeat are blank AND if they are not equal to each other
            LOGGER.error("Password or PasswordRepeat are blank or do not match. Please check the values.");
            return false;
        }
        return true;
    }

    /**
     * Saves a new user and then logs them in
     * @param request
     * @param response
     * @param registeringUser
     * @throws MiBudgetError
     * @throws ServletException
     * @throws IOException
     */
    private void completeRegistration(HttpServletRequest request, HttpServletResponse response, User registeringUser) throws MiBudgetError, ServletException, IOException {
        LOGGER.info("Unregistered user with valid inputs. Registering user");
        userDAO.save(registeringUser);
        login(request, response, registeringUser);
    }

    /**
     * Sets the session for the registering user and redirects to the Homepage
     * @param request
     * @param response
     * @param registeringUser
     * @throws IOException
     * @throws ServletException
     */
    private void login(HttpServletRequest request, HttpServletResponse response, User registeringUser) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        session.setAttribute("institutionIdsAndAccounts", new HashMap<>());
        session.setAttribute("user", registeringUser);
        session.setAttribute("accountsSize", 0);
        session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
        session.setAttribute("getTransactions", new JSONObject());
        session.setAttribute("transactionsList", new JSONArray());
        session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
        LOGGER.info("Redirecting to Homepage.jsp");
        LOGGER.info(end);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().append("Success: Redirecting to Homepage.jsp");
        request.getServletContext().getRequestDispatcher("/WEB-INF/view/Homepage.jsp" ).forward(request, response);
        response.getWriter().flush();
    }
}
