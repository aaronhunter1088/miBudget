package com.miBudget.v1.main;

import java.util.ArrayList;

public class NewLogicTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Category> categoriesList = new ArrayList<>();
		String name = "Rent";
		Double amount = 2000.00;
		
		Category userCategory;
		
		if (name.equals(Categories.Rent.getName())) {
			userCategory = new Category(name, amount);
			categoriesList.add(userCategory);
		}
		
		for (Category category : categoriesList) {
			System.out.println(category.toString());
		}
		
	}

}
