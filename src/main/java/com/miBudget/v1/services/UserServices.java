package com.miBudget.v1.services;

import java.util.List;

import org.hibernate.Session;

import javax.servlet.http.HttpSession;

public interface UserServices {

	/**
	 * Connects to MiBudgetDAO.
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllUsersByCellphone(HttpSession session) throws Exception;
}
