package fr.openwide.core.commons.util.binding;

import org.bindgen.BindingRoot;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public interface ICoreBinding<R, T> extends BindingRoot<R, T>, Function<R, T> {

	/**
	 * Wrapper for {@link #getSafelyWithRoot(Object)} as a {@link Function}.
	 */
	@Override
	T apply(R input);

	/**
	 * Shorthand for {@code Predicates.compose(predicate, (Function<R, T>) this)}
	 */
	Predicate<R> compose(Predicate<? super T> predicate);

}
