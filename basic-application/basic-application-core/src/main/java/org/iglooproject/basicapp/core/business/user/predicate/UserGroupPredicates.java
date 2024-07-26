package org.iglooproject.basicapp.core.business.user.predicate;

import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

public final class UserGroupPredicates {

  public static SerializablePredicate2<UserGroup> locked() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.userGroup().locked()));
  }

  public static SerializablePredicate2<UserGroup> unlocked() {
    return Predicates2.notNullAndNot(locked());
  }

  private UserGroupPredicates() {}
}
