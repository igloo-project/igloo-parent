package test.wicket.more.bindable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.bindable.model.IBindableCollectionModel;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import test.wicket.more.config.spring.SpringBootTestWicketSimple;

@SpringBootTestWicketSimple
class TestBindableCollectionModel extends AbstractTestBindableModel {
	
	@Test
	void alwaysReturnsSameModelInstance() {
		doReturn(rootValue).when(rootModel).getObject();
		
		IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
		IBindableModel<?> firstCall = bindableModel.bindCollectionWithCache(
				rootBinding().collectionProperty(),
				Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
		);
		IBindableModel<?> secondCall = bindableModel.bindCollectionAlreadyAdded(rootBinding().collectionProperty());
		assertSame(firstCall, secondCall);
		IBindableModel<?> thirdCall = bindableModel.bindCollectionWithCache(
				rootBinding().collectionProperty(),
				Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
		);
		assertSame(firstCall, thirdCall);
	}
	
	@Test
	void alwaysReturnsSameModelInstanceEvenIfChained() {
		doReturn(rootValue).when(rootModel).getObject();
		
		IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
		IBindableModel<?> directCall = bindableModel.bindCollectionWithCache(
				rootBinding().compositeProperty().collectionProperty(),
				Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
		);
		IBindableModel<?> chainedCall = bindableModel.bind(rootBinding().compositeProperty())
				.bindCollectionAlreadyAdded(rootBinding().collectionProperty());
		assertSame(directCall, chainedCall);
		IBindableModel<?> chainedCallWithCache = bindableModel.bind(rootBinding().compositeProperty())
				.bindCollectionWithCache(
						rootBinding().collectionProperty(),
						Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
				);
		assertSame(directCall, chainedCallWithCache);
	}

	@Test
	void simpleCacheUsage() {
		Collection<CollectionPropertyItemValue> firstExpectedValue = ImmutableList.of(new CollectionPropertyItemValue());
		Collection<CollectionPropertyItemValue> secondExpectedValue = ImmutableList.of(
				new CollectionPropertyItemValue(), new CollectionPropertyItemValue()
		);
		rootValue.setCollectionProperty(firstExpectedValue);

		doReturn(rootValue).when(rootModel).getObject();
		
		IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
		IBindableModel<Collection<CollectionPropertyItemValue>> propertyModel = bindableModel.bindCollectionWithCache(
				rootBinding().collectionProperty(),
				Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
		);
		
		assertEquals(firstExpectedValue, propertyModel.getObject());
		verify(rootValue).getCollectionProperty(); // Cache was initialized

		rootValue.setCollectionProperty(secondExpectedValue);
		
		propertyModel.readAll();
		verify(rootValue, times(2)).getCollectionProperty(); // The value was fetched
		
		assertEquals(secondExpectedValue, propertyModel.getObject()); // The cache was updated
		verify(rootValue, times(2)).getCollectionProperty(); // Only the cache was touched, no the actual value
		
		Collection<CollectionPropertyItemValue> valueToSet = ImmutableList.of(
				new CollectionPropertyItemValue(), new CollectionPropertyItemValue(), new CollectionPropertyItemValue()
		);
		assertEquals(secondExpectedValue, rootValue.getCollectionProperty());
		propertyModel.setObject(valueToSet);
		assertEquals(secondExpectedValue, rootValue.getCollectionProperty());
				// Only the cache was touched, not the actual value (which was only initialized at the start of this test)
		assertEquals(valueToSet, propertyModel.getObject());
		
		propertyModel.writeAll();
		assertEquals(valueToSet, rootValue.getCollectionProperty());
		assertEquals(valueToSet, propertyModel.getObject());
	}

