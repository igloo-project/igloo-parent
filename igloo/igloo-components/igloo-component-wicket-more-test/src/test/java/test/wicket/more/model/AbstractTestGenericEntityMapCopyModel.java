package test.wicket.more.model;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.repeater.map.IMapModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.service.IPersonService;

public abstract class AbstractTestGenericEntityMapCopyModel<K, V, M extends Map<K, V>>
		extends AbstractTestMapModel<M> {
	
	protected static final Equivalence<Map<?, ?>> ORDERED_MAP_EQUIVALENCE = new Equivalence<Map<?, ?>>() {
		@Override
		protected boolean doEquivalent(Map<?, ?> a, Map<?, ?> b) {
			return Lists.newArrayList(a.entrySet()).equals(Lists.newArrayList(b.entrySet())); // SortedMap.equals won't work on cloned transient instances
		}

		@Override
		protected int doHash(Map<?, ?> t) {
			return Lists.newArrayList(t.entrySet()).hashCode();
		}
		
		@Override
		public String toString() {
			return "ORDERED_MAP_EQUIVALENCE";
		}
	};
	
	@Autowired
	protected IPersonService personService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public AbstractTestGenericEntityMapCopyModel(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) {
		super(mapSupplier, equivalence);
	}

	protected abstract IMapModel<K, V, M> createModel();

	protected abstract M createMap(Person... persons);
	
	protected abstract Iterable<Person> personIterable(M map);
	
	@Test
	public void testNotAttached() {
		IModel<M> model = createModel();
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}
	
	@Test
	public void testAttachedNull() {
		IModel<M> model = createModel();
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}
	
	@Test
	public void testAttachedWhenTransient() {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		M map = createMap(person1, person2);
		
		IModel<M> model = createModel();
		model.setObject(clone(map));
		assertThat(model.getObject(), isEquivalent(map));
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(map.size(), modelObject.size());
		assertThat(modelObject, not(isEquivalent(map)));
	}
	
	@Test
	public void testAttachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		M map = createMap(person1, person2);
		
		IModel<M> model = createModel();
		model.setObject(clone(map));
		assertThat(model.getObject(), isEquivalent(map));
		for (Person p : personIterable(model.getObject())) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(map));
		
		for (Person p : personIterable(modelObject)) {
			assertThat(p, isAttachedToSession());
		}
	}
	
	@Test
	public void testAttachedWhenTransientAndDetachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		M map = createMap(person1, person2);
		
		IModel<M> model = createModel();
		model.setObject(clone(map));
		assertThat(model.getObject(), isEquivalent(map));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		for (Person p : personIterable(model.getObject())) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(map));
		
		for (Person p : personIterable(modelObject)) {
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
		
		M map = createMap(person1, person2);
		
		IModel<M> model = createModel();
		model.setObject(clone(map));
		assertThat(model.getObject(), isEquivalent(map));
		for (Person p : personIterable(model.getObject())) {
			assertThat(p, isAttachedToSession());
		}
		
		personService.delete(person1);
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		M expected = createMap(null, person2);
		assertThat(modelObject, isEquivalent(expected));
	}

	@Test
	public void testDetachedWhenTransientThenDetachedWhenPersisted() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		M map = createMap(person1, person2);
		
		IModel<M> model = createModel();
		model.setObject(clone(map));
		assertThat(model.getObject(), isEquivalent(map));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		model.detach(); // First detach()

		// Simulate work on the same object obtained from another model
		personService.create(person2);
		assertThat(person2, isAttachedToSession());

		model = serializeAndDeserialize(model); // Includes a second detach()
		M modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(map));
		
		for (Person p : personIterable(modelObject)) {
			assertThat(p, isAttachedToSession());
		}
	}
	
}
