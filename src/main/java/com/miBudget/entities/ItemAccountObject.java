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

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long itemId;
	private String accountId;

	public ItemAccountObject() {}
	public ItemAccountObject(Long itemId, String accountId) {
		this.itemId = itemId;
		this.accountId = accountId;
	}
}
