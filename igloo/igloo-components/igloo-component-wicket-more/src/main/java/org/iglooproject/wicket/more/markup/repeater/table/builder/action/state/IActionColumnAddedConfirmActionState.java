package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

public interface IActionColumnAddedConfirmActionState<T, I>
    extends IActionColumnAddedElementState<T, I> {

  @Override
  IActionColumnAddedConfirmActionState<T, I> showLabel();

  @Override
  IActionColumnAddedConfirmActionState<T, I> showLabel(Condition showLabelCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> hideLabel();

  @Override
  IActionColumnAddedConfirmActionState<T, I> hideLabel(Condition hideLabelCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> showTooltip();

  @Override
  IActionColumnAddedConfirmActionState<T, I> showTooltip(Condition showTooltipCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> hideTooltip();

  @Override
  IActionColumnAddedConfirmActionState<T, I> hideTooltip(Condition hideTooltipCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> showIcon();

  @Override
  IActionColumnAddedConfirmActionState<T, I> showIcon(Condition showIconCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> hideIcon();

  @Override
  IActionColumnAddedConfirmActionState<T, I> hideIcon(Condition hideIconCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> showPlaceholder();

  @Override
  IActionColumnAddedConfirmActionState<T, I> showPlaceholder(Condition showPlaceholderCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> hidePlaceholder();

  @Override
  IActionColumnAddedConfirmActionState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  @Override
  IActionColumnAddedConfirmActionState<T, I> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  @Override
  IActionColumnAddedConfirmActionState<T, I> withClass(IModel<? extends String> valueModel);

  @Override
  IActionColumnAddedConfirmActionState<T, I> withClass(String firstValue, String... otherValues);

  @Override
  IActionColumnAddedConfirmActionState<T, I> when(
      IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

  @Override
  IActionColumnAddedConfirmActionState<T, I> when(Condition condition);

  @Override
  IActionColumnAddedConfirmActionState<T, I> whenPredicate(
      SerializablePredicate2<? super T> predicate);

  @Override
  IActionColumnAddedConfirmActionState<T, I> whenPermission(String permission);

  @Override
  IActionColumnAddedConfirmActionState<T, I> whenPermission(Permission permission);

  @Override
  IActionColumnAddedConfirmActionState<T, I> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories);

  @Override
  IActionColumnAddedConfirmActionState<T, I> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

  @Override
  IActionColumnAddedConfirmActionState<T, I> add(
      Behavior firstBehavior, Behavior... otherBehaviors);
}
