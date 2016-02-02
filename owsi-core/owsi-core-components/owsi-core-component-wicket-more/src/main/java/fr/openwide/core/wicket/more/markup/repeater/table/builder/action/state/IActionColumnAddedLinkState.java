package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedLinkState<T, S extends ISort<?>> extends IActionColumnAddedElementState<T, S> {

	@Override
	IActionColumnAddedLinkState<T, S> showLabel();

	@Override
	IActionColumnAddedLinkState<T, S> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedLinkState<T, S> hideLabel();

	@Override
	IActionColumnAddedLinkState<T, S> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedLinkState<T, S> showTooltip();

	@Override
	IActionColumnAddedLinkState<T, S> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedLinkState<T, S> hideTooltip();

	@Override
	IActionColumnAddedLinkState<T, S> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedLinkState<T, S> showIcon();

	@Override
	IActionColumnAddedLinkState<T, S> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedLinkState<T, S> hideIcon();

	@Override
	IActionColumnAddedLinkState<T, S> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedLinkState<T, S> showPlaceholder();

	@Override
	IActionColumnAddedLinkState<T, S> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedLinkState<T, S> hidePlaceholder();

	@Override
	IActionColumnAddedLinkState<T, S> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedLinkState<T, S> when(Condition condition);

	@Override
	IActionColumnAddedLinkState<T, S> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedLinkState<T, S> whenPermission(String permission);

	@Override
	IActionColumnAddedLinkState<T, S> whenPermission(Permission permission);

	@Override
	IActionColumnAddedLinkState<T, S> withClass(String cssClass);

	IActionColumnAddedLinkState<T, S> hideIfInvalid();

}
