package org.iglooproject.test.rest.jersey2.business.person.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class Person extends GenericEntity<Long, Person> {

	private static final long serialVersionUID = -2471930493134125282L;

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String lastName;

	private Instant creationDate;

	public Person() {
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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
	
	public Instant getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

}
