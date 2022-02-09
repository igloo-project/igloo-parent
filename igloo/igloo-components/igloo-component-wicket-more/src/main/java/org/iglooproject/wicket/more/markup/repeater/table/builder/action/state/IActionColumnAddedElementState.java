package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import java.util.Collection;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.api.factory.IDetachableFactory;
import org.springframework.security.acls.model.Permission;

public interface IActionColumnAddedElementState<T, I> extends IActionColumnNoParameterBuildState<T, I>, IActionColumnBuildState<T, I> {

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

	IActionColumnAddedElementState<T, I> hidePlaceholder();

	IActionColumnAddedElementState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

	IActionColumnAddedElementState<T, I> withClass(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>> valueModelFactories);

	IActionColumnAddedElementState<T, I> withClass(IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>> valueModelFactory);

	IActionColumnAddedElementState<T, I> withClass(IModel<? extends String> valueModel);

	IActionColumnAddedElementState<T, I> withClass(String firstValue, String... otherValues);

	IActionColumnAddedElementState<T, I> when(IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

	IActionColumnAddedElementState<T, I> when(Condition condition);

	IActionColumnAddedElementState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

	IActionColumnAddedElementState<T, I> whenPermission(String permission);

	IActionColumnAddedElementState<T, I> whenPermission(Permission permission);

	IActionColumnAddedElementState<T, I> add(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> behaviorFactories);

	IActionColumnAddedElementState<T, I> add(IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

	IActionColumnAddedElementState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);

}
