package com.miBudget.v1.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usersinstitutionsids")
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
	@Column(name="userinstitutionid")
	int id;
	
	@Column(name="institutionid")
	String institutionId;
	
	@Column(name="userid")
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
