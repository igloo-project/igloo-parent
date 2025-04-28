package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

public interface IActionColumnAddedPlaceholderState<T, I>
    extends IActionColumnAddedBaseState<T, I> {

  IActionColumnAddedPlaceholderState<T, I> showLabel();

  IActionColumnAddedPlaceholderState<T, I> showLabel(Condition showLabelCondition);

  IActionColumnAddedPlaceholderState<T, I> hideLabel();

  IActionColumnAddedPlaceholderState<T, I> hideLabel(Condition hideLabelCondition);

  IActionColumnAddedPlaceholderState<T, I> showIcon();

  IActionColumnAddedPlaceholderState<T, I> showIcon(Condition showIconCondition);

  IActionColumnAddedPlaceholderState<T, I> hideIcon();

  IActionColumnAddedPlaceholderState<T, I> hideIcon(Condition hideIconCondition);

  @Override
  IActionColumnAddedPlaceholderState<T, I> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  @Override
  IActionColumnAddedPlaceholderState<T, I> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  @Override
  IActionColumnAddedPlaceholderState<T, I> withClass(IModel<? extends String> valueModel);

  @Override
  IActionColumnAddedPlaceholderState<T, I> withClass(String firstValue, String... otherValues);

  @Override
  IActionColumnAddedPlaceholderState<T, I> when(
      IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

  @Override
  IActionColumnAddedPlaceholderState<T, I> when(Condition condition);

  @Override
  IActionColumnAddedPlaceholderState<T, I> whenPredicate(
      SerializablePredicate2<? super T> predicate);

  @Override
  IActionColumnAddedPlaceholderState<T, I> whenPermission(String permission);

  @Override
  IActionColumnAddedPlaceholderState<T, I> whenPermission(Permission permission);

  @Override
  IActionColumnAddedPlaceholderState<T, I> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories);

  @Override
  IActionColumnAddedPlaceholderState<T, I> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

  @Override
  IActionColumnAddedPlaceholderState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);
}
