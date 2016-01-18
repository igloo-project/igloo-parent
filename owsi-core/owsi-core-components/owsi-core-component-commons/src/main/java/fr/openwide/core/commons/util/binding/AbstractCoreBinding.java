package fr.openwide.core.commons.util.binding;

import org.bindgen.binding.AbstractBinding;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public abstract class AbstractCoreBinding<R, T> extends AbstractBinding<R, T> implements ICoreBinding<R, T> {

	private static final long serialVersionUID = 6398238108193037144L;

	@Override
	public T apply(R input) {
		return getSafelyWithRoot(input);
	}

	@Override
	public Predicate<R> compose(Predicate<? super T> predicate) {
		return Predicates.compose(predicate, this);
	}
	
}
