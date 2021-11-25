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

import java.util.List;

import javax.persistence.PersistenceException;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.PersonSubTypeA;
import org.iglooproject.test.business.person.model.PersonSubTypeB;
import org.iglooproject.test.business.person.service.IPersonService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestGenericService extends AbstractJpaCoreTestCase {

	@Autowired
	IPersonService personService;

	@Test
	public void testSaveCreate() throws ServiceException, SecurityServiceException {
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		Assert.assertNotNull(person1.getId());

		Person person2 = new Person("Firstname2", "Lastname2");
		personService.create(person2);
		Assert.assertNotNull(person2.getId());
	}

	@Test
	public void testDelete() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		Assert.assertTrue(personService.list().contains(person));

		personService.delete(person);
		Assert.assertFalse(personService.list().contains(person));

		/*
		 * La suppression d'un objet non persisté ne lève aucune exception
		 */
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.delete(person1);
	}

	@Test
	public void testUpdate() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		Assert.assertEquals("Firstname", personService.getById(person.getId()).getFirstName());

		person.setFirstName("NewFirstname");
		personService.update(person);
		Assert.assertEquals("NewFirstname", personService.getById(person.getId()).getFirstName());
		
		Person person1 = new Person("Firstname1", "Lastname1");
		try {
			personService.update(person1);
			Assert.fail("Faire un update sur un objet non persisté doit lever une exception");
		} catch (PersistenceException e) {	
		}
	}

	@Test
	public void testRefresh() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);

		person.setFirstName("AAAAA");
		Assert.assertEquals("AAAAA", person.getFirstName());

		personService.refresh(person);
		Assert.assertEquals("Firstname", person.getFirstName());

		Person person1 = new Person("Firstname", "Lastname");

		try {
			personService.refresh(person1);
			Assert.fail("Faire un refresh sur un objet avec un identifiant null doit lever une exception");
		} catch (IllegalArgumentException e) {
		}

		personService.create(person1);
		personService.delete(person1);

		try {
			personService.refresh(person1);
			Assert.fail("Faire un refresh sur un objet non persisté doit lever une exception");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testGet() throws ServiceException, SecurityServiceException {
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);

		personService.count();
		Person person1 = personService.getById(Person.class, person.getId());
		Person person2 = personService.getById(person.getId());

		Assert.assertTrue(person.equals(person1));
		Assert.assertTrue(person.equals(person2));
		
		cleanAll();
		Assert.assertEquals(Long.valueOf(0), personService.count());
		
		person = new Person("Firstname", "Lastname");
		personService.create(person);
		cleanAll();
		person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		person = new Person("Firstname", "Lastname");
		personService.create(person);
		
		Assert.assertEquals(Long.valueOf(2), personService.count());
	}

	@Test
	public void testSubTypeGet() throws ServiceException, SecurityServiceException {
		Person personA = new PersonSubTypeA("Firstname", "A", "DATA");
		Person personB = new PersonSubTypeB("Firstname", "B", 3);
		personService.create(personA);
		personService.create(personB);

		personService.count();
		Person personA1 = personService.getById(personA.getId());
		Person personA2 = personService.getById(Person.class, personA.getId());
		PersonSubTypeA personA3 = personService.getById(PersonSubTypeA.class, personA.getId());
		PersonSubTypeB personA4 = personService.getById(PersonSubTypeB.class, personA.getId());

		Person personB1 = personService.getById(personB.getId());
		Person personB2 = personService.getById(Person.class, personB.getId());
		PersonSubTypeB personB3 = personService.getById(PersonSubTypeB.class, personB.getId());
		PersonSubTypeA personB4 = personService.getById(PersonSubTypeA.class, personB.getId());

		Assert.assertTrue(personA.equals(personA1));
		Assert.assertTrue(personA.equals(personA2));
		Assert.assertTrue(personA.equals(personA3));
		Assert.assertNull(personA4);
		
		Assert.assertTrue(personB.equals(personB1));
		Assert.assertTrue(personB.equals(personB2));
		Assert.assertTrue(personB.equals(personB3));
		Assert.assertNull(personB4);
		
		cleanAll();
		Assert.assertEquals(Long.valueOf(0), personService.count());
	}
	
	@Test
	public void testLists() throws ServiceException, SecurityServiceException {
		cleanAll();
		
		List<Person> emptyList = personService.list();
		
		Assert.assertEquals(0, emptyList.size());

		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		
		List<Person> list = personService.list();
		
		Assert.assertTrue(list.contains(person));
		Assert.assertTrue(list.contains(person1));
		Assert.assertEquals(1, (long) personService.countByLastName("Lastname"));
		Assert.assertEquals(1, (long) personService.countByLastName("Lastname1"));
	}
	
	@Test
	public void testCount() throws ServiceException, SecurityServiceException {
		Assert.assertEquals(Long.valueOf(0), personService.count());
		
		Person person = new Person("Firstname", "Lastname");
		personService.create(person);
		Person person1 = new Person("Firstname1", "Lastname1");
		personService.create(person1);
		
		Assert.assertEquals(Long.valueOf(2), personService.count());
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
