package com.v1.miBudget.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="users_items")
public class UserItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	public int id;
	@Column(name="item_table_id")
	int item_table_id;
	
	@Column(name="user_id")
	public int user_id = 0;
	
	public UserItem(int item_table_id, int user_id) {
		this.user_id = user_id;
		this.item_table_id = item_table_id;
	}
	
	public UserItem() {}

	/**
	 * @return the item_id
	 */
	public int getItem_id() {
		return item_table_id;
	}

	/**
	 * @param item_id the item_id to set
	 */
	public void setItem_id(int item_id) {
		this.item_table_id = item_id;
	}

	/**
	 * @return the user_id
	 */
	public int getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
