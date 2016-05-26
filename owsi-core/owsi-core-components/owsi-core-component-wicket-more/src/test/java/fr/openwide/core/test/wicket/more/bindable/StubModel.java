package fr.openwide.core.test.wicket.more.bindable;

import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

class StubModel<T> implements IModel<T> {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static <T> Function<T, StubModel<T>> factory() {
		return (Function<T, StubModel<T>>) (Object) Factory.INSTANCE;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"}) // SerializableModelFactory works for any T extending Serializable
	private enum Factory implements Function<Object, StubModel> {
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