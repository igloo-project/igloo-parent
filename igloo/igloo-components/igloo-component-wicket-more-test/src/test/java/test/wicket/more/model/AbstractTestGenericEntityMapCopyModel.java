package test.wicket.more.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.repeater.map.IMapModel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
	
	protected abstract IMapModel<K, V, M> createModel(SerializableSupplier2<? extends M> mapSupplier);

	protected abstract M createMap(SerializableSupplier2<? extends M> mapSupplier, Person... persons);
	
	protected abstract Iterable<Person> personIterable(M map);

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testNotAttached(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) {
		IModel<M> model = createModel(mapSupplier);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedNull(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) {
		IModel<M> model = createModel(mapSupplier);
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenTransient(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		M map = createMap(mapSupplier, person1, person2);
		
		IModel<M> model = createModel(mapSupplier);
		model.setObject(clone(mapSupplier, map));
		assertThat(model.getObject(), isEquivalent(equivalence, map));
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSameSizeAs(map);
		assertThat(modelObject, not(isEquivalent(equivalence, map)));
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenPersisted(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		M map = createMap(mapSupplier, person1, person2);
		
		IModel<M> model = createModel(mapSupplier);
		model.setObject(clone(mapSupplier, map));
		assertThat(model.getObject(), isEquivalent(equivalence, map));
		for (Person p : personIterable(model.getObject())) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(equivalence, map));
		
		for (Person p : personIterable(modelObject)) {
			assertThat(p, isAttachedToSession());
		}
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenTransientAndDetachedWhenPersisted(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		M map = createMap(mapSupplier, person1, person2);
		
		IModel<M> model = createModel(mapSupplier);
		model.setObject(clone(mapSupplier, map));
		assertThat(model.getObject(), isEquivalent(equivalence, map));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		for (Person p : personIterable(model.getObject())) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(equivalence, map));
		
		for (Person p : personIterable(modelObject)) {
			assertThat(p, isAttachedToSession());
		}
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenPersistedAndDetachedWhenTransient(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		M map = createMap(mapSupplier, person1, person2);
		
		IModel<M> model = createModel(mapSupplier);
		model.setObject(clone(mapSupplier, map));
		assertThat(model.getObject(), isEquivalent(equivalence, map));
		for (Person p : personIterable(model.getObject())) {
			assertThat(p, isAttachedToSession());
		}
		
		personService.delete(person1);
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		M expected = createMap(mapSupplier, null, person2);
		assertThat(modelObject, isEquivalent(equivalence, expected));
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testDetachedWhenTransientThenDetachedWhenPersisted(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		M map = createMap(mapSupplier, person1, person2);
		
		IModel<M> model = createModel(mapSupplier);
		model.setObject(clone(mapSupplier, map));
		assertThat(model.getObject(), isEquivalent(equivalence, map));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		model.detach(); // First detach()

		// Simulate work on the same object obtained from another model
		personService.create(person2);
		assertThat(person2, isAttachedToSession());

		model = serializeAndDeserialize(model); // Includes a second detach()
		M modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(equivalence, map));
		
		for (Person p : personIterable(modelObject)) {
			assertThat(p, isAttachedToSession());
		}
	}
	
}
