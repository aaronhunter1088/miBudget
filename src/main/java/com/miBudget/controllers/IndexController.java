package com.miBudget.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

@RestController
@RequestMapping("/miBudget")
@CrossOrigin(origins = "*")
public class IndexController {
    private static final Logger LOGGER = LogManager.getLogger(IndexController.class);

    public IndexController() {}

    @RequestMapping(path="/test", method=RequestMethod.GET)
    public Response testMe() {
        return Response.ok("index works").build();
    }

    @RequestMapping(path="/", method= RequestMethod.GET)
    public void miBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getServletContext().getRequestDispatcher("/index.html").forward(request, response);
    }
}
