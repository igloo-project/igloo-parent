package org.iglooproject.basicapp.core.business.user.predicate;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

public final class UserPredicates {

	public static SerializablePredicate2<User> enabled() {
		return Predicates2.notNullAnd(
			Predicates2.compose(Predicates2.isTrue(), Bindings.user().enabled())
		);
	}

	public static SerializablePredicate2<User> disabled() {
		return Predicates2.notNullAndNot(enabled());
	}

	private UserPredicates() {
	}

}
