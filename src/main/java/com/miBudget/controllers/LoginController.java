package com.miBudget.controllers;

import com.miBudget.daos.*;
import com.miBudget.entities.*;
import com.miBudget.core.Constants;
import com.miBudget.servlets.Login;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final ItemDAO itemDAO;
    private final BudgetDAO budgetDAO;
    private final CategoryDAO categoryDAO;

    @Autowired
    public LoginController(UserDAO userDAO, AccountDAO accountDAO, ItemDAO itemDAO, BudgetDAO budgetDAO, CategoryDAO categoryDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.itemDAO = itemDAO;
        this.budgetDAO = budgetDAO;
        this.categoryDAO = categoryDAO;
    }

//    @RequestMapping(path="/login", method=RequestMethod.GET)
//    public String goToLogin() {
//        return "Login";
//    }

    @RequestMapping(path="/login", method=RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            LOGGER.info(Constants.start);
            LOGGER.info("LoginController:login");
            String usersCellphone = request.getParameter("cellphone").replaceAll("-","");
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
                    final Long mainBudgetId = loginUser.getMainBudgetId();
                    Budget usersMainBudget = budgetDAO.findBudgetByMainBudgetId(loginUser.getMainBudgetId());
                    List<Budget> children = budgetDAO.findBudgetByUserId(loginUser.getId())
                            .stream()
                            .filter(budget -> !Objects.equals(budget.getId(), mainBudgetId))
                            .collect(Collectors.toList());
                    List<Category> allCategories = new ArrayList<>();
                    List<Long> childrenIds = new ArrayList<>();
                    for (Budget child : children) {
                        allCategories.addAll(categoryDAO.findAllByBudgetId(child.getId()));
                        childrenIds.add(child.getId());
                    }
                    usersMainBudget.setCategories(allCategories);
                    usersMainBudget.setChildBudgetIds(childrenIds);
                    loginUser.setBudget(usersMainBudget);

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
                    session.setAttribute("institutionIdsAndAccounts", institutionIdsAndAccounts);
                    session.setAttribute("loggedIn", true); // just a check
                    session.setAttribute("user", loginUser);
                    session.setAttribute("accountsSize", accountsTotal);
                    session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
                    session.setAttribute("getTransactions", new JSONObject());
                    session.setAttribute("transactionsList", new JSONArray());
                    session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
                    session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
                    if (accountsTotal == 0) { session.setAttribute("changingText", ":( You can't create a Budget with no Accounts. Go add Accounts next."); }
                    else {
                        // Can change the changingText when they log in based on conditions
                        session.setAttribute("changingText", "Wonderful, you've added an Account or two. Now, update your budget with custom Categories");
                    }
                    LOGGER.info("Redirecting to Homepage.jsp");
                    LOGGER.info(Constants.end);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().append("Success: Logging into Homepage.jsp");
                    //request.getRequestDispatcher("/Homepage").forward(request, response);
                    //request.getServletContext().getRequestDispatcher("/Homepage.jsp").forward(request, response);
                    //response.sendRedirect("/Homepage.jsp");
                    //response.getWriter().flush();
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
