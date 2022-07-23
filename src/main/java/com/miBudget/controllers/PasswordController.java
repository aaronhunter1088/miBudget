package com.miBudget.controllers;

import com.miBudget.dao.UserDAO;
import com.miBudget.entities.Email;
import com.miBudget.entities.User;
import com.miBudget.utilities.EmailUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.List;

@RestController
@RequestMapping("/password")
@CrossOrigin(origins = "*")
public class PasswordController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final EmailUtility emailUtility;
    private final UserDAO userDAO;

    @Autowired
    public PasswordController(UserDAO userDAO) {
        this.userDAO = userDAO;
        emailUtility = new EmailUtility();
    }

    @RequestMapping(path="/test", method=RequestMethod.GET)
    public Response testMe() {
        return Response.ok("Password works").build();
    }

    @RequestMapping(path="/forgot", method= RequestMethod.GET)
    public Response forgotPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<String> allCellphones = userDAO.findAllCellphones();
            String cellphone = request.getParameter("cellphone").replaceAll("-", "");
            boolean cellphoneExists = allCellphones.contains(cellphone);
            if (cellphoneExists) {
                User user = userDAO.findUserByCellphone(cellphone);
                String password = user.getPassword();
                String resetPasswordLink = "/miBudget/password-reset";
                // email password
                StringBuffer message = new StringBuffer();
                message.append("Hi there, ").append(user.getFirstName()).append(", ").append("\\n");
                message.append("We have recovered your password: ").append(password).append("\\n");
                message.append("Please try to log back in. If that still doesn't work, you can click the following link to reset your password: ").append("\\n");
                message.append(resetPasswordLink);
                Email forgotPasswordEmail = new Email("from", user.getEmail(), "Recovered Password", message.toString());
                emailUtility.sendEmail(forgotPasswordEmail);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().append("Success: Email sent");
                response.getWriter().flush();
                return Response.ok("Email sent").build();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().append("Fail: No cellphone found");
                response.getWriter().flush();
                return Response.status(Response.Status.BAD_REQUEST).entity("No cellphone found").build();
            }
        } catch (Exception e) {
            LOGGER.info("Exception e:" + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().append("Failed: ").append(e.getMessage());
            response.getWriter().flush();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }
    }

}
