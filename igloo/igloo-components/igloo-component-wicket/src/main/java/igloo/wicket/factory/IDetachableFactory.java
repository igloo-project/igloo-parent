package igloo.wicket.factory;

import org.apache.wicket.model.IDetachable;
import org.iglooproject.functional.SerializableFunction2;

public interface IDetachableFactory<T, R> extends SerializableFunction2<T, R>, IDetachable {

	R create(T parameter);

	/**
	 * @deprecated Use {@link #create(T)} instead.
	 */
	@Deprecated
	@Override
	default R apply(T parameter) {
		return create(parameter);
	}

	@Override
	default void detach() {
		// nothing to do
	}

}
