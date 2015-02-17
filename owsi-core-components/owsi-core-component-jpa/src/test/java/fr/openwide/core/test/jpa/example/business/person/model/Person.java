/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.test.jpa.example.business.person.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.test.person.TestMetaModel;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends GenericEntity<Long, Person> {

	private static final long serialVersionUID = -2471930493134125282L;

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String lastName;

	private Date creationDate;

	/**
	 * cf {@link TestMetaModel}
	 */
	private PersonSubTypeA otherPerson;

	/**
	 * cf {@link TestMetaModel}
	 */
	private List<PersonSubTypeA> otherPersonList;

	/**
	 * cf {@link TestMetaModel}
	 */
	private TestEnumeration enumeration;

	/**
	 * cf {@link TestMetaModel}
	 */
	@Enumerated(EnumType.STRING)
	private TestEnumeration enumerationString;

	/**
	 * cf {@link TestMetaModel}
	 */
	@OneToMany
	private Map<TestEnumeration, PersonSubTypeA> enumMap;

	/**
	 * cf {@link TestMetaModel}
	 */
	@OneToMany
	@MapKeyEnumerated(EnumType.STRING)
	private Map<TestEnumeration, PersonSubTypeA> enumMapString;

	/**
	 * cf {@link TestMetaModel}
	 */
	@ElementCollection
	private List<TestEnumeration> enumList;

	/**
	 * cf {@link TestMetaModel}
	 */
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<TestEnumeration> enumListString;

	/**
	 * cf {@link TestMetaModel}
	 */
	@ElementCollection
	private Map<Long, TestEnumeration> enumMapValue;

	/**
	 * cf {@link TestMetaModel}
	 */
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Map<Long, TestEnumeration> enumMapValueString;

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
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public PersonSubTypeA getOtherPerson() {
		return otherPerson;
	}

	public void setOtherPerson(PersonSubTypeA otherPerson) {
		this.otherPerson = otherPerson;
	}

	public TestEnumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(TestEnumeration enumeration) {
		this.enumeration = enumeration;
	}

	public Map<TestEnumeration, PersonSubTypeA> getEnumMap() {
		return enumMap;
	}

	public void setEnumMap(Map<TestEnumeration, PersonSubTypeA> enumMap) {
		this.enumMap = enumMap;
	}

	@Override
	public String getNameForToString() {
		return getDisplayName();
	}

	@Override
	public String getDisplayName() {
		return getLastName() + " " + getFirstName();
	}
	
}
