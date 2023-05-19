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

import org.iglooproject.jpa.config.spring.IglooJpaConfiguration;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.JpaTestBusinessPackage;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.service.IPersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

class TestGenericEntity extends AbstractJpaCoreTestCase {

	@Autowired
	protected IPersonService personService;

	@Test
	public void testGenericEntity() throws ServiceException, SecurityServiceException {
		Person person = new Person("FirstName", "LastName");
		
		assertNull(person.getId());
		assertTrue(person.isNew());

		personService.create(person);

		assertFalse(person.isNew());
		
		Long oldId = person.getId();
		
		person.setId(2L);
		assertEquals(2, person.getId().intValue());

		person.setId(oldId);

		Person person1 = new Person("FirstName1", "LastName1");
		personService.create(person1);

		Person person2 = personService.getById(person.getId());

		assertFalse(person.equals(person1));
		assertTrue(person.equals(person2));

		Person person4 = person;

		assertFalse(person.compareTo(person1) == 0);
		assertTrue(person.compareTo(person4) == 0);

		assertEquals("LastName", person.getLastName());
		assertEquals("FirstName", person.getFirstName());
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
