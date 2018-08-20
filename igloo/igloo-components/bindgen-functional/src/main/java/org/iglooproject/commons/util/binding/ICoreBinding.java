package org.iglooproject.commons.util.binding;

import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializablePredicate2;

import com.google.common.base.Predicate;


public interface ICoreBinding<R, T> extends BindingRoot<R, T>, SerializableFunction2<R, T> {

	/**
	 * Wrapper for {@link #getSafelyWithRoot(Object)} as a {@link SerializableFunction2}.
	 */
	@Override
	T apply(R input);

	/**
	 * Shorthand for {@code Predicates2.compose(predicate, (SerializableFunction2<R, T>) this)}
	 * 
	 * @param predicate to compose with self
	 * @return composition of passed predicate with *this* {@link SerializableFunction2}
	 */
	SerializablePredicate2<R> compose(Predicate<? super T> predicate);

}
