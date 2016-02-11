package fr.openwide.core.test.wicket.more.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.wicket.model.IModel;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Equivalence;

import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.service.PersonService;

public abstract class AbstractTestGenericEntityModel extends AbstractTestModel<Person> {
	
	@Autowired
	private PersonService personService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public AbstractTestGenericEntityModel() {
		super(Equivalence.equals());
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
		assertThat(model.getObject(), CoreMatchers.not(isEquivalent(person)));
	}
	
	@Test
	public void testAttachedWhenPersisted() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		assertThat(person, isAttachedToSession());
		
		IModel<Person> model = createModel(person);
		assertThat(model.getObject(), isAttachedToSession());
		
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEquivalent(person));
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
		assertThat(model.getObject(), isEquivalent(person));
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
		assertThat(model.getObject(), isEquivalent(person));
		assertThat(person, isAttachedToSession());
		assertThat(model.getObject(), isAttachedToSession());
	}

}
