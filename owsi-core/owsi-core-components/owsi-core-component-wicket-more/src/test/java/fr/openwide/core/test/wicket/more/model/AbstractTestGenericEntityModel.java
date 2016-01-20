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
		assertThat(person, isAttachedToSession());
		
		IModel<Person> model = createModel(person);
		assertThat(model.getObject(), isAttachedToSession());
		
		model = serializeAndDeserialize(model);
		assertEquals(person, model.getObject());
		assertThat(person, isAttachedToSession());
		assertThat(model.getObject(), isAttachedToSession());
	}
	
	@Test
	public void testAttachedWhenTransientAndDetachedWhenPersisted() throws Exception {
		Person person = new Person("John", "Doe");
		IModel<Person> model = createModel(person);
		
		personService.create(person);
		assertThat(person, isAttachedToSession());
		assertThat(model.getObject(), isAttachedToSession());
		
		model = serializeAndDeserialize(model);
		assertEquals(person, model.getObject());
		assertThat(person, isAttachedToSession());
		assertThat(model.getObject(), isAttachedToSession());
	}
	
	@Test
	public void testAttachedWhenPersistedAndDetachedWhenTransient() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		assertThat(person, isAttachedToSession());
		
		IModel<Person> model = createModel(person);
		assertThat(model.getObject(), isAttachedToSession());
		
		personService.delete(person);
		
		model = serializeAndDeserialize(model);
		assertNull(model.getObject()); // Tries to load an entity whose id no longer exists => null
	}
	
	@Test
	public void testDetachedWhenTransientThenDetachedWhenPersisted() throws Exception {
		Person person = new Person("John", "Doe");
		IModel<Person> model = createModel(person);
		
		model.detach(); // First detach()
		
		// Simulate work on the same object obtained from another model
		personService.create(person);
		assertThat(person, isAttachedToSession());
		
		model = serializeAndDeserialize(model); // Includes a second detach()
		assertEquals(person, model.getObject());
		assertThat(person, isAttachedToSession());
		assertThat(model.getObject(), isAttachedToSession());
	}

}
