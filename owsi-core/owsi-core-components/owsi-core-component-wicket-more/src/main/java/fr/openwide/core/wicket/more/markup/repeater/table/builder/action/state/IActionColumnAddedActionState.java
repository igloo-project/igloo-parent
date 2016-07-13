package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedActionState<T, I> extends IActionColumnAddedElementState<T, I> {

	@Override
	IActionColumnAddedActionState<T, I> showLabel();

	@Override
	IActionColumnAddedActionState<T, I> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedActionState<T, I> hideLabel();

	@Override
	IActionColumnAddedActionState<T, I> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedActionState<T, I> showTooltip();

	@Override
	IActionColumnAddedActionState<T, I> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedActionState<T, I> hideTooltip();

	@Override
	IActionColumnAddedActionState<T, I> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedActionState<T, I> showIcon();

	@Override
	IActionColumnAddedActionState<T, I> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedActionState<T, I> hideIcon();

	@Override
	IActionColumnAddedActionState<T, I> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedActionState<T, I> showPlaceholder();

	@Override
	IActionColumnAddedActionState<T, I> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedActionState<T, I> hidePlaceholder();

	@Override
	IActionColumnAddedActionState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedActionState<T, I> when(Condition condition);

	@Override
	IActionColumnAddedActionState<T, I> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedActionState<T, I> whenPermission(String permission);

	@Override
	IActionColumnAddedActionState<T, I> whenPermission(Permission permission);

	@Override
	IActionColumnAddedActionState<T, I> withClass(String cssClass);

}
