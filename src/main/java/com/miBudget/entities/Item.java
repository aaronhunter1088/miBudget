package com.miBudget.entities;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import java.io.Serializable;

@Data
@Entity
@Table(name="items")
public class Item implements Serializable {

	@Id
	@SequenceGenerator(name="items_sequence", sequenceName="items_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="items_sequence")
	private Long id;
	private String institutionId;
	private String itemId;
	private String accessToken;
	private String bankName;
	private Long userId;

	public Item() {}
	public Item(String itemId, String accessToken) {
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	public Item(Long id, String itemId, String accessToken) {
		this.id = id;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	public Item(String itemId, String accessToken, String institutionId, String bankName, Long userId) {
		if (StringUtils.isNotEmpty(itemId) && StringUtils.isNotEmpty(accessToken) &&
			StringUtils.isNotEmpty(institutionId) && StringUtils.isNotEmpty(bankName) &&
			userId != 0.0) {
			this.itemId = itemId;
			this.accessToken = accessToken;
			this.institutionId = institutionId;
			this.bankName = bankName;
			this.userId = userId;
		}
		
	}
	public Item(long id, String itemId, String accessToken, String institutionId) {
		this.id = id;
		this.institutionId = institutionId;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
}