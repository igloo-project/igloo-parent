package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import java.util.Collection;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;

public interface IActionColumnAddedLinkState<T, I> extends IActionColumnAddedElementState<T, I> {

	@Override
	IActionColumnAddedLinkState<T, I> showLabel();

	@Override
	IActionColumnAddedLinkState<T, I> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedLinkState<T, I> hideLabel();

	@Override
	IActionColumnAddedLinkState<T, I> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedLinkState<T, I> showTooltip();

	@Override
	IActionColumnAddedLinkState<T, I> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedLinkState<T, I> hideTooltip();

	@Override
	IActionColumnAddedLinkState<T, I> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedLinkState<T, I> showIcon();

	@Override
	IActionColumnAddedLinkState<T, I> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedLinkState<T, I> hideIcon();

	@Override
	IActionColumnAddedLinkState<T, I> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedLinkState<T, I> showPlaceholder();

	@Override
	IActionColumnAddedLinkState<T, I> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedLinkState<T, I> hidePlaceholder();

	@Override
	IActionColumnAddedLinkState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedLinkState<T, I> withClass(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>> valueModelFactories);

	@Override
	IActionColumnAddedLinkState<T, I> withClass(IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>> valueModelFactory);

	@Override
	IActionColumnAddedLinkState<T, I> withClass(IModel<? extends String> valueModel);

	@Override
	IActionColumnAddedLinkState<T, I> withClass(String firstValue, String... otherValues);

	@Override
	IActionColumnAddedLinkState<T, I> when(IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

	@Override
	IActionColumnAddedLinkState<T, I> when(Condition condition);

	@Override
	IActionColumnAddedLinkState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

	@Override
	IActionColumnAddedLinkState<T, I> whenPermission(String permission);

	@Override
	IActionColumnAddedLinkState<T, I> whenPermission(Permission permission);

	@Override
	IActionColumnAddedLinkState<T, I> add(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> behaviorFactories);

	@Override
	IActionColumnAddedLinkState<T, I> add(IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

	@Override
	IActionColumnAddedLinkState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);

	IActionColumnAddedLinkState<T, I> hideIfInvalid();

}
