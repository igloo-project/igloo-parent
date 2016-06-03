package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IModel;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.util.model.Detachables;

public final class ConditionFactories {

	private ConditionFactories() {
	}

	public static final <T> IDetachableFactory<T, Condition> constant(final Condition condition) {
		return new AbstractDetachableFactory<T, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(T parameter) {
				return condition;
			}
			@Override
			public void detach() {
				super.detach();
				Detachables.detach(condition);
			}
		};
	}
	
	public static final <T> IDetachableFactory<T, Condition> negate(final IDetachableFactory<T, Condition> factory) {
		return new AbstractDetachableFactory<T, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(T parameter) {
				return factory.create(parameter).negate();
			}
			@Override
			public void detach() {
				super.detach();
				Detachables.detach(factory);
			}
		};
	}

	public static final <T> IDetachableFactory<IModel<? extends T>, Condition> predicate(final Predicate<? super T> predicate) {
		return new AbstractDetachableFactory<IModel<? extends T>, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<? extends T> parameter) {
				return Condition.predicate(parameter, predicate);
			}
		};
	}

	public static final <T> IDetachableFactory<IModel<? extends T>, Condition> permission(final String permission) {
		return new AbstractDetachableFactory<IModel<? extends T>, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<? extends T> parameter) {
				return Condition.permission(parameter, permission);
			}
		};
	}

	public static final <T> IDetachableFactory<IModel<? extends T>, Condition> permission(final Permission permission) {
		return new AbstractDetachableFactory<IModel<? extends T>, Condition>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<? extends T> parameter) {
				return Condition.permission(parameter, permission);
			}
		};
	}

}
