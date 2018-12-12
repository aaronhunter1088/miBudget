package com.miBudget.v1.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users_items")
public class UsersItemsObject implements Serializable {

	public UsersItemsObject() {}
	
	public UsersItemsObject(int id, int itemTableId, int userId) {
		this.id = id;
		this.itemTableId = itemTableId;
		this.userId = userId;
	}
	
	public UsersItemsObject(int itemTableId, int userId) {
		this.itemTableId = itemTableId;
		this.userId = userId;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	int id;
	
	@Column(name="item_table_id")
	int itemTableId;
	
	@Column(name="user_id")
	int userId;
	
	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItemTableId() {
		return itemTableId;
	}

	public void setItemTableId(int itemTableId) {
		this.itemTableId = itemTableId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UsersItemsObject [id=" + id + ", itemTableId=" + itemTableId + ", userId=" + userId + "]";
	}

}
