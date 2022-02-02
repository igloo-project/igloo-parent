package test.wicket.more.model;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Equivalence;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;
import test.wicket.more.business.person.service.IPersonService;

public abstract class AbstractTestGenericEntityCollectionModel<C extends Collection<Person>>
		extends AbstractTestCollectionModel<C> {
	
	protected static final Equivalence<Set<?>> ORDERED_SET_EQUIVALENCE = new Equivalence<Set<?>>() {
		@Override
		protected boolean doEquivalent(Set<?> a, Set<?> b) {
			return Lists.newArrayList(a).equals(Lists.newArrayList(b)); // SortedSet.equals won't work on cloned transient instances
		}

		@Override
		protected int doHash(Set<?> t) {
			return Lists.newArrayList(t).hashCode();
		}
		
		@Override
		public String toString() {
			return "ORDERED_SET_EQUIVALENCE";
		}
	};
	
	@Autowired
	private IPersonService personService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public AbstractTestGenericEntityCollectionModel(Equivalence<? super C> equivalence) {
		super(equivalence);
	}
	
	protected abstract IModel<C> createModel();
	
	protected abstract C createCollection(Person ... person);
	
	protected abstract C clone(C collection);
	
	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testNotAttached(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
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
		assertThat(model.getObject(), isEquivalent(collection));
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(collection.size(), modelObject.size());
		assertThat(modelObject, not(isEquivalent(collection)));
	}
	
	@Test
	public void testAttachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), isEquivalent(collection));
		for (Person p : model.getObject()) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(collection));
		
		for (Person p : modelObject) {
			assertThat(p, isAttachedToSession());
		}
	}
	
	@Test
	public void testAttachedWhenTransientAndDetachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), isEquivalent(collection));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		for (Person p : model.getObject()) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(collection));
		
		for (Person p : modelObject) {
			assertThat(p, isAttachedToSession());
		}
	}
	
	@Test
	public void testAttachedWhenPersistedAndDetachedWhenTransient() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), isEquivalent(collection));
		for (Person p : model.getObject()) {
			assertThat(p, isAttachedToSession());
		}
		
		personService.delete(person1);
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		C expected = createCollection(null, person2);
		assertThat(modelObject, isEquivalent(expected));
	}
	
	@Test
	public void testCollectionCopy() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		C collectionSetOnModel = clone(collection);
		model.setObject(collectionSetOnModel);
		assertThat(model.getObject(), isEquivalent(collection));
		
		Person person3 = new Person("John3", "Doe3");
		collectionSetOnModel.add(person3);
		assertThat(model.getObject(), isEquivalent(collection));
	}
	
	@Test
	public void testDetachedWhenTransientThenDetachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(person1, person2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), isEquivalent(collection));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		model.detach(); // First detach()

		// Simulate work on the same object obtained from another model
		personService.create(person2);
		assertThat(person2, isAttachedToSession());

		model = serializeAndDeserialize(model); // Includes a second detach()
		C modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(collection));
		
		for (Person p : modelObject) {
			assertThat(p, isAttachedToSession());
		}
	}
	
	@Test
	public void testCollectionGetObjectAdd() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<C> model = createModel();
		C collectionSetOnModel = createCollection(person1, person2);
		model.setObject(collectionSetOnModel);
		C modelObject = model.getObject();
		
		Person person3 = new Person("John3", "Doe3");
		modelObject.add(person3);
		assertThat(model.getObject(), isEquivalent(createCollection(person1, person2, person3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(3, modelObject.size());
	}
	
	@Test
	public void testCollectionGetObjectRemove() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<C> model = createModel();
		C collectionSetOnModel = createCollection(person1, person2);
		model.setObject(collectionSetOnModel);
		C modelObject = model.getObject();
		
		modelObject.remove(person2);
		assertThat(model.getObject(), isEquivalent(createCollection(person1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(1, modelObject.size());
		assertThat(modelObject, isEquivalent(createCollection(person1)));
	}

}
