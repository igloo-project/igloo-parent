package test.wicket.more.bindable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.bindable.model.IBindableMapModel;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.junit.jupiter.api.Test;
import test.wicket.more.config.spring.SpringBootTestWicketSimple;

@SpringBootTestWicketSimple
class TestBindableMapModel extends AbstractTestBindableModel {

  @Test
  void alwaysReturnsSameModelInstance() {
    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<?> firstCall =
        bindableModel.bindMapWithCache(
            rootBinding().mapProperty(),
            Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
            StubModel.<MapPropertyItemKey>factory(),
            StubModel.<MapPropertyItemValue>factory());
    IBindableModel<?> secondCall = bindableModel.bindMapAlreadyAdded(rootBinding().mapProperty());
    assertSame(firstCall, secondCall);
    IBindableModel<?> thirdCall =
        bindableModel.bindMapWithCache(
            rootBinding().mapProperty(),
            Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
            StubModel.<MapPropertyItemKey>factory(),
            StubModel.<MapPropertyItemValue>factory());
    assertSame(firstCall, thirdCall);
  }

  @Test
  void alwaysReturnsSameModelInstanceEvenIfChained() {
    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<?> directCall =
        bindableModel.bindMapWithCache(
            rootBinding().compositeProperty().mapProperty(),
            Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
            StubModel.<MapPropertyItemKey>factory(),
            StubModel.<MapPropertyItemValue>factory());
    IBindableModel<?> chainedCall =
        bindableModel
            .bind(rootBinding().compositeProperty())
            .bindMapAlreadyAdded(rootBinding().mapProperty());
    assertSame(directCall, chainedCall);
    IBindableModel<?> chainedCallWithCache =
        bindableModel
            .bind(rootBinding().compositeProperty())
            .bindMapWithCache(
                rootBinding().mapProperty(),
                Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
                StubModel.<MapPropertyItemKey>factory(),
                StubModel.<MapPropertyItemValue>factory());
    assertSame(directCall, chainedCallWithCache);
  }

  @Test
  void simpleCacheUsage() {
    Map<MapPropertyItemKey, MapPropertyItemValue> firstExpectedValue =
        ImmutableMap.of(new MapPropertyItemKey(), new MapPropertyItemValue());
    Map<MapPropertyItemKey, MapPropertyItemValue> secondExpectedValue =
        ImmutableMap.of(
            new MapPropertyItemKey(), new MapPropertyItemValue(),
            new MapPropertyItemKey(), new MapPropertyItemValue());
    rootValue.setMapProperty(firstExpectedValue);

    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<Map<MapPropertyItemKey, MapPropertyItemValue>> propertyModel =
        bindableModel.bindMapWithCache(
            rootBinding().mapProperty(),
            Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
            StubModel.<MapPropertyItemKey>factory(),
            StubModel.<MapPropertyItemValue>factory());

    assertEquals(firstExpectedValue, propertyModel.getObject());
    verify(rootValue).getMapProperty(); // Cache was initialized

    rootValue.setMapProperty(secondExpectedValue);

    propertyModel.readAll();
    verify(rootValue, times(2)).getMapProperty(); // The value was fetched

    assertEquals(secondExpectedValue, propertyModel.getObject()); // The cache was updated
    verify(rootValue, times(2)).getMapProperty(); // Only the cache was touched, no the actual value

    Map<MapPropertyItemKey, MapPropertyItemValue> valueToSet =
        ImmutableMap.of(
            new MapPropertyItemKey(), new MapPropertyItemValue(),
            new MapPropertyItemKey(), new MapPropertyItemValue(),
            new MapPropertyItemKey(), new MapPropertyItemValue());
    assertEquals(secondExpectedValue, rootValue.getMapProperty());
    propertyModel.setObject(valueToSet);
    assertEquals(secondExpectedValue, rootValue.getMapProperty());
    // Only the cache was touched, not the actual value (which was only initialized at the start of
    // this test)
    assertEquals(valueToSet, propertyModel.getObject());

    propertyModel.writeAll();
    assertEquals(valueToSet, rootValue.getMapProperty());
    assertEquals(valueToSet, propertyModel.getObject());
  }

