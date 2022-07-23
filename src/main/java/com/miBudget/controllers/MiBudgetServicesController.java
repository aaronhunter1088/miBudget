package com.miBudget.controllers;


import com.miBudget.dao.AccountDAO;
import com.miBudget.dao.BudgetDAO;
import com.miBudget.dao.ItemDAO;
import com.miBudget.dao.UserDAO;
import com.miBudget.entities.Account;
import com.miBudget.entities.Item;
import com.miBudget.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.util.*;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*")
public class MiBudgetServicesController {
    private static final Logger LOGGER = LogManager.getLogger(MiBudgetServicesController.class);

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final ItemDAO itemDAO;
    private final BudgetDAO budgetDAO;

    @Autowired
    public MiBudgetServicesController(UserDAO userDAO, AccountDAO accountDAO, ItemDAO itemDAO, BudgetDAO budgetDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.itemDAO = itemDAO;
        this.budgetDAO = budgetDAO;
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

}
