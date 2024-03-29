package com.miBudget.entities;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable, Comparable<Location> {

	public Location() {}
	
	public Location(String address, String city, String state, String zipcode) {
		setAddress(address);
		setCity(city);
		setState(state);
		setZipcode(zipcode);
	}
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String zipcode;
	
	private static final long serialVersionUID = 1L;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param address the address to set
	 */
	private void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param city the city to set
	 */
	private void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param state the state to set
	 */
	private void setState(String state) {
		this.state = state;
	}

	/**
	 * @param zipcode the zipcode to set
	 * 
	 * @throws NumberFormatException, for when zipcode is null
	 */
	private void setZipcode(String zipcode) {
		try {
			if (zipcode != null) {
				this.zipcode = zipcode;
			} else throw new NumberFormatException(); 
		} catch (NumberFormatException e) {
			this.zipcode = "00000";
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Location{address=" + address + ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + "}";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(address, city, state, zipcode);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(address, other.address) && Objects.equals(city, other.city)
				&& Objects.equals(state, other.state) && zipcode == other.zipcode;
	}

	@Override
	public int compareTo(Location o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
