package com.miBudget.entities;

import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.*;

import com.miBudget.enums.AppType;
import lombok.Data;

import static com.miBudget.enums.AppType.FREE;

@Data
@Entity
@Table(name="users")
public class User {

	@Id
	@SequenceGenerator(name="users_sequence", sequenceName="users_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="users_sequence")
	private Long id;
	private String firstName;
	private String lastName;
	private String cellphone;
	private String password;
	private String email;
	private AppType appType; // free or paid
	@Transient
	private Budget budget;

	public User() {}
	
	/**
	 * If you need to validate a user, you can use this constructor.
	 * @param cellphone
	 * @param password
	 */
	public User(String cellphone, String password) {
		this.cellphone = cellphone;
		this.password = password;
		createCategories();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 */
	public User(String firstName, String lastName, String cellphone, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		createCategories();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 * @param email
	 */
	public User(String firstName, String lastName, String cellphone, String password, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.appType = FREE;
		createCategories();
	}
	
	/**
	 * To create a user with no accounts, please provide in the following order:
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 */
	public User(Long id, String firstName, String lastName, String cellphone, String password, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		createCategories();
	}
	
	/**
	 * To create a user with accounts, please provide in the following order:
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 * @param email
	 * @param accountIds
	 */
	public User(Long id, String firstName, String lastName, String cellphone, String password, String email, ArrayList<String> accountIds) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
	}


	public void createCategories() {
		ArrayList<Category> categoriesList = new ArrayList<>();
		Category morgtageCat = new Category("Mortgage", "USD", 1500.00);
		categoriesList.add(morgtageCat);
		Category utilitiesCat = new Category("Utilities", "USD", 500.00);
		categoriesList.add(utilitiesCat);
		Category transportCat = new Category("Transportation", "USD", 1000.00);
		categoriesList.add(transportCat);
		Category insuranceCat = new Category("Insurance", "USD", 200.00);
		categoriesList.add(insuranceCat);
		Category foodCat = new Category("Food", "USD", 500.00);
		categoriesList.add(foodCat);
		Category subscriptionsCat = new Category("Subscriptions", "USD", 500.00);
		categoriesList.add(subscriptionsCat);
		Category billsCat = new Category("Bill", "USD", 1000.00);
		categoriesList.add(billsCat);
		//this.categories = categoriesList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return getId() == user.getId() || (
				Objects.equals(getCellphone(), user.getCellphone()) &&
				Objects.equals(getPassword(), user.getPassword()) );
	}

}
