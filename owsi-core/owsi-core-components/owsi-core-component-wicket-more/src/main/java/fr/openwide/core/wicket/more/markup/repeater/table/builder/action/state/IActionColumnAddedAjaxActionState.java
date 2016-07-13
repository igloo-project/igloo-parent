package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;

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
	IActionColumnAddedAjaxActionState<T, I> when(Condition condition);

	@Override
	IActionColumnAddedAjaxActionState<T, I> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedAjaxActionState<T, I> whenPermission(String permission);

	@Override
	IActionColumnAddedAjaxActionState<T, I> whenPermission(Permission permission);

	@Override
	IActionColumnAddedAjaxActionState<T, I> withClass(String cssClass);

}
