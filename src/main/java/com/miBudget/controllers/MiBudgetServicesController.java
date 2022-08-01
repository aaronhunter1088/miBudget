package com.miBudget.controllers;


import com.miBudget.daos.*;
import com.miBudget.entities.Account;
import com.miBudget.entities.Budget;
import com.miBudget.entities.Category;
import com.miBudget.entities.Item;
import com.miBudget.utilities.JsonUtility;
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

    @RequestMapping(path="/budgets/{userId}/{mainBudgetId}/categories", method = RequestMethod.GET)
    public Response getAllCategories(
            @PathVariable("userId") Long userId,
            @PathVariable("mainBudgetId") Long mainBudgetId) {
        List<Category> allCategories = new ArrayList<>();
        List<Budget> children = budgetDAO.findBudgetByUserId(userId)
                .stream()
                .filter(budget -> !Objects.equals(budget.getId(), mainBudgetId))
                .collect(Collectors.toList());
        for (Budget child : children) {
            allCategories.addAll(categoryDAO.findAllByBudgetId(child.getId()));
        }
        String result = JsonUtility.changeToJsonString(allCategories);
        return Response.ok(result).build();
    }
}
