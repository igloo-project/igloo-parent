package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedActionState<T, S extends ISort<?>> extends IActionColumnAddedElementState<T, S> {

	@Override
	IActionColumnAddedActionState<T, S> showLabel();

	@Override
	IActionColumnAddedActionState<T, S> showLabel(Condition showLabelCondition);

	@Override
	IActionColumnAddedActionState<T, S> hideLabel();

	@Override
	IActionColumnAddedActionState<T, S> hideLabel(Condition hideLabelCondition);

	@Override
	IActionColumnAddedActionState<T, S> showTooltip();

	@Override
	IActionColumnAddedActionState<T, S> showTooltip(Condition showTooltipCondition);

	@Override
	IActionColumnAddedActionState<T, S> hideTooltip();

	@Override
	IActionColumnAddedActionState<T, S> hideTooltip(Condition hideTooltipCondition);

	@Override
	IActionColumnAddedActionState<T, S> showIcon();

	@Override
	IActionColumnAddedActionState<T, S> showIcon(Condition showIconCondition);

	@Override
	IActionColumnAddedActionState<T, S> hideIcon();

	@Override
	IActionColumnAddedActionState<T, S> hideIcon(Condition hideIconCondition);

	@Override
	IActionColumnAddedActionState<T, S> showPlaceholder();

	@Override
	IActionColumnAddedActionState<T, S> showPlaceholder(Condition showPlaceholderCondition);

	@Override
	IActionColumnAddedActionState<T, S> hidePlaceholder();

	@Override
	IActionColumnAddedActionState<T, S> hidePlaceholder(Condition hidePlaceholderCondition);

	@Override
	IActionColumnAddedActionState<T, S> when(Condition condition);

	@Override
	IActionColumnAddedActionState<T, S> when(Predicate<? super T> predicate);

	@Override
	IActionColumnAddedActionState<T, S> whenPermission(String permission);

	@Override
	IActionColumnAddedActionState<T, S> whenPermission(Permission permission);

	@Override
	IActionColumnAddedActionState<T, S> withClass(String cssClass);

}
