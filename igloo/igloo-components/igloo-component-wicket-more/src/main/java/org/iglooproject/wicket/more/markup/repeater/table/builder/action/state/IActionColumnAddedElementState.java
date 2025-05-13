package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

public interface IActionColumnAddedElementState<T, I> extends IActionColumnAddedBaseState<T, I> {

  IActionColumnAddedElementState<T, I> showLabel();

  IActionColumnAddedElementState<T, I> showLabel(Condition showLabelCondition);

  IActionColumnAddedElementState<T, I> hideLabel();

  IActionColumnAddedElementState<T, I> hideLabel(Condition hideLabelCondition);

  IActionColumnAddedElementState<T, I> showTooltip();

  IActionColumnAddedElementState<T, I> showTooltip(Condition showTooltipCondition);

  IActionColumnAddedElementState<T, I> hideTooltip();

  IActionColumnAddedElementState<T, I> hideTooltip(Condition hideTooltipCondition);

  IActionColumnAddedElementState<T, I> showIcon();

  IActionColumnAddedElementState<T, I> showIcon(Condition showIconCondition);

  IActionColumnAddedElementState<T, I> hideIcon();

  IActionColumnAddedElementState<T, I> hideIcon(Condition hideIconCondition);

  IActionColumnAddedElementState<T, I> showPlaceholder();

  IActionColumnAddedElementState<T, I> showPlaceholder(Condition showPlaceholderCondition);

  IActionColumnAddedElementState<T, I> showPlaceholder(
      IDetachableFactory<? super IModel<? extends T>, Condition> showPlaceholderConditionFactory);

  IActionColumnAddedElementState<T, I> hidePlaceholder();

  IActionColumnAddedElementState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

  @Override
  IActionColumnAddedElementState<T, I> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  @Override
  IActionColumnAddedElementState<T, I> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  @Override
  IActionColumnAddedElementState<T, I> withClass(IModel<? extends String> valueModel);

  @Override
  IActionColumnAddedElementState<T, I> withClass(String firstValue, String... otherValues);

  @Override
  IActionColumnAddedElementState<T, I> when(
      IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

  @Override
  IActionColumnAddedElementState<T, I> when(Condition condition);

  @Override
  IActionColumnAddedElementState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

  @Override
  IActionColumnAddedElementState<T, I> whenPermission(String permission);

  @Override
  IActionColumnAddedElementState<T, I> whenPermission(Permission permission);

  @Override
  IActionColumnAddedElementState<T, I> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories);

  @Override
  IActionColumnAddedElementState<T, I> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

  @Override
  IActionColumnAddedElementState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);
}
