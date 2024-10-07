package basicapp.back.business.user.predicate;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

public final class UserPredicates {

  private static SerializablePredicate2<User> type(UserType type) {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.equalTo(type), Bindings.user().type()));
  }

  public static SerializablePredicate2<User> technical() {
    return type(UserType.TECHNICAL);
  }

  public static SerializablePredicate2<User> basic() {
    return type(UserType.BASIC);
  }

  public static SerializablePredicate2<User> enabled() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.user().enabled()));
  }

  public static SerializablePredicate2<User> disabled() {
    return Predicates2.notNullAndNot(enabled());
  }

  public static SerializablePredicate2<User> announcementOpen() {
    return Predicates2.notNullAnd(
        Predicates2.compose(
            Predicates2.isTrue(), Bindings.user().announcementInformation().open()));
  }

  public static SerializablePredicate2<User> announcementClose() {
    return Predicates2.notNullAndNot(announcementOpen());
  }

  private UserPredicates() {}
}
