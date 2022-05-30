package com.miBudget.main;

import java.util.List;

import com.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	private static MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	//private static AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	//private static ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
    
	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(Main.class);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<User> allUsersList = null;
		allUsersList = miBudgetDAOImpl.getAllUsers();
		
		User loginUser = new User("8324279384", "1");
		
		for (User user : allUsersList) {
			if (loginUser.equals(user)) {
				LOGGER.info("loginUser matches " + user.getFirstName());
				LOGGER.info(user.getFirstName() + " is a registered user. Logging user in...");
				loginUser = user;
				break;
			} else if (loginUser.getCellphone().equals(user.getCellphone()) &&
					   loginUser.getPassword().equals(user.getPassword()) ) {
				LOGGER.info("loginUser matches " + user.getFirstName());
				LOGGER.info(user.getFirstName() + " is a registered user. Logging user in...");
				loginUser = user;
				break;
			} else {
				LOGGER.info("loginUser is not a match to " + user.getFirstName());
			}
		}
		LOGGER.info(loginUser);
	}

}
