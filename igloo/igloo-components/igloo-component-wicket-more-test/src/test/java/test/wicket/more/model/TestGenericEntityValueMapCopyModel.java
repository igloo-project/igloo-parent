package test.wicket.more.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Equivalence;
import com.google.common.collect.Ordering;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.model.TestGenericEntityValueMapCopyModel.KeyEnum;

@RunWith(Parameterized.class)
public class TestGenericEntityValueMapCopyModel<M extends Map<KeyEnum, Person>>
		extends AbstractTestGenericEntityMapCopyModel<KeyEnum, Person, M> {

	@Parameters(name = "{index}: {0}, {1}")
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
	
	public TestGenericEntityValueMapCopyModel(SerializableSupplier2<? extends M> mapSupplier, Equivalence<? super M> equivalence) {
		super(mapSupplier, equivalence);
	}

	@Override
	protected IMapModel<KeyEnum, Person, M> createModel() {
		return MapCopyModel.custom(mapSupplier, Models.<KeyEnum>serializableModelFactory(), GenericEntityModel.<Person>factory());
	}

	@Override
	protected M createMap(Person... persons) {
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

	@Test
	public void testMapCopy() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		M map = createMap(person1, person2);
		
		IModel<M> model = createModel();
		M mapSetOnModel = clone(map);
		model.setObject(mapSetOnModel);
		assertThat(model.getObject(), isEquivalent(map));
		
		Person person3 = new Person("John3", "Doe3");
		mapSetOnModel.put(KeyEnum.KEY3, person3);
		assertThat(model.getObject(), isEquivalent(map));
	}
	
	@Test
	public void testGetObjectPut() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<M> model = createModel();
		M collectionSetOnModel = createMap(person1, person2);
		model.setObject(collectionSetOnModel);
		M modelObject = model.getObject();
		
		Person person3 = new Person("John3", "Doe3");
		modelObject.put(KeyEnum.KEY3, person3);
		assertThat(model.getObject(), isEquivalent(createMap(person1, person2, person3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(3, modelObject.size());
	}
	
	@Test
	public void testGetObjectRemove() throws Exception {
		Person person1 = new Person("John", "Doe");
		Person person2 = new Person("John2", "Doe2");
		personService.create(person1);
		assertThat(person1, isAttachedToSession());
		
		IModel<M> model = createModel();
		M mapSetOnModel = createMap(person1, person2);
		model.setObject(mapSetOnModel);
		M modelObject = model.getObject();
		
		modelObject.remove(KeyEnum.KEY2);
		assertThat(model.getObject(), isEquivalent(createMap(person1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(1, modelObject.size());
		assertThat(modelObject, isEquivalent(createMap(person1)));
	}

}
