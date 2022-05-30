package com.miBudget.serviceimplementations;

import java.util.List;

import com.miBudget.services.UserServices;

import javax.servlet.http.HttpSession;

public class UserServiceImpl implements UserServices {
	
	@Override
	public List<String> getAllUsersByCellphone(HttpSession session) throws Exception {
		return null;
		//return mibudgetuserDAOImpl.getAllUsersByCellphone(session);
	}
	
	

}
