package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedConfirmActionState<T, I> extends IActionColumnAddedElementState<T, I> {

	@Override
	IActionColumnAddedConfirmActionState<T, I> showLabel();

	@Override
	IActionColumnAddedConfirmActionState<T, I> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> hideLabel();

	@Override
	IActionColumnAddedConfirmActionState<T, I> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> showTooltip();

	@Override
	IActionColumnAddedConfirmActionState<T, I> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> hideTooltip();

	@Override
	IActionColumnAddedConfirmActionState<T, I> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> showIcon();

	@Override
	IActionColumnAddedConfirmActionState<T, I> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> hideIcon();

	@Override
	IActionColumnAddedConfirmActionState<T, I> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> showPlaceholder();

	@Override
	IActionColumnAddedConfirmActionState<T, I> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> hidePlaceholder();

	@Override
	IActionColumnAddedConfirmActionState<T, I> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> when(Condition condition);

	@Override
	IActionColumnAddedConfirmActionState<T, I> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedConfirmActionState<T, I> whenPermission(String permission);

	@Override
	IActionColumnAddedConfirmActionState<T, I> whenPermission(Permission permission);

	@Override
	IActionColumnAddedConfirmActionState<T, I> withClass(String cssClass);

}
