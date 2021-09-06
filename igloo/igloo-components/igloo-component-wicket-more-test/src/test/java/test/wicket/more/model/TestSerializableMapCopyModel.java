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
import org.iglooproject.wicket.more.model.MapCopyModel;
import org.iglooproject.wicket.more.util.model.Models;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Equivalence;
import com.google.common.collect.Ordering;

import test.wicket.more.model.TestSerializableMapCopyModel.KeyEnum;
import test.wicket.more.model.TestSerializableMapCopyModel.ValueEnum;

@RunWith(Parameterized.class)
public class TestSerializableMapCopyModel<M extends Map<KeyEnum, ValueEnum>>
		extends AbstractTestMapModel<M> {

	@Parameters(name = "{index}: {0}, {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ Suppliers2.hashMap(), UNORDERED_MAP_EQUIVALENCE },
				{ Suppliers2.linkedHashMap(), Equivalence.equals() },
				{ Suppliers2.treeMap(), Equivalence.equals() },
				{ Suppliers2.treeMap(Ordering.natural().reverse().nullsLast()), Equivalence.equals() }
		});
	}
	
	public static enum KeyEnum {
		KEY1,
		KEY2,
		KEY3;
	}
	
	public static enum ValueEnum {
		VALUE1,
		VALUE2,
		VALUE3;
	}
	
	public TestSerializableMapCopyModel(SerializableSupplier2<? extends M> supplier, Equivalence<? super M> equivalence) {
		super(supplier, equivalence);
	}

	protected IMapModel<KeyEnum, ValueEnum, M> createModel() {
		return MapCopyModel.custom(mapSupplier, Models.<KeyEnum>serializableModelFactory(), Models.<ValueEnum>serializableModelFactory());
	}

	protected M createMap(KeyEnum... items) {
		M map = mapSupplier.get();
		Iterator<ValueEnum> valueIt = EnumUtils.getEnumList(ValueEnum.class).iterator();
		for (KeyEnum item : items) {
			map.put(item, valueIt.next());
		}
		return map;
	}
	
	@Test
	public void testNull() {
		IModel<M> model = createModel();
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}
	
	@Test
	public void testNonNull() {
		M collection = createMap(KeyEnum.KEY1, KeyEnum.KEY2);
		
		IModel<M> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), isEquivalent(collection));
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(collection.size(), modelObject.size());
		assertThat(modelObject, isEquivalent(collection));
	}
	
	@Test
	public void testCollectionCopy() throws Exception {
		M collection = createMap(KeyEnum.KEY1, KeyEnum.KEY2);
		
		IModel<M> model = createModel();
		M collectionSetOnModel = clone(collection);
		model.setObject(collectionSetOnModel);
		assertThat(model.getObject(), isEquivalent(collection));
		
		collectionSetOnModel.put(KeyEnum.KEY3, ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(collection));
	}
	
	@Test
	public void testGetObjectAdd() throws Exception {
		IModel<M> model = createModel();
		model.setObject(createMap(KeyEnum.KEY1, KeyEnum.KEY2));
		M modelObject = model.getObject();

		modelObject.put(KeyEnum.KEY3, ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(createMap(KeyEnum.KEY1, KeyEnum.KEY2, KeyEnum.KEY3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(3, modelObject.size());
		assertThat(modelObject, isEquivalent(createMap(KeyEnum.KEY1, KeyEnum.KEY2, KeyEnum.KEY3)));
	}
	
	@Test
	public void testCollectionGetObjectRemove() throws Exception {
		IModel<M> model = createModel();
		model.setObject(createMap(KeyEnum.KEY1, KeyEnum.KEY2));
		M modelObject = model.getObject();
		
		modelObject.remove(KeyEnum.KEY2);
		assertThat(model.getObject(), isEquivalent(createMap(KeyEnum.KEY1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(1, modelObject.size());
		assertThat(modelObject, isEquivalent(createMap(KeyEnum.KEY1)));
	}
	
}
