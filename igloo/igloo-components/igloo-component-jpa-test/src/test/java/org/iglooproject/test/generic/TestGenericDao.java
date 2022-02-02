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

package org.iglooproject.test.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.person.dao.IPersonDao;
import org.iglooproject.test.business.person.dao.IPersonReferenceDao;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.PersonReference;
import org.iglooproject.test.business.person.model.PersonSubTypeA;
import org.iglooproject.test.business.person.model.PersonSubTypeB;
import org.iglooproject.test.business.person.model.QPerson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestGenericDao extends AbstractJpaCoreTestCase {

	@Autowired
	private IPersonDao personDao;

	@Autowired
	private IPersonReferenceDao personReferenceDao;

	@Test
	void testGet() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		
		{
			Person person1 = (Person) personDao.getById(Person.class, person.getId());
			Person person2 = personDao.getById(person.getId());
			
			assertTrue(person.equals(person1));
			assertTrue(person.equals(person2));
		}
		
		{
			Person person1 = (Person) personDao.getById(Person.class, person.getId());
			Person person2 = personDao.getById(person.getId());
			
			assertTrue(person.equals(person1));
			assertTrue(person.equals(person2));
		}
	}

	@Test
	void testSubTypeGet() throws ServiceException, SecurityServiceException {
		Person personA = new PersonSubTypeA("Firstname", "A", "DATA");
		Person personB = new PersonSubTypeB("Firstname", "B", 3);
		personService.create(personA);
		personService.create(personB);

		Person personA1 = personDao.getById(personA.getId());
		Person personA2 = personDao.getById(Person.class, personA.getId());
		PersonSubTypeA personA3 = personDao.getById(PersonSubTypeA.class, personA.getId());
		PersonSubTypeB personA4 = personDao.getById(PersonSubTypeB.class, personA.getId());

		Person personB1 = personDao.getById(personB.getId());
		Person personB2 = personDao.getById(Person.class, personB.getId());
		PersonSubTypeB personB3 = personDao.getById(PersonSubTypeB.class, personB.getId());
		PersonSubTypeA personB4 = personDao.getById(PersonSubTypeA.class, personB.getId());

		assertTrue(personA.equals(personA1));
		assertTrue(personA.equals(personA2));
		assertTrue(personA.equals(personA3));
		assertNull(personA4);
		
		assertTrue(personB.equals(personB1));
		assertTrue(personB.equals(personB2));
		assertTrue(personB.equals(personB3));
		assertNull(personB4);
		
		cleanAll();
		assertEquals(Long.valueOf(0), personService.count());
		assertEquals(Long.valueOf(0), personDao.count(QPerson.person));
	}

	@Test
	void testPolymorphicSubTypeGet() throws ServiceException, SecurityServiceException {
		Person person = new PersonSubTypeA("Firstname", "A", "DATA");
		PersonReference personReference = new PersonReference(person);
		personService.create(person);
		personReferenceService.create(personReference);
		
		// Vidage de la session
		personService.flush();
		personService.clear();
		
		PersonReference personReference1 = personReferenceDao.getById(personReference.getId());
		Person person1 = personReference1.getPerson();
		assertEquals(person, person1);
		assertFalse(person1 instanceof PersonSubTypeA); // person1 devrait être chargé en session en tant que proxy de Person
		
		Person person2 = personDao.getById(PersonSubTypeA.class, person.getId());
		assertEquals(person, person2);
		assertTrue(person2 instanceof PersonSubTypeA); // Chargement en session SANS proxy
	}

	@Test
	void testCreateDelete() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		personService.flush();
		assertTrue(personService.list().contains(person));

		personService.delete(person);
		personService.flush();
		assertFalse(personService.list().contains(person));
	}

	@Test
	void testUpdate() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		assertEquals("Firstname", personService.getById(person.getId()).getFirstName());

		person.setFirstName("NewFirstname");
		personDao.update(person);
		assertEquals("NewFirstname", personService.getById(person.getId()).getFirstName());
	}

	@Test
	void testRefresh() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);

		person.setFirstName("AAAAA");
		assertEquals("AAAAA", person.getFirstName());

		personService.refresh(person);
		assertEquals("Firstname", person.getFirstName());

		Person person1 = new Person("Firstname", "Lastname");

		try {
			personService.refresh(person1);
			fail("Faire un refresh sur un objet avec un identifiant null doit lever une exception");
		} catch (IllegalArgumentException e) {
		}

		personService.create(person1);
		personService.delete(person1);

		try {
			personService.refresh(person1);
			fail("Faire un refresh sur un objet non persisté doit lever une exception");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	void testLists() throws ServiceException, SecurityServiceException {
		{
			List<Person> emptyList = personDao.list();
			assertEquals(0, emptyList.size());
		}
		
		{
			List<Person> emptyList = personDao.list(QPerson.person);
			assertEquals(0, emptyList.size());
		}
		
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		Person person2 = new Person("Firstname2", "AAAA");
		personService.create(person2);
		Person person3 = new Person("Firstname3", "AAAA");
		personService.create(person3);
		Person person4 = new Person("Firstname4", "Lastname4");
		personService.create(person4);
		
		{
			List<Person> list = personDao.list();
			
			assertEquals(4, list.size());
			assertTrue(list.contains(person1));
			assertTrue(list.contains(person2));
			assertTrue(list.contains(person3));
			assertTrue(list.contains(person4));
		}
		
		{
			List<Person> list = personDao.list(QPerson.person);
			
			assertEquals(4, list.size());
			assertTrue(list.contains(person1));
			assertTrue(list.contains(person2));
			assertTrue(list.contains(person3));
			assertTrue(list.contains(person4));
		}
	}

	@Test
	void testCounts() throws ServiceException, SecurityServiceException {
		{
			assertEquals(Long.valueOf(0), personDao.count());
		}
		
		{
			assertEquals(Long.valueOf(0), personDao.count(QPerson.person));
			assertEquals(Long.valueOf(0), personDao.countByField(QPerson.person, QPerson.person.lastName, "AAAA"));
		}
		
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		Person person2 = new Person("Firstname2", "AAAA");
		personService.create(person2);
		Person person3 = new Person("Firstname3", "AAAA");
		personService.create(person3);
		Person person4 = new Person("Firstname4", "Lastname4");
		personService.create(person4);
		
		{
			assertEquals(Long.valueOf(4), personDao.count());
		}
		
		{
			assertEquals(Long.valueOf(4), personDao.count(QPerson.person));
			assertEquals(Long.valueOf(2), personDao.countByField(QPerson.person, QPerson.person.lastName, "AAAA"));
		}
	}
	
	@BeforeEach
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
	}
	
	@AfterEach
	@Override
	public void close() throws ServiceException, SecurityServiceException {
		super.close();
	}
}
