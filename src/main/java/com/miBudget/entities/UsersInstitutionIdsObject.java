package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="users_institutions_ids")
public class UsersInstitutionIdsObject implements Serializable {

	public UsersInstitutionIdsObject() {}
	
	public UsersInstitutionIdsObject(String institutionId, int userId) {
		this.institutionId = institutionId;
		this.userId = userId;
	}
	
	public UsersInstitutionIdsObject(int id, String institutionId, int userId) {
		this.id = id;
		this.institutionId = institutionId;
		this.userId = userId;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	int id;
	
	@Column(name="institution_id")
	String institutionId;
	
	@Column(name="user_id")
	int userId;
	
	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;

}
