package com.miBudget.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "*")
public class IndexController {
    private static final Logger LOGGER = LogManager.getLogger(IndexController.class);

    public IndexController() {}

//    @RequestMapping(path="/", method=RequestMethod.GET)
//    public String goToIndex() throws IOException {
//        return "index";
//    }

//    @RequestMapping(path="/login", method=RequestMethod.GET)
//    public String goToLogin() throws IOException {
//        return "Login";
//    }
}
