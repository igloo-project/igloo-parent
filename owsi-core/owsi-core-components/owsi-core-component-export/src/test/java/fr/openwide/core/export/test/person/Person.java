package fr.openwide.core.export.test.person;

import java.util.Date;

public class Person {
	
	String userName;
	
	String firstName;
	
	String lastName;
	
	Date birthDate;
	
	int age;
	
	double size;
	
	double percentage;

	public Person(String userName, String firstName, String lastName,
			Date birthDate, int age, double size, double percentage) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.age = age;
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
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
