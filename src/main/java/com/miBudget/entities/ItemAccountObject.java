package com.miBudget.entities;

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
@Table(name="itemsaccounts")
public class ItemAccountObject implements Serializable {

	public ItemAccountObject() {}
	
	public ItemAccountObject(int itemTableId, String accountId) {
		this.itemTableId = itemTableId;
		this.accountId = accountId;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="itemaccountid")
	private int id;
	
	@Column(name="itemtableid")
	private int itemTableId;
	
	@Column(name="accountid")
	private String accountId;
	
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
		return "ItemAccountObject [itemsaccountid=" + id + ", itemTableId=" + itemTableId + ", accountId=" + accountId + "]";
	}
	
	
	
}
