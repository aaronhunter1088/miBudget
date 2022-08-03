package com.miBudget.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*")
public class WebController {
    private static Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    @RequestMapping(value="/")
    public String goToIndex() {
        return "index";
    }

    @RequestMapping(path="/login")
    public String goToLogin() {
        return "Login";
    }

    @RequestMapping(path="/register")
    public String goToRegister() {
        return "Register";
    }

    @RequestMapping(path="/homepage")
    public String goToHomepage() {
        return "Homepage";
    }

    @RequestMapping(path="/accounts")
    public String goToAccounts() {
        return "Accounts";
    }

    @RequestMapping(path="/cat")
    public String goToCategoriesAndTransactions() {
        return "CategoriesAndTransactions";
    }
}
