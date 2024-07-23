package test.wicket.more.bindable;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.junit.jupiter.api.Test;

class TestBindableModel extends AbstractTestBindableModel {

  @Test
  void readAccessToProperties() {
    SimplePropertyValue expectedPropertyValue = new SimplePropertyValue();
    rootValue.setSimpleProperty(expectedPropertyValue);

    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<SimplePropertyValue> propertyModel =
        bindableModel.bind(rootBinding().simpleProperty());

    assertSame(propertyModel.getObject(), expectedPropertyValue);
    verify(rootValue, times(1)).getSimpleProperty();
  }

  @Test
  void writeAccessToProperties() {
    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<SimplePropertyValue> propertyModel =
        bindableModel.bind(rootBinding().simpleProperty());

    SimplePropertyValue newExpectedPropertyValue = new SimplePropertyValue();
    propertyModel.setObject(newExpectedPropertyValue);
    verify(rootValue, times(1)).setSimpleProperty(newExpectedPropertyValue);
  }

  @Test
  void alwaysReturnsSameModelInstance() {
    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<?> firstCall = bindableModel.bind(rootBinding().simpleProperty());
    IBindableModel<?> secondCall = bindableModel.bind(rootBinding().simpleProperty());
    assertSame(firstCall, secondCall);
    IBindableModel<?> thirdCall =
        bindableModel.bindWithCache(
            rootBinding().simpleProperty(), new StubModel<SimplePropertyValue>());
    assertSame(firstCall, thirdCall);
  }

  @Test
  void alwaysReturnsSameModelInstanceEvenIfChained() {
    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<?> directCall =
        bindableModel.bind(rootBinding().compositeProperty().simpleProperty());
    IBindableModel<?> chainedCall =
        bindableModel.bind(rootBinding().compositeProperty()).bind(rootBinding().simpleProperty());
    assertSame(directCall, chainedCall);
    IBindableModel<?> chainedCallWithCache =
        bindableModel
            .bind(rootBinding().compositeProperty())
            .bindWithCache(rootBinding().simpleProperty(), new StubModel<SimplePropertyValue>());
    assertSame(directCall, chainedCallWithCache);
  }

  @Test
  void simpleCacheUsage() {
    SimplePropertyValue firstExpectedValue = new SimplePropertyValue();
    SimplePropertyValue secondExpectedValue = new SimplePropertyValue();
    rootValue.setSimpleProperty(firstExpectedValue);

    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<SimplePropertyValue> propertyModel =
        bindableModel.bindWithCache(
            rootBinding().simpleProperty(), new StubModel<SimplePropertyValue>());

    assertSame(firstExpectedValue, propertyModel.getObject());
    verify(rootValue).getSimpleProperty(); // Cache was initialized

    rootValue.setSimpleProperty(secondExpectedValue);

    propertyModel.readAll();
    verify(rootValue, times(2)).getSimpleProperty(); // The value was fetched

    assertSame(secondExpectedValue, propertyModel.getObject()); // The cache was updated
    verify(rootValue, times(2))
        .getSimpleProperty(); // Only the cache was touched when calling getObject(), no the actual
    // value

    SimplePropertyValue valueToSet = new SimplePropertyValue();
    assertSame(secondExpectedValue, rootValue.getSimpleProperty());
    propertyModel.setObject(valueToSet);
    assertSame(secondExpectedValue, rootValue.getSimpleProperty());
    // Only the cache was touched, not the actual value (which was only initialized at the start of
    // this test)
    assertSame(valueToSet, propertyModel.getObject());

    propertyModel.writeAll();
    assertSame(valueToSet, rootValue.getSimpleProperty());
    assertSame(valueToSet, propertyModel.getObject());
  }

  @Test
  void cacheUpdatePropagation() {
    SimplePropertyValue firstExpectedValue = new SimplePropertyValue();
    SimplePropertyValue secondExpectedValue = new SimplePropertyValue();
    rootValue.setSimpleProperty(firstExpectedValue);

    doReturn(rootValue).when(rootModel).getObject();

    IBindableModel<RootValue> bindableModel = new BindableModel<>(rootModel);
    IBindableModel<SimplePropertyValue> propertyModel =
        bindableModel.bindWithCache(
            rootBinding().simpleProperty(), new StubModel<SimplePropertyValue>());

    assertSame(firstExpectedValue, propertyModel.getObject());
    verify(rootValue).getSimpleProperty(); // Cache was initialized

    rootValue.setSimpleProperty(secondExpectedValue);

    bindableModel.readAll();
    verify(rootValue, times(2)).getSimpleProperty(); // The value was fetched

    assertSame(secondExpectedValue, propertyModel.getObject()); // The cache was updated
    verify(rootValue, times(2))
        .getSimpleProperty(); // Only the cache was touched when calling getObject(), no the actual
    // value

    SimplePropertyValue valueToSet = new SimplePropertyValue();
    assertSame(secondExpectedValue, rootValue.getSimpleProperty());
    propertyModel.setObject(valueToSet);
    assertSame(secondExpectedValue, rootValue.getSimpleProperty());
    // Only the cache was touched, not the actual value (which was only initialized at the start of
    // this test)
    assertSame(valueToSet, propertyModel.getObject());

    bindableModel.writeAll();
    assertSame(valueToSet, rootValue.getSimpleProperty());
    assertSame(valueToSet, propertyModel.getObject());
  }
}
