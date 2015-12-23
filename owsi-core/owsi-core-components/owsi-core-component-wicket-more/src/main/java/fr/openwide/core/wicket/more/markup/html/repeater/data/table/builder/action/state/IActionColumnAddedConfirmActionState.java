package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedConfirmActionState<T, S extends ISort<?>> extends IActionColumnAddedElementState<T, S> {

	@Override
	IActionColumnAddedConfirmActionState<T, S> showLabel();

	@Override
	IActionColumnAddedConfirmActionState<T, S> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> hideLabel();

	@Override
	IActionColumnAddedConfirmActionState<T, S> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> showTooltip();

	@Override
	IActionColumnAddedConfirmActionState<T, S> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> hideTooltip();

	@Override
	IActionColumnAddedConfirmActionState<T, S> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> showIcon();

	@Override
	IActionColumnAddedConfirmActionState<T, S> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> hideIcon();

	@Override
	IActionColumnAddedConfirmActionState<T, S> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> showPlaceholder();

	@Override
	IActionColumnAddedConfirmActionState<T, S> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> hidePlaceholder();

	@Override
	IActionColumnAddedConfirmActionState<T, S> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> when(Condition condition);

	@Override
	IActionColumnAddedConfirmActionState<T, S> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedConfirmActionState<T, S> whenPermission(String permission);

	@Override
	IActionColumnAddedConfirmActionState<T, S> whenPermission(Permission permission);

	@Override
	IActionColumnAddedConfirmActionState<T, S> withClass(String cssClass);

}
