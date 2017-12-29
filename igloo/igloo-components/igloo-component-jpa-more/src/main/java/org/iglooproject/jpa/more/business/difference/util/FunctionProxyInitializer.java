package org.iglooproject.jpa.more.business.difference.util;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import org.iglooproject.jpa.util.HibernateUtils;

public class FunctionProxyInitializer<T> implements IProxyInitializer<T> {
	private final Iterable<? extends Function<? super T, ?>> functions;

	@SafeVarargs
	public FunctionProxyInitializer(Function<? super T, ?> ... functions) {
		super();
		this.functions = ImmutableList.copyOf(functions);
	}

	public FunctionProxyInitializer(Iterable<? extends Function<? super T, ?>> functions) {
		super();
		this.functions = ImmutableList.copyOf(functions);
	}

	@Override
	public void initialize(T value) {
		HibernateUtils.initialize(value);
		for (Function<? super T, ?> function : functions) {
			HibernateUtils.initialize(function.apply(value));
		}
	}
}