package com.miBudget.controllers;

import com.miBudget.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @RequestMapping(path="/clear", method= RequestMethod.GET)
    public String clearTransactions(HttpSession session)
    {
        // clear transactions from user and session
        if (session.getId() != null)
        {
            User user = (User) session.getAttribute("user");
            //user.setTransactions(new ArrayList<>());
            session.setAttribute("usersTransactions", new ArrayList<>());
        }
        else return "{\"message\": \"session is null\"";
        return "{\"message\":\"user and session cleared\"";
    }
}
