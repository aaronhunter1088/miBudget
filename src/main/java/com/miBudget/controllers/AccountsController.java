package com.miBudget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountsController {

    @RequestMapping(path="/", method= RequestMethod.GET)
    public String goToAccounts(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/static/Accounts.jsp";
    }
}
