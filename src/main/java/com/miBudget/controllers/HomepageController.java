package com.miBudget.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/homepage")
@CrossOrigin(origins = "*")
public class HomepageController {

    @RequestMapping(path="/", method= RequestMethod.GET)
    public void goToHomepage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getServletContext().getRequestDispatcher("/WEB-INF/views/Homepage.jsp").forward(request, response);
    }
}
