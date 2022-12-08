package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import java.util.Collection;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.springframework.security.acls.model.Permission;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;

public interface IActionColumnAddedAjaxActionState<T, I> extends IActionColumnAddedElementState<T, I> {

	@Override
	IActionColumnAddedAjaxActionState<T, I> showLabel();

	@Override
	IActionColumnAddedAjaxActionState<T, I> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> hideLabel();

	@Override
	IActionColumnAddedAjaxActionState<T, I> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> showTooltip();

	@Override
	IActionColumnAddedAjaxActionState<T, I> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> hideTooltip();

	@Override
	IActionColumnAddedAjaxActionState<T, I> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> showIcon();

	@Override
	IActionColumnAddedAjaxActionState<T, I> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> hideIcon();

	@Override
	IActionColumnAddedAjaxActionState<T, I> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> showPlaceholder();

	@Override
	IActionColumnAddedAjaxActionState<T, I> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> hidePlaceholder();

	@Override
	IActionColumnAddedAjaxActionState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> withClass(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>> valueModelFactories);

	@Override
	IActionColumnAddedAjaxActionState<T, I> withClass(IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>> valueModelFactory);

	@Override
	IActionColumnAddedAjaxActionState<T, I> withClass(IModel<? extends String> valueModel);

	@Override
	IActionColumnAddedAjaxActionState<T, I> withClass(String firstValue, String... otherValues);

	@Override
	IActionColumnAddedAjaxActionState<T, I> when(IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

	@Override
	IActionColumnAddedAjaxActionState<T, I> when(Condition condition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

	@Override
	IActionColumnAddedAjaxActionState<T, I> whenPermission(String permission);

	@Override
	IActionColumnAddedAjaxActionState<T, I> whenPermission(Permission permission);

	@Override
	IActionColumnAddedAjaxActionState<T, I> add(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> behaviorFactories);

	@Override
	IActionColumnAddedAjaxActionState<T, I> add(IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory);

	@Override
	IActionColumnAddedAjaxActionState<T, I> add(Behavior firstBehavior, Behavior... otherBehaviors);

}
