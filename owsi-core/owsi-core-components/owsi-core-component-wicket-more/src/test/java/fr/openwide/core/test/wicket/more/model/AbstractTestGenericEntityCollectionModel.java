package fr.openwide.core.test.wicket.more.model;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.service.PersonService;
import fr.openwide.core.test.wicket.more.AbstractWicketMoreJpaTestCase;

public abstract class AbstractTestGenericEntityCollectionModel<C extends Collection<Person>> extends AbstractWicketMoreJpaTestCase {
	
	@Autowired
	private PersonService personService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	private static <T extends IDetachable> T serializeAndDeserialize(T object) {
		byte[] array;
		
		object.detach();
		
		try {
			ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(arrayOut);
			objectOut.writeObject(object);
			array = arrayOut.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error while serializing " + object, e);
		}

		try {
			ByteArrayInputStream arrayIn = new ByteArrayInputStream(array);
			ObjectInputStream objectIn = new ObjectInputStream(arrayIn);
			return (T) objectIn.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Error while deserializing " + object, e);
		}
	}
	
	private Matcher<C> isEmpty() {
		return new TypeSafeMatcher<C>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("an empty collection");
			}

			@Override
			protected boolean matchesSafely(C item) {
				return item.isEmpty();
			}
		};
	}
	
	private Matcher<Person> attachedToSession() {
		return new TypeSafeMatcher<Person>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("an entity already in the session");
			}

			@Override
			protected boolean matchesSafely(Person item) {
				return entityManager.contains(item);
			}
		};
	}
	
	private Matcher<C> equals(final C expected) {
		return new TypeSafeMatcher<C>() {
			@Override
			public void describeTo(Description description) {
				description.appendValue(expected);
			}

			@Override
			protected boolean matchesSafely(C item) {
				return AbstractTestGenericEntityCollectionModel.this.equals(expected, item);
			}
		};
	}

	protected abstract boolean equals(C expected, C item);
	
	protected abstract IModel<C> createModel();
	
	protected abstract C createCollection(Person ... person);
	
	protected abstract C clone(C collection);
	
	@Test
	public void testNotAttached() {
		IModel<C> model = createModel();
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}
	
	@Test
	public void testAttachedNull() {
		IModel<C> model = createModel();
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}
	
	@Test
	public void testAttachedWhenTransient() {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), equals(collection));
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(collection.size(), modelObject.size());
		assertThat(modelObject, not(equals(collection)));
	}
	
	@Test
	public void testAttachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, attachedToSession());
		personService.create(person2);
		assertThat(person2, attachedToSession());
		
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), equals(collection));
		for (Person p : model.getObject()) {
			assertThat(p, attachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject, equals(collection));
		
		for (Person p : modelObject) {
			assertThat(p, attachedToSession());
		}
	}
	
	@Test
	public void testAttachedWhenTransientAndDetachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), equals(collection));
		
		personService.create(person1);
		assertThat(person1, attachedToSession());
		personService.create(person2);
		assertThat(person2, attachedToSession());
		for (Person p : model.getObject()) {
			assertThat(p, attachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject, equals(collection));
		
		for (Person p : modelObject) {
			assertThat(p, attachedToSession());
		}
	}
	
	@Test
	public void testAttachedWhenPersistedAndDetachedWhenTransient() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, attachedToSession());
		personService.create(person2);
		assertThat(person2, attachedToSession());
		
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), equals(collection));
		for (Person p : model.getObject()) {
			assertThat(p, attachedToSession());
		}
		
		personService.delete(person1);
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		C expected = createCollection(null, person2);
		assertThat(modelObject, equals(expected));
	}
	
	@Test
	public void testCollectionChangeWithoutSetObject() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, attachedToSession());
		
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		C collectionSetOnModel = clone(collection);
		model.setObject(collectionSetOnModel);
		assertThat(model.getObject(), equals(collection));
		
		Person person3 = new Person("John3", "Doe3");
		collectionSetOnModel.add(person3);
		assertThat(model.getObject(), equals(collection));
	}

}