	@Test
	void cacheUpdatePropagation() {
		Collection<CollectionPropertyItemValue> firstExpectedValue = ImmutableList.of(new CollectionPropertyItemValue());
		Collection<CollectionPropertyItemValue> secondExpectedValue = ImmutableList.of(
				new CollectionPropertyItemValue(), new CollectionPropertyItemValue()
		);
		rootValue.setCollectionProperty(firstExpectedValue);

		doReturn(rootValue).when(rootModel).getObject();
		
		IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
		IBindableModel<Collection<CollectionPropertyItemValue>> propertyModel = bindableModel.bindCollectionWithCache(
				rootBinding().collectionProperty(),
				Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
		);
		
		assertEquals(firstExpectedValue, propertyModel.getObject());
		verify(rootValue).getCollectionProperty(); // Cache was initialized

		rootValue.setCollectionProperty(secondExpectedValue);
		
		bindableModel.readAll();
		verify(rootValue, times(2)).getCollectionProperty(); // The value was fetched
		
		assertEquals(secondExpectedValue, propertyModel.getObject()); // The cache was updated
		verify(rootValue, times(2)).getCollectionProperty(); // Only the cache was touched when calling getObject(), no the actual value
		
		Collection<CollectionPropertyItemValue> valueToSet = ImmutableList.of(
				new CollectionPropertyItemValue(), new CollectionPropertyItemValue(), new CollectionPropertyItemValue()
		);
		assertEquals(secondExpectedValue, rootValue.getCollectionProperty());
		propertyModel.setObject(valueToSet);
		assertEquals(secondExpectedValue, rootValue.getCollectionProperty());
				// Only the cache was touched, not the actual value (which was only initialized at the start of this test)
		assertEquals(valueToSet, propertyModel.getObject());
		
		bindableModel.writeAll();
		assertEquals(valueToSet, rootValue.getCollectionProperty());
		assertEquals(valueToSet, propertyModel.getObject());
	}

	@Test
	void collectionModelInterfaceUsage() {
		CollectionPropertyItemValue firstItem = new CollectionPropertyItemValue();
		CollectionPropertyItemValue secondItem = new CollectionPropertyItemValue();
		CollectionPropertyItemValue thirdItem = new CollectionPropertyItemValue();
		rootValue.setCollectionProperty(ImmutableList.of(firstItem, secondItem));
		
		doReturn(rootValue).when(rootModel).getObject();
		
		IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
		IBindableCollectionModel<CollectionPropertyItemValue, ?> propertyModel = bindableModel.bindCollectionWithCache(
				rootBinding().collectionProperty(),
				Suppliers2.<CollectionPropertyItemValue>arrayList(), StubModel.<CollectionPropertyItemValue>factory()
		);
		
		assertEquals(propertyModel.getObject(), ImmutableList.of(firstItem, secondItem)); // Cache was initialized
		
		propertyModel.remove(secondItem);
		assertEquals(ImmutableList.of(firstItem), propertyModel.getObject()); // The cache was updated
		assertEquals(ImmutableList.of(firstItem, secondItem), rootValue.getCollectionProperty()); // The actual value was *not* updated
		propertyModel.writeAll();
		assertEquals(ImmutableList.of(firstItem), propertyModel.getObject()); // The cache was *not* updated
		assertEquals(ImmutableList.of(firstItem), rootValue.getCollectionProperty()); // The actual value was updated

		propertyModel.add(thirdItem);
		assertEquals(ImmutableList.of(firstItem, thirdItem), propertyModel.getObject()); // The cache was updated
		assertEquals(ImmutableList.of(firstItem), rootValue.getCollectionProperty()); // The actual value was *not* updated
		propertyModel.writeAll();
		assertEquals(ImmutableList.of(firstItem, thirdItem), propertyModel.getObject()); // The cache was *not* updated
		assertEquals(ImmutableList.of(firstItem, thirdItem), rootValue.getCollectionProperty()); // The actual value was updated

		propertyModel.clear();
		assertEquals(ImmutableList.of(), propertyModel.getObject()); // The cache was updated
		assertEquals(ImmutableList.of(firstItem, thirdItem), rootValue.getCollectionProperty()); // The actual value was *not* updated
		propertyModel.writeAll();
		assertEquals(ImmutableList.of(), propertyModel.getObject()); // The cache was *not* updated
		assertEquals(ImmutableList.of(), rootValue.getCollectionProperty()); // The actual value was updated
	}

}
