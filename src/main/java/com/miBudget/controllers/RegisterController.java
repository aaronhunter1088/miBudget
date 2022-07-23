package com.miBudget.controllers;

import com.miBudget.core.MiBudgetError;
import com.miBudget.dao.BudgetDAO;
import com.miBudget.dao.CategoryDAO;
import com.miBudget.dao.UserDAO;
import com.miBudget.entities.Budget;
import com.miBudget.entities.Category;
import com.miBudget.entities.Transaction;
import com.miBudget.entities.User;
import com.miBudget.enums.AppType;
import com.miBudget.core.Constants;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.miBudget.core.Constants.end;
import static com.miBudget.core.Constants.start;

@Controller
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegisterController {
    private static Logger LOGGER = LogManager.getLogger(RegisterController.class);

    private final UserDAO userDAO;
    private final BudgetDAO budgetDAO;
    private final CategoryDAO categoryDAO;

    @Autowired
    public RegisterController(UserDAO userDAO, BudgetDAO budgetDAO, CategoryDAO categoryDAO) {
        this.userDAO = userDAO;
        this.budgetDAO = budgetDAO;
        this.categoryDAO = categoryDAO;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> testMe() {
        return ResponseEntity.ok("Register works");
    }

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public void signup(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            User registeringUser = new User(usersFirstName, usersLastName, usersCellphone, usersPassword, usersEmail, AppType.FREE);
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
                LOGGER.info("Unregistered user with valid inputs. Registering user");
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
     * Saves a new user, sets up their budget, prepares the session
     * and, finally logs them in
     * @param request
     * @param response
     * @param registeringUser
     * @throws MiBudgetError
     * @throws ServletException
     * @throws IOException
     */
    private void completeRegistration(HttpServletRequest request, HttpServletResponse response, User registeringUser) throws Exception {
        setupUser(registeringUser);
        login(request, response, registeringUser);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void setupUser(User registeringUser) {
        // Save user
        userDAO.save(registeringUser);
        // Create and save Budget for user. A budget can consist of one or many Budgets
        Budget budget = new Budget(registeringUser.getId());
        budgetDAO.save(budget); // needed to populate ID
        budget = budgetDAO.findBudgetByUserId(registeringUser.getId()).get(0); // will only be one at this moment
        List<Category> defaultCategories = budget.setupDefaultCategories(registeringUser.getId(), budget.getId());
        budget.setCategories(defaultCategories);
        budget.setAmount(BigDecimal.valueOf(budget.getCategories().stream().mapToDouble(Category::getBudgetedAmt).sum()));
        // Update budget
        budgetDAO.save(budget);
        //categoryDAO.saveAll(defaultCategories); Don't save Main budget categories. Main budget categories holds ALL categories
        Budget innerBudget = new Budget(budget);
        budgetDAO.save(innerBudget);
        innerBudget = budgetDAO.findBudgetById(budget.getId()+1L);
        for (Category childCategory : innerBudget.getCategories()) {childCategory.setBudgetId(innerBudget.getId());}
        categoryDAO.saveAll(innerBudget.getCategories());
        budget.setChildBudgetIds(List.of(innerBudget.getId()));
        registeringUser.setBudget(budget);
        registeringUser.setMainBudgetId(budget.getId());
        categoryDAO.saveAll(budget.getCategories());
        // Update user
        userDAO.save(registeringUser);
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
        session.setAttribute("changingText", "Great job, you're in. Next thing to do is to add Accounts.");
        LOGGER.info("Redirecting to Homepage.jsp");
        LOGGER.info(end);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().append("Success: Redirecting to Homepage.jsp");
        request.getServletContext().getRequestDispatcher("/WEB-INF/views/Homepage.jsp").forward(request, response);
        response.getWriter().flush();
    }
}
