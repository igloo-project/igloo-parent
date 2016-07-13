package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.behavior.Behavior;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;

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
	IActionColumnAddedLinkState<T, I> when(Condition condition);

	@Override
	IActionColumnAddedLinkState<T, I> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedLinkState<T, I> whenPermission(String permission);

	@Override
	IActionColumnAddedLinkState<T, I> whenPermission(Permission permission);

	@Override
	IActionColumnAddedLinkState<T, I> withClass(String cssClass);
	
	@Override
	IActionColumnAddedLinkState<T, I> add(Behavior... behaviors);

	IActionColumnAddedLinkState<T, I> hideIfInvalid();

}
