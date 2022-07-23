package com.miBudget.controllers;


import com.miBudget.dao.*;
import com.miBudget.entities.Account;
import com.miBudget.entities.Budget;
import com.miBudget.entities.Category;
import com.miBudget.entities.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*")
public class MiBudgetServicesController {
    private static final Logger LOGGER = LogManager.getLogger(MiBudgetServicesController.class);

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final ItemDAO itemDAO;
    private final BudgetDAO budgetDAO;
    private final CategoryDAO categoryDAO;
    private final TransactionDAO transactionDAO;

    @Autowired
    public MiBudgetServicesController(UserDAO userDAO, AccountDAO accountDAO, ItemDAO itemDAO, BudgetDAO budgetDAO, CategoryDAO categoryDAO, TransactionDAO transactionDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.itemDAO = itemDAO;
        this.budgetDAO = budgetDAO;
        this.categoryDAO = categoryDAO;
        this.transactionDAO = transactionDAO;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public Response testMe() {
        return Response.ok("MiBudgetServicesController works").build();
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public Response getInstitutionIdsAndAccountsMap(@PathVariable("userId") String userId) {
        List<Long> itemsIdsList = itemDAO.findItemIdByUserId(Long.valueOf(userId));
        Map<String, List<Account>> institutionIdsAndAccounts = new HashMap<>();
        for (Long itemId : itemsIdsList) {
            Item item = Objects.requireNonNull(itemDAO.findById(itemId).orElse(null));
            List<Account> accountsForItem = accountDAO.findAccountsByItemId(item.getItemId());
            institutionIdsAndAccounts.put(item.getInstitutionId(), accountsForItem);
        }
        return Response.ok(institutionIdsAndAccounts).build();
    }

    @RequestMapping(path="/budgets/{usersId}/{mainBudgetId}/categories", method = RequestMethod.GET)
    public Response getAllCategories(
            @PathVariable("userId") Long userId,
            @PathVariable("usersMainBudgetId") Long usersMainBudgetId) {
        List<Category> allCategories = new ArrayList<>();
        //Budget parentBudget = budgetDAO.findBudgetByMainBudgetId(usersMainBudgetId);
        List<Budget> children = budgetDAO.findBudgetByUserId(userId); // all budgets. need just children
        children.stream().filter(child -> !child.getId().equals(usersMainBudgetId)).collect(Collectors.toList());
        //List<Long> childrenBudgetIds = parentBudget.getChildBudgetIds();
        for(Budget childBudget : children) {
            //Budget childBudget = budgetDAO.findBudgetById(childBudgetId);
            //childrenBudgetIds.add(childBudget.getId());
            allCategories.addAll(childBudget.getCategories());
        }
        return Response.ok(allCategories).build();
    }
}