  @Test
  void cacheUpdatePropagation() {
    Map<MapPropertyItemKey, MapPropertyItemValue> firstExpectedValue =
        ImmutableMap.of(new MapPropertyItemKey(), new MapPropertyItemValue());
    Map<MapPropertyItemKey, MapPropertyItemValue> secondExpectedValue =
        ImmutableMap.of(
            new MapPropertyItemKey(), new MapPropertyItemValue(),
            new MapPropertyItemKey(), new MapPropertyItemValue());
    rootValue.setMapProperty(firstExpectedValue);

    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<Map<MapPropertyItemKey, MapPropertyItemValue>> propertyModel =
        bindableModel.bindMapWithCache(
            rootBinding().mapProperty(),
            Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
            StubModel.<MapPropertyItemKey>factory(),
            StubModel.<MapPropertyItemValue>factory());

    assertEquals(firstExpectedValue, propertyModel.getObject());
    verify(rootValue).getMapProperty(); // Cache was initialized

    rootValue.setMapProperty(secondExpectedValue);

    bindableModel.readAll();
    verify(rootValue, times(2)).getMapProperty(); // The value was fetched

    assertEquals(secondExpectedValue, propertyModel.getObject()); // The cache was updated
    verify(rootValue, times(2))
        .getMapProperty(); // Only the cache was touched when calling getObject(), no the actual
    // value

    Map<MapPropertyItemKey, MapPropertyItemValue> valueToSet =
        ImmutableMap.of(
            new MapPropertyItemKey(), new MapPropertyItemValue(),
            new MapPropertyItemKey(), new MapPropertyItemValue(),
            new MapPropertyItemKey(), new MapPropertyItemValue());
    assertEquals(secondExpectedValue, rootValue.getMapProperty());
    propertyModel.setObject(valueToSet);
    assertEquals(secondExpectedValue, rootValue.getMapProperty());
    // Only the cache was touched, not the actual value (which was only initialized at the start of
    // this test)
    assertEquals(valueToSet, propertyModel.getObject());

    bindableModel.writeAll();
    assertEquals(valueToSet, rootValue.getMapProperty());
    assertEquals(valueToSet, propertyModel.getObject());
  }

  @Test
  void mapModelInterfaceUsage() {
    MapPropertyItemKey firstItemKey = new MapPropertyItemKey();
    MapPropertyItemKey secondItemKey = new MapPropertyItemKey();
    MapPropertyItemKey thirdItemKey = new MapPropertyItemKey();
    MapPropertyItemValue firstItemValue = new MapPropertyItemValue();
    MapPropertyItemValue secondItemValue = new MapPropertyItemValue();
    MapPropertyItemValue thirdItemValue = new MapPropertyItemValue();
    rootValue.setMapProperty(
        ImmutableMap.of(firstItemKey, firstItemValue, secondItemKey, secondItemValue));

    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableMapModel<MapPropertyItemKey, MapPropertyItemValue, ?> propertyModel =
        bindableModel.bindMapWithCache(
            rootBinding().mapProperty(),
            Suppliers2.<MapPropertyItemKey, MapPropertyItemValue>linkedHashMap(),
            StubModel.<MapPropertyItemKey>factory(),
            StubModel.<MapPropertyItemValue>factory());

    assertEquals(
        propertyModel.getObject(),
        ImmutableMap.of(
            firstItemKey, firstItemValue, secondItemKey, secondItemValue)); // Cache was initialized

    propertyModel.remove(secondItemKey);
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue),
        propertyModel.getObject()); // The cache was updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, secondItemKey, secondItemValue),
        rootValue.getMapProperty()); // The actual value was *not* updated
    propertyModel.writeAll();
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue),
        propertyModel.getObject()); // The cache was *not* updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue),
        rootValue.getMapProperty()); // The actual value was updated

    propertyModel.put(thirdItemKey, thirdItemValue);
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, thirdItemValue),
        propertyModel.getObject()); // The cache was updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue),
        rootValue.getMapProperty()); // The actual value was *not* updated
    propertyModel.writeAll();
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, thirdItemValue),
        propertyModel.getObject()); // The cache was *not* updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, thirdItemValue),
        rootValue.getMapProperty()); // The actual value was updated

    propertyModel.put(thirdItemKey, secondItemValue);
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, secondItemValue),
        propertyModel.getObject()); // The cache was updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, thirdItemValue),
        rootValue.getMapProperty()); // The actual value was *not* updated
    propertyModel.writeAll();
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, secondItemValue),
        propertyModel.getObject()); // The cache was *not* updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, secondItemValue),
        rootValue.getMapProperty()); // The actual value was updated

    propertyModel.clear();
    assertEquals(ImmutableMap.of(), propertyModel.getObject()); // The cache was updated
    assertEquals(
        ImmutableMap.of(firstItemKey, firstItemValue, thirdItemKey, secondItemValue),
        rootValue.getMapProperty()); // The actual value was *not* updated
    propertyModel.writeAll();
    assertEquals(ImmutableMap.of(), propertyModel.getObject()); // The cache was *not* updated
    assertEquals(ImmutableMap.of(), rootValue.getMapProperty()); // The actual value was updated
  }
}
