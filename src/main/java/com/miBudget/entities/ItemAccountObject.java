package com.miBudget.entities;

import lombok.Data;

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

@Data
@Entity
@Table(name="item_accounts")
public class ItemAccountObject implements Serializable {

	public ItemAccountObject() {}
	
	public ItemAccountObject(int item__id, String accountId) {
		this.item__id = item__id;
		this.accountId = accountId;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@Column(name="item__id")
	private int item__id;
	
	@Column(name="account_id")
	private String accountId;
	
	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;

}
