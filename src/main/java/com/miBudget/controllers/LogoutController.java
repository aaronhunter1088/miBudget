package com.miBudget.controllers;

import com.miBudget.core.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/logout")
@CrossOrigin(origins = "*")
public class LogoutController {
    private static Logger LOGGER = LogManager.getLogger(LogoutController.class);

    public LogoutController() {}

    @RequestMapping(path="/test", method= RequestMethod.GET)
    @ResponseBody
    public void testMe() {
        ResponseEntity.ok("Logout works");
    }

    @RequestMapping(path= "/", method=RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(Constants.start);
        LOGGER.info("LogoutController:logout");
        if (request.getSession() != null || request.getSession().getAttribute("user") != null) {
            LOGGER.info("session invalidated");
            request.getSession().invalidate();
        }
        LOGGER.info(Constants.end);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().append("Success: Redirecting to index.html");
        response.getWriter().flush();
    }

}
