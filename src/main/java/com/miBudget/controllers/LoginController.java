package com.miBudget.controllers;

import com.miBudget.entities.*;
import com.miBudget.core.MiBudgetState;
import com.miBudget.utilities.Constants;
import com.miBudget.utilities.DateAndTimeUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController extends MiBudgetController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    public LoginController() {super();}

    @RequestMapping(path="/", method=RequestMethod.GET)
    public void begin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getServletContext().getRequestDispatcher("/index.html").forward(request, response);
    }

    @RequestMapping(path="/test", method=RequestMethod.GET)
    @ResponseBody
    public void testMe() {
        Response.ok("Login works").build();
    }

    @RequestMapping(path= "/", method=RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(Constants.start);
        LOGGER.info("Inside LoginController:login");
        String cellphone = request.getParameter("cellphone");
        String password = request.getParameter("password");
        boolean loginCredentials = false;
        List<User> allUsersList = null;
        HttpSession session = request.getSession(false);
        //Instant instantNow = Instant.now();

        allUsersList = MiBudgetState.getMiBudgetDAOImpl().getAllUsers();
        User loginUser = new User(cellphone, password);
        LOGGER.info("loginUser: " + loginUser);

        // Validate user
        LOGGER.info("validating user credentials...");
        LOGGER.info("cellphone: " + cellphone);
        LOGGER.info("password: " + password);
        // for every user in the list

        for (User activeUser : allUsersList) {
            if (loginUser.equals(activeUser)) {
                LOGGER.info("loginUser matches activeUser");
                LOGGER.info("Registered user. Logging in");
                loginUser = activeUser;
                loginCredentials = true;
                break;
            } else if (loginUser.getCellphone().equals(activeUser.getCellphone()) &&
                    loginUser.getPassword().equals(activeUser.getPassword()) ) {
                LOGGER.info("loginUser is actively a user... ");
                LOGGER.info("Registered user. Logging in");
                loginUser = activeUser;
                loginCredentials = true;
                break;
            } else {
                LOGGER.info(loginUser + " is not a match to " + activeUser);
            }
        }
        // if logInUser does not have a first name, then they are a new user
        // else, logInUser will be fully populated and this is a returning user
        if (loginCredentials) {
            ArrayList<String> accountIdsList = (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAllAccountsIds(loginUser);
            //loginUser.setAccountIds(accountIdsList);
            //if (loginUser.getCategories().size() == 0) loginUser.createCategories();
            ArrayList<UserItemsObject> allUsersItemsList = MiBudgetState.getItemDAOImpl().getAllUserItems(loginUser);

            // Populate accounts and institutions map
            HashMap<String, ArrayList<Account>> acctsAndInstitutionIdMap = new HashMap<>();
            for (UserItemsObject uiObj : allUsersItemsList) {
                Item item = MiBudgetState.getItemDAOImpl().getItem(uiObj.getItem__id() );
                acctsAndInstitutionIdMap.put(item.getInstitutionId(), MiBudgetState.getAccountDAOImpl().getAllAccountsForItem(uiObj.getItem__id()) );
            }
            int accountsTotal = 0;
            LOGGER.info("acctsAndInstitutionIdMap");
            for(String id : acctsAndInstitutionIdMap.keySet()) {
                LOGGER.info("key: " + id);
                for (Account a : acctsAndInstitutionIdMap.get(id)) {
                    LOGGER.info("value: " + a);
                    accountsTotal++;
                }
            }

            //loginUser.setCategories(miBudgetDAOImpl.getAllCategories(loginUser));

            // Populate errors map

            LOGGER.info("acctsAndInstitutionIdMap size: " + acctsAndInstitutionIdMap.size());
            if (accountsTotal == 0) acctsAndInstitutionIdMap = new HashMap<String, ArrayList<Account>>();
            session = request.getSession(true);
            if (session == null || session.isNew()) {
                LOGGER.info("session is null. setting session");
                LOGGER.info("requestSessionId: " + session.getId());
            } else {
                LOGGER.info("Session already exists... but they're just logging in so get new session");
                session.invalidate();
                session = request.getSession(true);
            }
            // Update time
            ArrayList<String> institutionIdsList = MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(loginUser);
            session.setAttribute("acctsAndInstitutionIdMap", acctsAndInstitutionIdMap);
            session.setAttribute("institutionIdsList", institutionIdsList);
            session.setAttribute("institutionIdsListSize", institutionIdsList.size());
            session.setAttribute("session", session); // just a check
            session.setAttribute("sessionId", session.getId()); // just a check
            session.setAttribute("isUserLoggedIn", true); // just a check
            session.setAttribute("Firstname", loginUser.getFirstName());
            session.setAttribute("Lastname", loginUser.getLastName());
            session.setAttribute("user", loginUser);
            session.setAttribute("accountsSize", accountsTotal);
            session.setAttribute("dateAndTime", DateAndTimeUtility.getDateAndTimeAsStr());
            session.setAttribute("getTransactions", new JSONObject());
            session.setAttribute("transactionsList", new JSONArray());
            session.setAttribute("usersTransactions", new ArrayList<Transaction>()); // meant to be empty at this moment
            session.setAttribute("usersBills", new ArrayList<Transaction>()); // meant to be empty at this moment
            session.setAttribute("change", "Once you finish adding accounts, and creating categories, your budget will appear here.");
            LOGGER.info("Redirecting to Welcome.jsp");
            LOGGER.info(Constants.end);
            // call Profile.doGet here
            //RequestDispatcher dispatcher = request.getRequestDispatcher( "../view/Welcome.jsp" );
            //dispatcher.forward(request, response);
//            request.getServletContext().getRequestDispatcher("/Welcome.jsp")
//                    .forward(request, response);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().append("Success: Redirecting to Welcome.jsp");
        }
        else {
            LOGGER.info("Redirecting to Register.jsp");
            LOGGER.info(Constants.end);
            //RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/Register.jsp" );
            //dispatcher.forward( request, response );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().append("Fail: Redirecting to Register.html");
        }
        response.getWriter().flush();
        //return Response.status(response.getStatus()).entity(response.getWriter().toString()).build();
    }
}
