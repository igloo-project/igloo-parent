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
import org.iglooproject.wicket.more.model.MapCopyModel;
import org.iglooproject.wicket.more.util.model.Models;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.base.Equivalence;
import com.google.common.collect.Ordering;

import test.wicket.more.model.TestSerializableMapCopyModel.KeyEnum;
import test.wicket.more.model.TestSerializableMapCopyModel.ValueEnum;

public class TestSerializableMapCopyModel<M extends Map<KeyEnum, ValueEnum>>
		extends AbstractTestMapModel<M> {

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
	
	protected IMapModel<KeyEnum, ValueEnum, M> createModel(SerializableSupplier2<? extends M> mapSupplier) {
		return MapCopyModel.custom(mapSupplier, Models.<KeyEnum>serializableModelFactory(), Models.<ValueEnum>serializableModelFactory());
	}

	protected M createMap(SerializableSupplier2<? extends M> mapSupplier, KeyEnum... items) {
		M map = mapSupplier.get();
		Iterator<ValueEnum> valueIt = EnumUtils.getEnumList(ValueEnum.class).iterator();
		for (KeyEnum item : items) {
			map.put(item, valueIt.next());
		}
		return map;
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testNull(SerializableSupplier2<? extends M> supplier, Equivalence<? super M> equivalence) {
		IModel<M> model = createModel(supplier);
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testNonNull(SerializableSupplier2<? extends M> supplier, Equivalence<? super M> equivalence) {
		M collection = createMap(supplier, KeyEnum.KEY1, KeyEnum.KEY2);
		
		IModel<M> model = createModel(supplier);
		model.setObject(clone(supplier, collection));
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		
		model = serializeAndDeserialize(model);
		M modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSameSizeAs(collection);
		assertThat(modelObject, isEquivalent(equivalence, collection));
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testCollectionCopy(SerializableSupplier2<? extends M> supplier, Equivalence<? super M> equivalence) throws Exception {
		M collection = createMap(supplier, KeyEnum.KEY1, KeyEnum.KEY2);
		
		IModel<M> model = createModel(supplier);
		M collectionSetOnModel = clone(supplier, collection);
		model.setObject(collectionSetOnModel);
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
		
		collectionSetOnModel.put(KeyEnum.KEY3, ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(equivalence, collection));
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testGetObjectAdd(SerializableSupplier2<? extends M> supplier, Equivalence<? super M> equivalence) throws Exception {
		IModel<M> model = createModel(supplier);
		model.setObject(createMap(supplier, KeyEnum.KEY1, KeyEnum.KEY2));
		M modelObject = model.getObject();

		modelObject.put(KeyEnum.KEY3, ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(equivalence, createMap(supplier, KeyEnum.KEY1, KeyEnum.KEY2, KeyEnum.KEY3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSize(3);
		assertThat(modelObject, isEquivalent(equivalence, createMap(supplier, KeyEnum.KEY1, KeyEnum.KEY2, KeyEnum.KEY3)));
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testCollectionGetObjectRemove(SerializableSupplier2<? extends M> supplier, Equivalence<? super M> equivalence) throws Exception {
		IModel<M> model = createModel(supplier);
		model.setObject(createMap(supplier, KeyEnum.KEY1, KeyEnum.KEY2));
		M modelObject = model.getObject();
		
		modelObject.remove(KeyEnum.KEY2);
		assertThat(model.getObject(), isEquivalent(equivalence, createMap(supplier, KeyEnum.KEY1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertThat(modelObject).isNotNull();
		assertThat(modelObject).hasSize(1);
		assertThat(modelObject, isEquivalent(equivalence, createMap(supplier, KeyEnum.KEY1)));
	}
	
}
