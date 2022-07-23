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
	private Long mainBudgetId;
	@Enumerated(EnumType.ORDINAL)
	private AppType appType; // free or paid
	@Transient
	private Budget budget;

	public User() {}
	
	/**
	 * Used when logging in
	 * @param cellphone
	 * @param password
	 */
	public User(String cellphone, String password) {
		this.cellphone = cellphone;
		this.password = password;
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 * @param email
	 */
	public User(String firstName, String lastName, String cellphone, String password, String email, AppType appType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.appType = appType;
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
