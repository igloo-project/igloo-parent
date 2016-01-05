package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedAjaxActionState<T, S extends ISort<?>> extends IActionColumnAddedElementState<T, S> {

	@Override
	IActionColumnAddedAjaxActionState<T, S> showLabel();

	@Override
	IActionColumnAddedAjaxActionState<T, S> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> hideLabel();

	@Override
	IActionColumnAddedAjaxActionState<T, S> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> showTooltip();

	@Override
	IActionColumnAddedAjaxActionState<T, S> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> hideTooltip();

	@Override
	IActionColumnAddedAjaxActionState<T, S> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> showIcon();

	@Override
	IActionColumnAddedAjaxActionState<T, S> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> hideIcon();

	@Override
	IActionColumnAddedAjaxActionState<T, S> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> showPlaceholder();

	@Override
	IActionColumnAddedAjaxActionState<T, S> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> hidePlaceholder();

	@Override
	IActionColumnAddedAjaxActionState<T, S> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> when(Condition condition);

	@Override
	IActionColumnAddedAjaxActionState<T, S> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedAjaxActionState<T, S> whenPermission(String permission);

	@Override
	IActionColumnAddedAjaxActionState<T, S> whenPermission(Permission permission);

	@Override
	IActionColumnAddedAjaxActionState<T, S> withClass(String cssClass);

}
