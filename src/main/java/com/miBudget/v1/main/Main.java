package com.miBudget.v1.main;

import java.util.List;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;
import com.miBudget.v1.daoimplementations.ItemDAOImpl;
import com.miBudget.v1.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.v1.entities.User;

public class Main {

	private static MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	private static AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	private static ItemDAOImpl itemDAOImpl = new ItemDAOImpl();
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<User> allUsersList = null;
		allUsersList = miBudgetDAOImpl.getAllUsers();
		
		User loginUser = new User("8324279384", "1");
		
		for (User user : allUsersList) {
			if (loginUser.equals(user)) {
				System.out.println(loginUser + " matches " + user);
				System.out.println("Registered user. Logging in");
				loginUser = user;
			} else if (loginUser.getCellphone().equals(user.getCellphone()) &&
					   loginUser.getPassword().equals(user.getPassword()) ) {
				System.out.println(loginUser + " matches " + user);
				System.out.println("Registered user. Logging in");
				loginUser = user;
			} else {
				System.out.println(loginUser + " is not a match to " + user);
			}
		}
		System.out.println(loginUser);
		
	}

}
