package com.miBudget.entities;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

import javax.persistence.Column;

@Data
@Entity
@Table(name="items")
public class Item implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	public String institutionId;
	public String itemId;
	public String accessToken;

	public Item() {}
	public Item(String itemId, String accessToken) {
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	public Item(long id, String itemId, String accessToken) {
		this.id = id;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	public Item(String itemId, String accessToken, String institutionId) {
		if (StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(itemId) && StringUtils.isNotEmpty(institutionId)) {
			this.itemId = itemId;
			this.accessToken = accessToken;
			this.institutionId = institutionId;
		} else {
			throw new NullPointerException("itemId: " + itemId + "\naccessToken: " + accessToken + "\ninstituionId: " + institutionId);
			// Fail here
		}
		
	}
	public Item(long id, String itemId, String accessToken, String institutionId) {
		this.id = id;
		this.institutionId = institutionId;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
}