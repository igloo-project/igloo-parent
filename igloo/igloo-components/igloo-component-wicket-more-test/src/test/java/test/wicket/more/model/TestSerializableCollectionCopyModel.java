package test.wicket.more.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.repeater.collection.ICollectionModel;
import org.iglooproject.wicket.more.model.CollectionCopyModel;
import org.iglooproject.wicket.more.util.model.Models;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Equivalence;
import com.google.common.collect.Ordering;

import test.wicket.more.model.TestSerializableCollectionCopyModel.ValueEnum;

@RunWith(Parameterized.class)
public class TestSerializableCollectionCopyModel<C extends Collection<ValueEnum>>
		extends AbstractTestCollectionModel<C> {
	
	public static enum ValueEnum {
		VALUE1,
		VALUE2,
		VALUE3;
	}

	@Parameters(name = "{index}: {0}, {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ Suppliers2.arrayList(), Equivalence.equals() },
				{ Suppliers2.linkedList(), Equivalence.equals() },
				{ Suppliers2.hashSet(), UNORDERED_SET_EQUIVALENCE },
				{ Suppliers2.linkedHashSet(), Equivalence.equals() },
				{ Suppliers2.treeSet(), Equivalence.equals() },
				{ Suppliers2.treeSet(Ordering.natural().reverse().nullsLast()), Equivalence.equals() }
		});
	}

	private final SerializableSupplier2<? extends C> collectionSupplier;
	
	public TestSerializableCollectionCopyModel(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
		super(equivalence);
		this.collectionSupplier = collectionSupplier;
	}

	protected ICollectionModel<ValueEnum, C> createModel() {
		return CollectionCopyModel.custom(collectionSupplier, Models.<ValueEnum>serializableModelFactory());
	}

	protected C createCollection(ValueEnum... items) {
		C collection = collectionSupplier.get();
		collection.addAll(Arrays.asList(items));
		return collection;
	}

	protected C clone(C collection) {
		C clone = collectionSupplier.get();
		clone.addAll(collection);
		return clone;
	}
	
	@Test
	public void testNull() {
		IModel<C> model = createModel();
		model.setObject(null);
		assertThat(model.getObject(), isEmpty());
		model = serializeAndDeserialize(model);
		assertThat(model.getObject(), isEmpty());
	}
	
	@Test
	public void testNonNull() {
		C collection = createCollection(ValueEnum.VALUE1, ValueEnum.VALUE2);
		
		IModel<C> model = createModel();
		model.setObject(clone(collection));
		assertThat(model.getObject(), isEquivalent(collection));
		
		model = serializeAndDeserialize(model);
		C modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(collection.size(), modelObject.size());
		assertThat(modelObject, isEquivalent(collection));
	}
	
	@Test
	public void testCollectionCopy() throws Exception {
		C collection = createCollection(ValueEnum.VALUE1, ValueEnum.VALUE2);
		
		IModel<C> model = createModel();
		C collectionSetOnModel = clone(collection);
		model.setObject(collectionSetOnModel);
		assertThat(model.getObject(), isEquivalent(collection));
		
		collectionSetOnModel.add(ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(collection));
	}
	
	@Test
	public void testGetObjectAdd() throws Exception {
		IModel<C> model = createModel();
		model.setObject(createCollection(ValueEnum.VALUE1, ValueEnum.VALUE2));
		C modelObject = model.getObject();
		
		modelObject.add(ValueEnum.VALUE3);
		assertThat(model.getObject(), isEquivalent(createCollection(ValueEnum.VALUE1, ValueEnum.VALUE2, ValueEnum.VALUE3)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(3, modelObject.size());
		assertThat(modelObject, isEquivalent(createCollection(ValueEnum.VALUE1, ValueEnum.VALUE2, ValueEnum.VALUE3)));
	}
	
	@Test
	public void testGetObjectRemove() throws Exception {
		IModel<C> model = createModel();
		model.setObject(createCollection(ValueEnum.VALUE1, ValueEnum.VALUE2));
		C modelObject = model.getObject();
		
		modelObject.remove(ValueEnum.VALUE2);
		assertThat(model.getObject(), isEquivalent(createCollection(ValueEnum.VALUE1)));
		
		model = serializeAndDeserialize(model);
		modelObject = model.getObject();
		assertNotNull(modelObject);
		assertEquals(1, modelObject.size());
		assertThat(modelObject, isEquivalent(createCollection(ValueEnum.VALUE1)));
	}
	
}
