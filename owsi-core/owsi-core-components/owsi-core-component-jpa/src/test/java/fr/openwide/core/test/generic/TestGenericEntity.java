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

package fr.openwide.core.test.generic;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.base.Function;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.business.person.model.Person;
import fr.openwide.core.test.business.person.service.IPersonService;

public class TestGenericEntity extends AbstractJpaCoreTestCase {
	
	@Autowired
	protected IPersonService personService;
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	private TransactionTemplate writeTransactionTemplate;
	
	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute writeTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
	}

	@Test
	public void testGenericEntity() throws ServiceException, SecurityServiceException {
		Person person = new Person("FirstName", "LastName");
		
		Assert.assertNull(person.getId());
		Assert.assertTrue(person.isNew());

		personService.create(person);

		Assert.assertFalse(person.isNew());
		
		Long oldId = person.getId();
		
		person.setId(2L);
		Assert.assertEquals(2, person.getId().intValue());

		person.setId(oldId);

		Person person1 = new Person("FirstName1", "LastName1");
		personService.create(person1);

		Person person2 = personService.getById(person.getId());

		Assert.assertFalse(person.equals(person1));
		Assert.assertTrue(person.equals(person2));

		Person person4 = person;

		Assert.assertFalse(person.compareTo(person1) == 0);
		Assert.assertTrue(person.compareTo(person4) == 0);

		Assert.assertEquals("LastName FirstName", person.getDisplayName());
		Assert.assertEquals("LastName FirstName", person.getNameForToString());
	}
	
	@Test
	public void testLimitedHashCodeAndEqualityConstraints() throws InterruptedException {
		Person person = new Person("TestHashCode", "TestHashCode");
		assertLimitedHashCodeAndEqualityConstraints(Person.class, person);
	}
	
	@Test
	public void testFullHashCodeAndEqualityConstraints() throws InterruptedException {
		Person person = new Person("TestHashCode", "TestHashCode");
		assertFullHashCodeAndEqualityConstraints(Person.class, person);
	}
	
	/*
	 * Adapted from https://vladmihalcea.com/2016/06/06/how-to-implement-equals-and-hashcode-using-the-entity-identifier/
	 * with no constraint regarding detached entities or multiple persistence contexts.
	 */
	private <T extends GenericEntity<?, ?>> void assertLimitedHashCodeAndEqualityConstraints(
			final Class<T> clazz, final T entity)
			throws InterruptedException {
		final Set<T> tuples = new HashSet<>();

		assertFalse(tuples.contains(entity));
		tuples.add(entity);
		assertTrue(tuples.contains(entity));

		doInJPA(new Function<EntityManager, Void>() {
			@Override
			public Void apply(EntityManager entityManager) {
				entityManager.persist(entity);
				entityManager.flush();
				assertTrue("The entity is found after it's persisted", tuples.contains(entity));
				return null;
			}
		});
		
		entityManagerReset();
	}
	
	/*
	 * Source: https://vladmihalcea.com/2016/06/06/how-to-implement-equals-and-hashcode-using-the-entity-identifier/
	 */
	private <T extends GenericEntity<?, ?>> void assertFullHashCodeAndEqualityConstraints(final Class<T> clazz, final T entity)
			throws InterruptedException {
		final Set<T> tuples = new HashSet<>();

		assertFalse(tuples.contains(entity));
		tuples.add(entity);
		assertTrue(tuples.contains(entity));

		doInJPA(new Function<EntityManager, Void>() {
			@Override
			public Void apply(EntityManager entityManager) {
				entityManager.persist(entity);
				entityManager.flush();
				assertTrue("The entity is found after it's persisted", tuples.contains(entity));
				return null;
			}
		});
			

		// The entity is found after the entity is detached
		assertTrue(tuples.contains(entity));
		doInJPA(new Function<EntityManager, Void>() {
			@Override
			public Void apply(EntityManager entityManager) {
				T _entity = entityManager.merge(entity);
				assertTrue("The entity is found after it's merged", tuples.contains(_entity));
	
				entityManager.unwrap(Session.class).update(entity);
				assertTrue("The entity is found after it's reattached", tuples.contains(entity));
				return null;
			}
		});

		doInJPA(new Function<EntityManager, Void>() {
			@Override
			public Void apply(EntityManager entityManager) {
				T _entity = entityManager.find(clazz, entity.getId());
				assertTrue("The entity is found after it's loaded " + "in an other Persistence Context",
						tuples.contains(_entity));
				return null;
			}
		});

		doASync(new Runnable() {
			@Override
			public void run() {
				doInJPA(new Function<EntityManager, Void>() {
					@Override
					public Void apply(EntityManager entityManager) {
						T _entity = entityManager.find(clazz, entity.getId());
						assertTrue("The entity is found after it's loaded " + "in an other Persistence Context and "
								+ "in an other thread", tuples.contains(_entity));
						return null;
					}
				});
			}
		});
		
		entityManagerReset();
	}
	
	private void doASync(Runnable runnable) throws InterruptedException {
		Thread thread = new Thread(runnable);
		thread.start();
		thread.join();
	}

	private void doInJPA(final Function<EntityManager, Void> consumer) {
		final EntityManager entityManager = entityManagerUtils.openEntityManager();
		try {
			writeTransactionTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
						consumer.apply(entityManager);
					return null;
				}
			});
		} finally {
			entityManagerUtils.closeEntityManager();
		}
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
