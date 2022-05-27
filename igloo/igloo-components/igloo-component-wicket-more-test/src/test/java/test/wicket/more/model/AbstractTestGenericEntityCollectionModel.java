package test.wicket.more.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;

import test.wicket.more.business.person.model.Person;
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
	
	protected abstract IModel<C> createModel(SerializableSupplier2<? extends C> collectionSupplier);
	
	protected abstract C createCollection(SerializableSupplier2<? extends C> collectionSupplier, Person ... person);
	
	protected abstract C clone(SerializableSupplier2<? extends C> collectionSupplier, C collection);
	
	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testNotAttached(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
		IModel<C> model = createModel(collectionSupplier);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedNull(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
		IModel<C> model = createModel(collectionSupplier);
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenTransient(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(collectionSupplier, person1, person2);
		
		IModel<C> model = createModel(collectionSupplier);
		model.setObject(clone(collectionSupplier, collection));
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSameSizeAs(collection);
		assertThat(modelObject, not(isEquivalent(equivalence, collection)));
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenPersisted(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		C collection = createCollection(collectionSupplier, person1, person2);
		
		IModel<C> model = createModel(collectionSupplier);
		model.setObject(clone(collectionSupplier, collection));
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		for (Person p : model.getObject()) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(equivalence, collection));
		
		for (Person p : modelObject) {
			assertThat(p, isAttachedToSession());
		}
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenTransientAndDetachedWhenPersisted(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(collectionSupplier, person1, person2);
		
		IModel<C> model = createModel(collectionSupplier);
		model.setObject(clone(collectionSupplier, collection));
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		for (Person p : model.getObject()) {
			assertThat(p, isAttachedToSession());
		}
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(equivalence, collection));
		
		for (Person p : modelObject) {
			assertThat(p, isAttachedToSession());
		}
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testAttachedWhenPersistedAndDetachedWhenTransient(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		personService.create(person2);
		assertThat(person2, isAttachedToSession());
		
		C collection = createCollection(collectionSupplier, person1, person2);
		
		IModel<C> model = createModel(collectionSupplier);
		model.setObject(clone(collectionSupplier, collection));
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		for (Person p : model.getObject()) {
			assertThat(p, isAttachedToSession());
		}
		
		personService.delete(person1);
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		C expected = createCollection(collectionSupplier, null, person2);
		assertThat(modelObject, isEquivalent(equivalence, expected));
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testCollectionCopy(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		C collection = createCollection(collectionSupplier, person1, person2);
		
		IModel<C> model = createModel(collectionSupplier);
		C collectionSetOnModel = clone(collectionSupplier, collection);
		model.setObject(collectionSetOnModel);
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		
		Person person3 = new Person("John3", "Doe3");
		collectionSetOnModel.add(person3);
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testDetachedWhenTransientThenDetachedWhenPersisted(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		C collection = createCollection(collectionSupplier, person1, person2);
		
		IModel<C> model = createModel(collectionSupplier);
		model.setObject(clone(collectionSupplier, collection));
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		model.detach(); // First detach()

		// Simulate work on the same object obtained from another model
		personService.create(person2);
		assertThat(person2, isAttachedToSession());

		model = serializeAndDeserialize(model); // Includes a second detach()
		C modelObject = model.getObject();
		assertThat(modelObject, isEquivalent(equivalence, collection));
		
		for (Person p : modelObject) {
			assertThat(p, isAttachedToSession());
		}
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testCollectionGetObjectAdd(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<C> model = createModel(collectionSupplier);
		C collectionSetOnModel = createCollection(collectionSupplier, person1, person2);
		model.setObject(collectionSetOnModel);
		C modelObject = model.getObject();
		
		Person person3 = new Person("John3", "Doe3");
		modelObject.add(person3);
		assertThat(model.getObject(), isEquivalent(equivalence, createCollection(collectionSupplier, person1, person2, person3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSize(3);
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testCollectionGetObjectRemove(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<C> model = createModel(collectionSupplier);
		C collectionSetOnModel = createCollection(collectionSupplier, person1, person2);
		model.setObject(collectionSetOnModel);
		C modelObject = model.getObject();
		
		modelObject.remove(person2);
		assertThat(model.getObject(), isEquivalent(equivalence, createCollection(collectionSupplier, person1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSize(1);
		assertThat(modelObject, isEquivalent(equivalence, createCollection(collectionSupplier, person1)));
	}

}
