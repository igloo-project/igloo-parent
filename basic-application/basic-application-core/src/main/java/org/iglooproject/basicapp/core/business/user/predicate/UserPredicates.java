package org.iglooproject.basicapp.core.business.user.predicate;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

public final class UserPredicates {

	public static SerializablePredicate2<User> active() {
		return Predicates2.notNullAnd(
			Predicates2.compose(Predicates2.isTrue(), Bindings.user().active())
		);
	}

	public static SerializablePredicate2<User> inactive() {
		return Predicates2.notNullAndNot(active());
	}

	private UserPredicates() {
	}

}
