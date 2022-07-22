package com.miBudget.controllers;

import com.miBudget.dao.AccountDAO;
import com.miBudget.dao.ItemDAO;
import com.miBudget.dao.UserDAO;
import com.miBudget.entities.*;
import com.miBudget.core.MiBudgetState;
import com.miBudget.utilities.Constants;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final ItemDAO itemDAO;

    @Autowired
    public LoginController(UserDAO userDAO, AccountDAO accountDAO, ItemDAO itemDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.itemDAO = itemDAO;
    }

    @RequestMapping(path="/test", method=RequestMethod.GET)
    public Response testMe() {
        return Response.ok("Login works").build();
    }

    @RequestMapping(path= "/", method=RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            LOGGER.info(Constants.start);
            LOGGER.info("Inside LoginController:login");
            String cellphone = request.getParameter("cellphone");
            String password = request.getParameter("password");
            boolean loginCredentials = false;
            List<User> allUsersList = null;
            HttpSession session = request.getSession(false);
            //Instant instantNow = Instant.now();
            allUsersList = userDAO.findAll();

            //allUsersList = MiBudgetState.getMiBudgetDAOImpl().getAllUsers();
            User loginUser = new User(cellphone, password);
            LOGGER.info("loginUser: " + loginUser);

            // Validate user
            LOGGER.info("validating user credentials...");
            LOGGER.info("cellphone: " + cellphone);
            LOGGER.info("password: " + password);
            // for every user in the list

            for (User activeUser : allUsersList) {
                if (loginUser.equals(activeUser)) {
                    LOGGER.info("loginUser matches activeUser");
                    LOGGER.info("Registered user. Logging in");
                    loginUser = activeUser;
                    loginCredentials = true;
                    break;
                }
            }
            // if logInUser does not have a first name, then they are a new user
            // else, logInUser will be fully populated and this is a returning user
            if (loginCredentials) {
                //ArrayList<String> accountIdsList = (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAllAccountsIds(loginUser);
                List<String> accountIdsList = accountDAO.findAccountByUserId(loginUser.getId());
                //loginUser.setAccountIds(accountIdsList);
                //if (loginUser.getCategories().size() == 0) loginUser.createCategories();
                //ArrayList<UserItemsObject> allUsersItemsList = MiBudgetState.getItemDAOImpl().getAllUserItems(loginUser);
                List<Item> allItemsForUser = itemDAO.findItemByUserId(loginUser.getId());
                // Populate accounts and institutions map
                HashMap<String, List<Account>> institutionIdsAndAccounts = new HashMap<>();
                for (Item item : allItemsForUser) {
                    institutionIdsAndAccounts.put(item.getInstitutionId(), accountDAO.findAccountsByItemId(item.getItemId()) );
                }
                int accountsTotal = 0;
                LOGGER.info("institutionIdsAndAccounts");
                for(String id : institutionIdsAndAccounts.keySet()) {
                    LOGGER.info("key: " + id);
                    for (Account a : institutionIdsAndAccounts.get(id)) {
                        LOGGER.info("value: " + a);
                        accountsTotal++;
                    }
                }

                //loginUser.setCategories(miBudgetDAOImpl.getAllCategories(loginUser));

                // Populate errors map

                LOGGER.info("institutionIdsAndAccounts size: " + institutionIdsAndAccounts.size());
                if (request.getSession() == null) {
                    LOGGER.info("session is null. setting session");
                } else {
                    LOGGER.info("Session already exists... but they're just logging in so get new session");
                    request.getSession().invalidate();
                }
                session = request.getSession(true);
                session.setAttribute("institutionIdsAndAccounts", institutionIdsAndAccounts);
                //session.setAttribute("institutionIdsList", institutionIdsList);
                //session.setAttribute("institutionIdsListSize", institutionIdsList.size());
                //session.setAttribute("session", session); // just a check
                //session.setAttribute("sessionId", session.getId()); // just a check
                session.setAttribute("isUserLoggedIn", true); // just a check
                //session.setAttribute("Firstname", loginUser.getFirstName());
                //session.setAttribute("Lastname", loginUser.getLastName());
                session.setAttribute("user", loginUser);
                session.setAttribute("accountsSize", accountsTotal);
                session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
                session.setAttribute("getTransactions", new JSONObject());
                session.setAttribute("transactionsList", new JSONArray());
                session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
                session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
                session.setAttribute("change", "Once you finish adding accounts, and creating categories, your budget will appear here.");
                LOGGER.info("Redirecting to Homepage.jsp");
                LOGGER.debug("contextPath: {}", request.getContextPath()); //  /miBudget
                LOGGER.info(Constants.end);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().append("Success: Logging into Homepage.jsp");
                request.getServletContext().getRequestDispatcher("/WEB-INF/view/Homepage.jsp" ).forward(request, response);
                response.getWriter().flush();
            }
            else {
                LOGGER.info("Redirecting to Register.jsp");
                LOGGER.info(Constants.end);
//                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher( "/Register.html" );
//                dispatcher.forward( request, response );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().append("Fail: Redirecting to Register.html");
                response.getWriter().flush();
            }
        } catch (Exception e) {
            LOGGER.info("Exception e:" + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().append("Failed: ").append(e.getMessage());
            response.getWriter().flush();
        }
    }
}
