package com.miBudget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/categoriesAndTransactions")
@CrossOrigin(origins = "*")
public class CategoriesAndTransactionsController {

    @RequestMapping(path="/", method= RequestMethod.GET)
    public String goToCategoriesAndTransactions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/static/CategoriesAndTransactions.jsp";
        //Forwarding to [/WEB-INF/views/CategoriesAndTransactions.jsp]
    }
}
