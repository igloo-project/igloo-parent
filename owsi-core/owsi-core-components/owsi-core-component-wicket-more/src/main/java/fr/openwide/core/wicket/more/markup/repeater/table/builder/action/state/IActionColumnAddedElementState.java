package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.behavior.Behavior;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;

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

	IActionColumnAddedElementState<T, I> when(Condition condition);

	IActionColumnAddedElementState<T, I> when(Predicate<? super T> predicate);

	IActionColumnAddedElementState<T, I> whenPermission(String permission);

	IActionColumnAddedElementState<T, I> whenPermission(Permission permission);

	IActionColumnAddedElementState<T, I> withClass(String cssClass);
	
	IActionColumnAddedElementState<T, I> add(Behavior...behaviors);

}
