package com.miBudget.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miBudget.daoimplementations.MiBudgetDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.entities.User;


/**
 * Servlet implementation class CheckInstitutionIds
 */
@WebServlet("/CheckInstitutionIds")
public class CheckInstitutionIds extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	
	private static Logger LOGGER = null;
	static  {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(CheckInstitutionIds.class);
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckInstitutionIds() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		LOGGER.info("\nInside CheckInstitutionIds doPost");
		boolean exit = checkInstitutionIds(request, (User)request.getSession(false).getAttribute("User") );
		if (exit) {
			LOGGER.info("Finished with CheckInstitutionIds doPost");
			LOGGER.info("Institution check failed.");
			// set response
			return;
		} else {
			LOGGER.info("Finished with CheckInstitutionIds doPost");
			LOGGER.info("Institution check passed.");
			// set response
		}
	}
	
	public boolean checkInstitutionIds(HttpServletRequest request, User user) {
		String institution_idIncoming = request.getParameter("institution_id");
		ArrayList<String> institutionIdsList = (ArrayList<String>) miBudgetDAOImpl.getAllInstitutionIdsFromUser(user);
		boolean exit = false;
		Iterator<String> iter = institutionIdsList.iterator();
		while (iter.hasNext()) {
			String id = iter.next();
			if (id.equals(institution_idIncoming)) {
				LOGGER.info(institution_idIncoming + " has already been added. We cannot add it again.");
				// exit = true;
				return exit = true;
			}
			LOGGER.info(id + " - This id did not match the one selected.");
		}
		return exit;
	}

}
