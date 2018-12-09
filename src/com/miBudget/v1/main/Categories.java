package com.miBudget.v1.main;

public enum Categories {
	Rent("Rent"), Mortgage("Mortgage"), CarPayment("CarPayment"), Insurance("Insurance"),
	Electricity("Electricity"), WaterAndSewage("Water & Sewage"), Trash("Trash"), Gas("Gas"),
	InternetAndTV("Internet & TV"), Cellphone("Cellphone"), Food("Food"), Custom();
	
	private String name;
	
	Categories(String name) {
		this.name = name;
	}
	Categories() {}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
