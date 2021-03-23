package com.miBudget.v1.serviceimplementations;

import java.util.List;

import org.hibernate.Session;

import com.miBudget.v1.services.UserServices;

import javax.servlet.http.HttpSession;

public class UserServiceImpl implements UserServices {
	
	@Override
	public List<String> getAllUsersByCellphone(HttpSession session) throws Exception {
		return null;
		//return mibudgetuserDAOImpl.getAllUsersByCellphone(session);
	}
	
	

}
