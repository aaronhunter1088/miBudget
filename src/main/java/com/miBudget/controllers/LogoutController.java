package com.miBudget.controllers;

import com.miBudget.core.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class LogoutController {
    private static Logger LOGGER = LogManager.getLogger(LogoutController.class);

    @RequestMapping(path= "/logout", method=RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(Constants.start);
        LOGGER.info("LogoutController:logout");
        try {
            if (request.getSession() != null || request.getSession().getAttribute("user") != null) {
                LOGGER.info("session invalidated");
                request.getSession().invalidate();
            }
            LOGGER.info(Constants.end);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().append("Success: Logged out. Redirecting to index.jsp");
            response.getWriter().flush();
        }
        catch (Exception e) {
            LOGGER.info("Exception e:" + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().append("Failed: ").append(e.getMessage());
            response.getWriter().flush();
        }
    }

}
