package test.wicket.more.model;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.model.IModel;
import org.assertj.core.api.Assertions;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.base.Equivalence;
import com.google.common.collect.Ordering;

import igloo.wicket.model.IMapModel;
import igloo.wicket.model.MapCopyModel;
import igloo.wicket.model.Models;
import test.wicket.more.business.person.model.Person;
import test.wicket.more.model.TestGenericEntityValueMapCopyModel.KeyEnum;

class TestGenericEntityValueMapCopyModel<M extends Map<KeyEnum, Person>>
		extends AbstractTestGenericEntityMapCopyModel<KeyEnum, Person, M> {

	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ Suppliers2.hashMap(), UNORDERED_MAP_EQUIVALENCE },
				{ Suppliers2.linkedHashMap(), ORDERED_MAP_EQUIVALENCE },
				{ Suppliers2.treeMap(), ORDERED_MAP_EQUIVALENCE },
				{ Suppliers2.treeMap(Ordering.natural().reverse().nullsLast()), ORDERED_MAP_EQUIVALENCE }
		});
	}
	
	public static enum KeyEnum {
		KEY1,
		KEY2,
		KEY3;
	}
	
	@Override
	protected IMapModel<KeyEnum, Person, M> createModel(SerializableSupplier2<? extends M> mapSupplier) {
		return MapCopyModel.custom(mapSupplier, Models.<KeyEnum>serializableModelFactory(), GenericEntityModel.<Person>factory());
	}

	@Override
	protected M createMap(SerializableSupplier2<? extends M> mapSupplier, Person... persons) {
		M map = mapSupplier.get();
		Iterator<KeyEnum> valueIt = EnumUtils.getEnumList(KeyEnum.class).iterator();
		for (Person person : persons) {
			map.put(valueIt.next(), person);
		}
		return map;
	}
	
	
	@Override
	protected Iterable<Person> personIterable(M map) {
		return map.values();
	}

	@ParameterizedTest
	@MethodSource("data")
	void testMapCopy(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		M map = createMap(mapSupplier, person1, person2);
		
		IModel<M> model = createModel(mapSupplier);
		M mapSetOnModel = clone(mapSupplier, map);
		model.setObject(mapSetOnModel);
		assertThat(model.getObject(), isEquivalent(equivalence, map));
		
		Person person3 = new Person("John3", "Doe3");
		mapSetOnModel.put(KeyEnum.KEY3, person3);
		assertThat(model.getObject(), isEquivalent(equivalence, map));
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testGetObjectPut(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<M> model = createModel(mapSupplier);
		M collectionSetOnModel = createMap(mapSupplier, person1, person2);
		model.setObject(collectionSetOnModel);
		M modelObject = model.getObject();
		
		Person person3 = new Person("John3", "Doe3");
		modelObject.put(KeyEnum.KEY3, person3);
		assertThat(model.getObject(), isEquivalent(equivalence, createMap(mapSupplier, person1, person2, person3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		Assertions.assertThat(modelObject).isNotNull();
		Assertions.assertThat(modelObject).hasSize(3);
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testGetObjectRemove(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<M> model = createModel(mapSupplier);
		M mapSetOnModel = createMap(mapSupplier, person1, person2);
		model.setObject(mapSetOnModel);
		M modelObject = model.getObject();
		
		modelObject.remove(KeyEnum.KEY2);
		assertThat(model.getObject(), isEquivalent(equivalence, createMap(mapSupplier, person1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		Assertions.assertThat(modelObject).isNotNull();
		Assertions.assertThat(modelObject).hasSize(1);
		assertThat(modelObject, isEquivalent(equivalence, createMap(mapSupplier, person1)));
	}

}
