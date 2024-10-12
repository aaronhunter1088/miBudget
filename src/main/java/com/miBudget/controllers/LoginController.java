package com.miBudget.controllers;

import com.miBudget.core.Constants;
import com.miBudget.daos.*;
import com.miBudget.entities.*;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

    @RequestMapping(path="/login", method=RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(Constants.start);
        LOGGER.info("LoginController:login");
        try {
            String usersCellphone = request.getParameter("cellphone").replaceAll("-","");
            String usersPassword = request.getParameter("password");
            LOGGER.info("cellphone: " + usersCellphone);
            LOGGER.info("password: " + usersPassword);
            User loginUser; // = new User(usersCellphone, usersPassword);
            List<String> allCellphones = userDAO.findAllCellphones();
            boolean existingUser = false;
            for (String cellphone : allCellphones) {
                if (cellphone.equals(usersCellphone) ) {
                    LOGGER.info("login user's cellphone found in list");
                    loginUser = userDAO.findUserByCellphone(cellphone); // user's name is set
                    // POPULATE USER BUDGET ATTRIBUTES
                    final Long mainBudgetId = loginUser.getMainBudgetId();
                    Budget usersMainBudget = budgetDAO.findBudgetByMainBudgetId(mainBudgetId);
                    List<Budget> childrenBudgets = budgetDAO.findBudgetByUserId(loginUser.getId())
                            .stream()
                            .filter(budget -> !Objects.equals(budget.getId(), mainBudgetId))
                            .toList();
                    for(Budget childBudget : childrenBudgets) {
                        childBudget.setCategories(categoryDAO.findAllByBudgetId(childBudget.getId()));
                    }
                    List<Category> allCategories = childrenBudgets.stream().map(Budget::getCategories).toList().get(0);
                    usersMainBudget.setCategories(allCategories);
                    usersMainBudget.setChildBudgetIds(childrenBudgets.stream().map(Budget::getId).toList());
                    loginUser.setBudget(usersMainBudget);

                    List<Account> accountsList = accountDAO.findAccountsByUserId(loginUser.getId());
                    loginUser.setAccounts(accountsList);
                    List<String> itemsIdsList = itemDAO.findItemIdByUserId(loginUser.getId());
                    // Populate institutionIdsAndAccounts map
                    Map<String, List<Account>> institutionIdsAndAccounts = new HashMap<>();
                    ArrayList<Item> items = new ArrayList<>();
                    int accountsTotal = 0;
                    for (String itemId : itemsIdsList) {
                        Item item = Objects.requireNonNull(itemDAO.findItemByItemId(itemId));
                        items.add(item);
                        List<Account> accountsForItem = accountDAO.findAccountsByItemId(item.getItemId());
                        accountsTotal += accountsForItem.size();
                        institutionIdsAndAccounts.put(item.getInstitutionId(), accountsForItem);
                    }
                    loginUser.setItems(items);
                    loginUser.setAccounts(accountsList);
                    loginUser.setBills(new ArrayList<Transaction>());
                    loginUser.setTransactions(new ArrayList<Transaction>());
                    loginUser.setIgnoredTransactions(new ArrayList<Transaction>());
                    LOGGER.info("institutionIdsAndAccounts size: " + institutionIdsAndAccounts.size());
                    HttpSession session = request.getSession(true);
                    session.setAttribute("institutionIdsAndAccounts", institutionIdsAndAccounts);
                    session.setAttribute("errMapForItems", new HashMap<>());
                    session.setAttribute("loggedIn", true); // just a check
                    session.setAttribute("accountsSize", accountsList.size());
                    session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
                    session.setAttribute("newCategoryName", null);
                    session.setAttribute("user", loginUser);
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
        }
        catch (Exception e) {
            LOGGER.info("Exception e:" + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().append("Failed: ").append(e.getMessage());
            response.getWriter().flush();
        }
    }
}
