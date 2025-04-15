package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

public interface IActionColumnAddedBaseState<T, I>
    extends IActionColumnNoParameterBuildState<T, I>, IActionColumnBuildState<T, I> {

  IActionColumnAddedBaseState<T, I> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  IActionColumnAddedBaseState<T, I> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  IActionColumnAddedBaseState<T, I> withClass(IModel<? extends String> valueModel);

  IActionColumnAddedBaseState<T, I> withClass(String firstValue, String... otherValues);

  IActionColumnAddedBaseState<T, I> when(
      IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

  IActionColumnAddedBaseState<T, I> when(Condition condition);

  IActionColumnAddedBaseState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

  IActionColumnAddedBaseState<T, I> whenPermission(String permission);

  IActionColumnAddedBaseState<T, I> whenPermission(Permission permission);

  IActionColumnAddedBaseState<T, I> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories);

  IActionColumnAddedBaseState<T, I> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

  IActionColumnAddedBaseState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);
}
