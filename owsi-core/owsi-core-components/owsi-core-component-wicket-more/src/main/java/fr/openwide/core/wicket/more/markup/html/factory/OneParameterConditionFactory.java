package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IModel;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.util.model.Detachables;

public final class OneParameterConditionFactory {

	private OneParameterConditionFactory() {
	}

	public static final <T> IOneParameterConditionFactory<T> identity(final Condition condition) {
		return new AbstractOneParameterConditionFactory<T>() {
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

	public static final <T> IOneParameterConditionFactory<IModel<T>> predicate(final Predicate<? super T> predicate) {
		return new AbstractOneParameterConditionFactory<IModel<T>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<T> parameter) {
				return Condition.predicate(parameter, predicate);
			}
		};
	}

	public static final <T> IOneParameterConditionFactory<IModel<T>> permission(final String permission) {
		return new AbstractOneParameterConditionFactory<IModel<T>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<T> parameter) {
				return Condition.permission(parameter, permission);
			}
		};
	}

	public static final <T> IOneParameterConditionFactory<IModel<T>> permission(final Permission permission) {
		return new AbstractOneParameterConditionFactory<IModel<T>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Condition create(IModel<T> parameter) {
				return Condition.permission(parameter, permission);
			}
		};
	}

}
