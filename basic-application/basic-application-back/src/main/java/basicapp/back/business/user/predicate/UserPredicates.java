package basicapp.back.business.user.predicate;

import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

public final class UserPredicates {

  public static SerializablePredicate2<User> enabled() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.user().enabled()));
  }

  public static SerializablePredicate2<User> disabled() {
    return Predicates2.notNullAndNot(enabled());
  }

  private UserPredicates() {}
}
