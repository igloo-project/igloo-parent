package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;
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

	IActionColumnAddedElementState<T, I> when(IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory);

	IActionColumnAddedElementState<T, I> when(Condition condition);

	IActionColumnAddedElementState<T, I> whenPredicate(SerializablePredicate2<? super T> predicate);

	IActionColumnAddedElementState<T, I> whenPermission(String permission);

	IActionColumnAddedElementState<T, I> whenPermission(Permission permission);

	IActionColumnAddedElementState<T, I> withClass(String cssClass);
	
	IActionColumnAddedElementState<T, I> add(Behavior...behaviors);

}
