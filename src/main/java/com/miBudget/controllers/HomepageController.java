package com.miBudget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/homepage")
@CrossOrigin(origins = "*")
public class HomepageController {

    @RequestMapping(path="/", method=RequestMethod.GET)
    public String goToHomepage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "Homepage";
    }
}
