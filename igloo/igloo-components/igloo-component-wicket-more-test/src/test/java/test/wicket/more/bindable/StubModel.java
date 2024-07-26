package test.wicket.more.bindable;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;

class StubModel<T> implements IModel<T> {
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unchecked")
  public static <T> SerializableFunction2<T, StubModel<T>> factory() {
    return (SerializableFunction2<T, StubModel<T>>) (Object) Factory.INSTANCE;
  }

  @SuppressWarnings({
    "rawtypes",
    "unchecked"
  }) // SerializableModelFactory works for any T extending Serializable
  private enum Factory implements SerializableFunction2<Object, StubModel> {
    INSTANCE;

    @Override
    public StubModel apply(Object input) {
      return new StubModel(input);
    }
  }

  private T value;

  public StubModel() {
    super();
  }

  public StubModel(T value) {
    super();
    this.value = value;
  }

  @Override
  public void detach() {
    // Does nothing
  }

  @Override
  public T getObject() {
    return value;
  }

  @Override
  public void setObject(T object) {
    this.value = object;
  }
}
