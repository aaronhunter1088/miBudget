package com.miBudget.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.miBudget.utilities.Constants.end;
import static com.miBudget.utilities.Constants.start;

@RestController("/welcome")
public class Welcome extends HttpServlet {
    private static Logger LOGGER = LogManager.getLogger(Welcome.class);

    @GetMapping("/getWelcomePage")
    public void getWelcomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(start);
        LOGGER.info("Inside the getWelcomePage() method.");
        getServletContext().getRequestDispatcher("view/Welcome.jsp").forward(request, response);
        LOGGER.info(end);
    }

}
