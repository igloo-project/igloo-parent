package fr.openwide.export.excel.example.person;

import java.util.Date;

import fr.openwide.core.commons.util.CloneUtils;

public class Person {
	
	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthDate;
	
	private int size;
	
	private double percentage;

	public Person(String userName, String firstName, String lastName,
			Date birthDate, int size, double percentage) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = CloneUtils.clone(birthDate);
		this.size = size;
		this.percentage = percentage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return CloneUtils.clone(birthDate);
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = CloneUtils.clone(birthDate);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
