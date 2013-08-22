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

package fr.openwide.core.test.person;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.jpa.example.business.person.dao.PersonDao;
import fr.openwide.core.test.jpa.example.business.person.dao.PersonReferenceDao;
import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.model.PersonReference;
import fr.openwide.core.test.jpa.example.business.person.model.PersonSubTypeA;
import fr.openwide.core.test.jpa.example.business.person.model.PersonSubTypeB;
import fr.openwide.core.test.jpa.example.business.person.model.Person_;

public class TestGenericDao extends AbstractJpaCoreTestCase {

	@Autowired
	private PersonDao personDao;

	@Autowired
	private PersonReferenceDao personReferenceDao;

	@Test
	public void testGet() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);

		Person person1 = (Person) personDao.getById(Person.class, person.getId());
		Person person2 = personDao.getByField(Person_.lastName, "Lastname");
		Person person3 = personDao.getById(person.getId());

		Assert.assertTrue(person.equals(person1));
		Assert.assertTrue(person.equals(person2));
		Assert.assertTrue(person.equals(person3));
	}

	@Test
	public void testSubTypeGet() throws ServiceException, SecurityServiceException {
		Person personA = new PersonSubTypeA("Firstname", "A", "DATA");
		Person personB = new PersonSubTypeB("Firstname", "B", 3);
		personService.create(personA);
		personService.create(personB);

		Person personA1 = personDao.getById(personA.getId());
		Person personA2 = personDao.getById(Person.class, personA.getId());
		PersonSubTypeA personA3 = personDao.getById(PersonSubTypeA.class, personA.getId());
		PersonSubTypeB personA4 = personDao.getById(PersonSubTypeB.class, personA.getId());
		Person personA5 = personDao.getByField(Person_.lastName, "A");

		Person personB1 = personDao.getById(personB.getId());
		Person personB2 = personDao.getById(Person.class, personB.getId());
		PersonSubTypeB personB3 = personDao.getById(PersonSubTypeB.class, personB.getId());
		PersonSubTypeA personB4 = personDao.getById(PersonSubTypeA.class, personB.getId());
		Person personB5 = personDao.getByField(Person_.lastName, "B");

		Assert.assertTrue(personA.equals(personA1));
		Assert.assertTrue(personA.equals(personA2));
		Assert.assertTrue(personA.equals(personA3));
		Assert.assertNull(personA4);
		Assert.assertTrue(personA.equals(personA5));
		
		Assert.assertTrue(personB.equals(personB1));
		Assert.assertTrue(personB.equals(personB2));
		Assert.assertTrue(personB.equals(personB3));
		Assert.assertNull(personB4);
		Assert.assertTrue(personB.equals(personB5));
		
		cleanAll();
		Assert.assertEquals(new Long(0), personService.count());
	}

	@Test
	public void testPolymorphicSubTypeGet() throws ServiceException, SecurityServiceException {
		Person person = new PersonSubTypeA("Firstname", "A", "DATA");
		PersonReference personReference = new PersonReference(person);
		personService.save(person);
		personReferenceService.save(personReference);
		
		// Vidage de la session
		personService.flush();
		personService.clear();
		
		PersonReference personReference1 = personReferenceDao.getById(personReference.getId());
		Person person1 = personReference1.getPerson();
		Assert.assertEquals(person, person1);
		Assert.assertFalse(person1 instanceof PersonSubTypeA); // person1 devrait être chargé en session en tant que proxy de Person
		
		Person person2 = personDao.getById(PersonSubTypeA.class, person.getId());
		Assert.assertEquals(person, person2);
		Assert.assertTrue(person2 instanceof PersonSubTypeA); // Chargement en session SANS proxy
	}

	@Test
	public void testSaveDelete() {
		Person person = new Person("Firstname", "Lastname");
		personDao.save(person);
		personService.flush();
		Assert.assertTrue(personService.list().contains(person));

		personDao.delete(person);
		personService.flush();
		Assert.assertFalse(personService.list().contains(person));
	}

	@Test
	public void testUpdate() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		Assert.assertEquals("Firstname", personService.getById(person.getId()).getFirstName());

		person.setFirstName("NewFirstname");
		personDao.update(person);
		Assert.assertEquals("NewFirstname", personService.getById(person.getId()).getFirstName());
	}

	@Test
	public void testRefresh() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);

		person.setFirstName("AAAAA");
		Assert.assertEquals("AAAAA", person.getFirstName());

		personDao.refresh(person);
		Assert.assertEquals("Firstname", person.getFirstName());

		Person person1 = new Person("Firstname", "Lastname");

		try {
			personDao.refresh(person1);
			Assert.fail("Faire un refresh sur un objet avec un identifiant null doit lever une exception");
		} catch (IllegalArgumentException e) {
		}

		personService.create(person1);
		personService.delete(person1);

		try {
			personDao.refresh(person1);
			Assert.fail("Faire un refresh sur un objet non persisté doit lever une exception");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testLists() throws ServiceException, SecurityServiceException {
		List<Person> emptyList = personDao.list();
		Assert.assertEquals(0, emptyList.size());
		
		List<Person> emptyListByField = personDao.listByField(Person_.lastName, "AAAA");
		Assert.assertEquals(0, emptyListByField.size());
		
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		Person person2 = new Person("Firstname2", "AAAA");
		personService.create(person2);
		Person person3 = new Person("Firstname3", "AAAA");
		personService.create(person3);
		Person person4 = new Person("Firstname4", "Lastname4");
		personService.create(person4);
		
		List<Person> list = personDao.list();
		
		Assert.assertEquals(4, list.size());
		Assert.assertTrue(list.contains(person1));
		Assert.assertTrue(list.contains(person2));
		Assert.assertTrue(list.contains(person3));
		Assert.assertTrue(list.contains(person4));
		
		List<Person> listByField = personDao.listByField(Person_.lastName, "AAAA");
		
		Assert.assertEquals(2, listByField.size());
		Assert.assertTrue(listByField.contains(person2));
		Assert.assertTrue(listByField.contains(person3));
	}

	@Test
	public void testCounts() throws ServiceException, SecurityServiceException {
		Assert.assertEquals(new Long(0), personDao.count());
		Assert.assertEquals(new Long(0), personDao.countByField(Person_.lastName, "AAAA"));

		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		Person person2 = new Person("Firstname2", "AAAA");
		personService.create(person2);
		Person person3 = new Person("Firstname3", "AAAA");
		personService.create(person3);
		Person person4 = new Person("Firstname4", "Lastname4");
		personService.create(person4);

		Assert.assertEquals(new Long(4), personDao.count());
		Assert.assertEquals(new Long(2), personDao.countByField(Person_.lastName, "AAAA"));
	}
	
	@Before
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
	}
	
	@After
	@Override
	public void close() throws ServiceException, SecurityServiceException {
		super.close();
	}
}
