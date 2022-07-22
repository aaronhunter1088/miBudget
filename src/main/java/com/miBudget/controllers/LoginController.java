package com.miBudget.controllers;

import com.miBudget.dao.AccountDAO;
import com.miBudget.dao.BudgetDAO;
import com.miBudget.dao.ItemDAO;
import com.miBudget.dao.UserDAO;
import com.miBudget.entities.*;
import com.miBudget.utilities.Constants;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final ItemDAO itemDAO;
    private final BudgetDAO budgetDAO;

    @Autowired
    public LoginController(UserDAO userDAO, AccountDAO accountDAO, ItemDAO itemDAO, BudgetDAO budgetDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.itemDAO = itemDAO;
        this.budgetDAO = budgetDAO;
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
            LOGGER.info("LoginController:login");
            String usersCellphone = request.getParameter("cellphone");
            String usersPassword = request.getParameter("password");
            LOGGER.info("cellphone: " + usersCellphone);
            LOGGER.info("password: " + usersPassword);
            User loginUser = new User(usersCellphone, usersPassword);
            List<String> allCellphones = userDAO.findAllCellphones();
            boolean existingUser = false;
            for (String cellphone : allCellphones) {
                if (cellphone.equals(loginUser.getCellphone())) {
                    LOGGER.info("login user's cellphone found in list");
                    loginUser = userDAO.findUserByCellphone(cellphone); // user's name is set
                    // POPULATE USER BUDGET ATTRIBUTES
                    Budget usersBudget = budgetDAO.findBudgetByUserId(loginUser.getId());
                    loginUser.setBudget(usersBudget);
                    List<Long> accountIdsList = accountDAO.findAccountIdByUserId(loginUser.getId());
                    List<Long> itemsIdsList = itemDAO.findItemIdByUserId(loginUser.getId());
                    // Populate institutionIdsAndAccounts map
                    Map<String, List<Account>> institutionIdsAndAccounts = new HashMap<>();
                    int accountsTotal = 0;
                    for (Long itemId : itemsIdsList) {
                        Item item = Objects.requireNonNull(itemDAO.findById(itemId).orElse(null));
                        List<Account> accountsForItem = accountDAO.findAccountsByItemId(item.getItemId());
                        accountsTotal += accountsForItem.size();
                        institutionIdsAndAccounts.put(item.getInstitutionId(), accountsForItem);
                    }
                    LOGGER.info("institutionIdsAndAccounts size: " + institutionIdsAndAccounts.size());
                    HttpSession session = request.getSession(true);
                    session.setAttribute("institutionIdsAndAccountIds", institutionIdsAndAccounts);
                    session.setAttribute("loggedIn", true); // just a check
                    session.setAttribute("user", loginUser);
                    session.setAttribute("accountsSize", accountsTotal);
                    session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
                    session.setAttribute("getTransactions", new JSONObject());
                    session.setAttribute("transactionsList", new JSONArray());
                    session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
                    session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
                    session.setAttribute("change", "Once you finish adding accounts, and creating categories, your budget will appear here.");
                    LOGGER.info(Constants.end);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().append("Success: Logging into Homepage.jsp");
                    request.getServletContext().getRequestDispatcher("/WEB-INF/view/Homepage.jsp" ).forward(request, response);
                    response.getWriter().flush();
                    existingUser = true;
                    break;
                }
            }
            if (!existingUser) {
                LOGGER.info("Redirecting to Register.html");
                LOGGER.info(Constants.end);
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
