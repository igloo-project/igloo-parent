package basicapp.back.business.referencedata.predicate;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

public final class ReferenceDataPredicates {

  public static SerializablePredicate2<ReferenceData<?>> enabled() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.referenceData().enabled()));
  }

  public static SerializablePredicate2<ReferenceData<?>> disabled() {
    return Predicates2.notNullAndNot(enabled());
  }

  public static SerializablePredicate2<ReferenceData<?>> editable() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.referenceData().editable()));
  }

  public static SerializablePredicate2<ReferenceData<?>> disableable() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.referenceData().disableable()));
  }

  public static SerializablePredicate2<ReferenceData<?>> deleteable() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.referenceData().deleteable()));
  }

  private ReferenceDataPredicates() {}
}
