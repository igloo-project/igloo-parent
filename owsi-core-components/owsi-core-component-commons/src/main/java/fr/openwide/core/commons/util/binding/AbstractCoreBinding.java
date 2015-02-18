package fr.openwide.core.commons.util.binding;

import org.bindgen.binding.AbstractBinding;

import com.google.common.base.Function;

public abstract class AbstractCoreBinding<R, T> extends AbstractBinding<R, T> implements Function<R, T> {

	private static final long serialVersionUID = 6398238108193037144L;

	@Override
	public T apply(R input) {
		return getSafelyWithRoot(input);
	}
	
}
