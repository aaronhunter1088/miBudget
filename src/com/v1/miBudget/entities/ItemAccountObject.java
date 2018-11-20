package com.v1.miBudget.entities;

import java.io.Serializable;

/*
 * id : item_table_id : account_id
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="items_accounts")
public class ItemAccountObject implements Serializable {

	public ItemAccountObject() {}
	
	public ItemAccountObject(int itemTableId, String accountId) {
		this.itemTableId = itemTableId;
		this.accountId = accountId;
	}
	
	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@Column(name="item_table_id")
	private int itemTableId;
	
	@Column(name="account_id")
	private String accountId;

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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ItemAccountObject [id=" + id + ", itemTableId=" + itemTableId + ", accountId=" + accountId + "]";
	}
	
	
	
}
