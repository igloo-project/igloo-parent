package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

public interface IActionColumnAddedActionState<T, I> extends IActionColumnAddedElementState<T, I> {

  @Override
  IActionColumnAddedActionState<T, I> showLabel();

  @Override
  IActionColumnAddedActionState<T, I> showLabel(Condition showLabelCondition);

  @Override
  IActionColumnAddedActionState<T, I> hideLabel();

  @Override
  IActionColumnAddedActionState<T, I> hideLabel(Condition hideLabelCondition);

  @Override
  IActionColumnAddedActionState<T, I> showTooltip();

  @Override
  IActionColumnAddedActionState<T, I> showTooltip(Condition showTooltipCondition);

  @Override
  IActionColumnAddedActionState<T, I> hideTooltip();

  @Override
  IActionColumnAddedActionState<T, I> hideTooltip(Condition hideTooltipCondition);

  @Override
  IActionColumnAddedActionState<T, I> showIcon();

  @Override
  IActionColumnAddedActionState<T, I> showIcon(Condition showIconCondition);

  @Override
  IActionColumnAddedActionState<T, I> hideIcon();

  @Override
  IActionColumnAddedActionState<T, I> hideIcon(Condition hideIconCondition);

  @Override
  IActionColumnAddedActionState<T, I> showPlaceholder();

  @Override
  IActionColumnAddedActionState<T, I> showPlaceholder(Condition showPlaceholderCondition);

  @Override
  IActionColumnAddedActionState<T, I> showPlaceholder(
      IDetachableFactory<? super IModel<? extends T>, Condition> showPlaceholderConditionFactory);

  @Override
  IActionColumnAddedActionState<T, I> hidePlaceholder();

  @Override
  IActionColumnAddedActionState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

  @Override
  IActionColumnAddedActionState<T, I> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  @Override
  IActionColumnAddedActionState<T, I> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  @Override
  IActionColumnAddedActionState<T, I> withClass(IModel<? extends String> valueModel);

  @Override
  IActionColumnAddedActionState<T, I> withClass(String firstValue, String... otherValues);

  @Override
  IActionColumnAddedActionState<T, I> when(
      IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

  @Override
  IActionColumnAddedActionState<T, I> when(Condition condition);

  @Override
  IActionColumnAddedActionState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

  @Override
  IActionColumnAddedActionState<T, I> whenPermission(String permission);

  @Override
  IActionColumnAddedActionState<T, I> whenPermission(Permission permission);

  @Override
  IActionColumnAddedActionState<T, I> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories);

  @Override
  IActionColumnAddedActionState<T, I> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

  @Override
  IActionColumnAddedActionState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);
}
