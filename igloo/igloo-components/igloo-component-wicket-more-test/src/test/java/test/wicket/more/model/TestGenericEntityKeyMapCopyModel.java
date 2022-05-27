package test.wicket.more.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.repeater.map.IMapModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.MapCopyModel;
import org.iglooproject.wicket.more.util.model.Models;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.base.Equivalence;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;
import test.wicket.more.model.TestGenericEntityKeyMapCopyModel.ValueEnum;

public class TestGenericEntityKeyMapCopyModel<M extends Map<Person, ValueEnum>>
		extends AbstractTestGenericEntityMapCopyModel<Person, ValueEnum, M> {

	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ Suppliers2.hashMap(), UNORDERED_MAP_EQUIVALENCE },
				{ Suppliers2.linkedHashMap(), ORDERED_MAP_EQUIVALENCE },
//				{ Suppliers2.treeMap(), Equivalence.equals() }, // Won't work since we have two transient entities
				{ Suppliers2.treeMap(PersonComparator.get()), ORDERED_MAP_EQUIVALENCE }
		});
	}
	
	public static enum ValueEnum {
		VALUE1,
		VALUE2,
		VALUE3;
	}
	
	@Override
	protected IMapModel<Person, ValueEnum, M> createModel(SerializableSupplier2<? extends M> mapSupplier) {
		return MapCopyModel.custom(mapSupplier, GenericEntityModel.<Person>factory(), Models.<ValueEnum>serializableModelFactory());
	}

	@Override
	protected M createMap(SerializableSupplier2<? extends M> mapSupplier, Person... persons) {
		M map = mapSupplier.get();
		Iterator<ValueEnum> valueIt = EnumUtils.getEnumList(ValueEnum.class).iterator();
		for (Person person : persons) {
			map.put(person, valueIt.next());
		}
		return map;
	}

	@Override
	protected Iterable<Person> personIterable(M map) {
		return map.keySet();
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
	@MethodSource("data")
	public void testMapCopy(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) throws Exception {
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
		mapSetOnModel.put(person3, ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(equivalence, map));
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
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
		modelObject.put(person3, ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(equivalence, createMap(mapSupplier, person1, person2, person3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSize(3);
	}

	@ParameterizedTest(name = "{index}: collectionSupplier={0}, equivalence={1}")
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
		
		modelObject.remove(person2);
		assertThat(model.getObject(), isEquivalent(equivalence, createMap(mapSupplier, person1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSize(1);
		assertThat(modelObject, isEquivalent(equivalence, createMap(mapSupplier, person1)));
	}

}
