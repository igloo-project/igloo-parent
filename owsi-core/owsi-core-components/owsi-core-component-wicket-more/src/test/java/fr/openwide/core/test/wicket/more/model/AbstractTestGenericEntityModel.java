package fr.openwide.core.test.wicket.more.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

public abstract class AbstractTestGenericEntityModel extends AbstractWicketMoreJpaTestCase {
	
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
	
	protected abstract IModel<Person> createModel();
	protected abstract IModel<Person> createModel(Person person);
	
	@Test
	public void testNotAttached() {
		IModel<Person> model = createModel();
		model = serializeAndDeserialize(model);
		assertNull(model.getObject());
	}
	
	@Test
	public void testAttachedNull() {
		IModel<Person> model = createModel(null);
		model = serializeAndDeserialize(model);
		assertNull(model.getObject());
	}
	
	@Test
	public void testAttachedWhenTransient() {
		Person person = new Person("John", "Doe");
		IModel<Person> model = createModel(person);
		model = serializeAndDeserialize(model);
		assertNotEquals(person, model.getObject());
	}
	
	@Test
	public void testAttachedWhenPersisted() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		assertThat(person, attachedToSession());
		
		IModel<Person> model = createModel(person);
		assertThat(model.getObject(), attachedToSession());
		
		model = serializeAndDeserialize(model);
		assertEquals(person, model.getObject());
		assertThat(person, attachedToSession());
		assertThat(model.getObject(), attachedToSession());
	}
	
	@Test
	public void testAttachedWhenTransientAndDetachedWhenPersisted() throws Exception {
		Person person = new Person("John", "Doe");
		IModel<Person> model = createModel(person);
		
		personService.create(person);
		assertThat(person, attachedToSession());
		assertThat(model.getObject(), attachedToSession());
		
		model = serializeAndDeserialize(model);
		assertEquals(person, model.getObject());
		assertThat(person, attachedToSession());
		assertThat(model.getObject(), attachedToSession());
	}
	
	@Test
	public void testAttachedWhenPersistedAndDetachedWhenTransient() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		assertThat(person, attachedToSession());
		
		IModel<Person> model = createModel(person);
		assertThat(model.getObject(), attachedToSession());
		
		personService.delete(person);
		
		model = serializeAndDeserialize(model);
		assertNull(model.getObject()); // Tries to load an entity whose id no longer exists => null
	}

}
