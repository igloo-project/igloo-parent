package fr.openwide.core.jpa.more.business.difference.util;

import java.util.Collection;

import com.google.common.base.Function;

import fr.openwide.core.jpa.util.HibernateUtils;

public class CollectionElementsFunctionProxyInitializer<T, T2> implements IProxyInitializer<T> {
	private final Function<? super T, ? extends Collection<? extends T2>> function;
	private final IProxyInitializer<? super T2> delegate;

	public CollectionElementsFunctionProxyInitializer(Function<? super T, ? extends Collection<? extends T2>> function) {
		this(function, new FunctionProxyInitializer<>());
	}

	public CollectionElementsFunctionProxyInitializer(Function<? super T, ? extends Collection<? extends T2>> function,
			IProxyInitializer<? super T2> delegate) {
		super();
		this.function = function;
		this.delegate = delegate;
	}

	@Override
	public void initialize(T value) {
		Collection<? extends T2> collection  = function.apply(value);
		if (collection != null) {
			HibernateUtils.initialize(collection);
			for (T2 element : collection) {
				delegate.initialize(element);
			}
		}
	}
}