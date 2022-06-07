package org.iglooproject.wicket.more.markup.html.factory;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.condition.BooleanOperator;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.factory.IDetachableFactory;
import org.iglooproject.wicket.model.Detachables;
import org.springframework.security.acls.model.Permission;

import com.google.common.collect.Lists;

public final class ConditionFactories {

	private ConditionFactories() {
	}

	public static final <T> IDetachableFactory<T, Condition> constant(final Condition condition) {
		return new IDetachableFactory<T, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(T parameter) {
				return condition;
			}
			@Override
			public void detach() {
				IDetachableFactory.super.detach();
				Detachables.detach(condition);
			}
		};
	}
	
	public static final <T> IDetachableFactory<T, Condition> negate(final IDetachableFactory<T, Condition> factory) {
		return new IDetachableFactory<T, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(T parameter) {
				return factory.create(parameter).negate();
			}
			@Override
			public void detach() {
				IDetachableFactory.super.detach();
				Detachables.detach(factory);
			}
		};
	}

	@SafeVarargs
	public static final <T> IDetachableFactory<T, Condition> compose(
			final BooleanOperator operator,
			final IDetachableFactory<T, Condition> firstFactory,
			final IDetachableFactory<T, Condition> ... otherFactories
	) {
		return new IDetachableFactory<T, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(final T parameter) {
				return Condition.composite(
						operator,
						Lists.asList(firstFactory, otherFactories)
								.stream()
								.map(DetachableFactories.<T, Condition>toApplyFunction(parameter))
								::iterator
				);
			}
			@Override
			public void detach() {
				IDetachableFactory.super.detach();
				Detachables.detach(firstFactory, otherFactories);
			}
		};
	}

	@SafeVarargs
	public static final <T> IDetachableFactory<T, Condition> or(
			final IDetachableFactory<T, Condition> firstFactory,
			final IDetachableFactory<T, Condition> ... otherFactories
	) {
		return compose(BooleanOperator.OR, firstFactory, otherFactories);
	}

	@SafeVarargs
	public static final <T> IDetachableFactory<T, Condition> and(
			final IDetachableFactory<T, Condition> firstFactory,
			final IDetachableFactory<T, Condition> ... otherFactories
	) {
		return compose(BooleanOperator.AND, firstFactory, otherFactories);
	}

	public static final <T> IDetachableFactory<IModel<? extends T>, Condition> predicate(final SerializablePredicate2<? super T> predicate) {
		return new IDetachableFactory<IModel<? extends T>, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<? extends T> parameter) {
				return Condition.predicate(parameter, predicate);
			}
		};
	}

	public static final <T> IDetachableFactory<IModel<? extends T>, Condition> permission(final String permission) {
		return new IDetachableFactory<IModel<? extends T>, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<? extends T> parameter) {
				return Condition.permission(parameter, permission);
			}
		};
	}

	public static final <T> IDetachableFactory<IModel<? extends T>, Condition> permission(final Permission permission) {
		return new IDetachableFactory<IModel<? extends T>, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<? extends T> parameter) {
				return Condition.permission(parameter, permission);
			}
		};
	}

}
