package com.v1.miBudget.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users_institution_ids")
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
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
		return "UsersInstitutionIdsObject [id=" + id + ", institutionId=" + institutionId + ", userId=" + userId + "]";
	}
	
	

}
