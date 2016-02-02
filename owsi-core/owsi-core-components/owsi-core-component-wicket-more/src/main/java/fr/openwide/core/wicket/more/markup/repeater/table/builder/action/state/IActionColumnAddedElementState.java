package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IActionColumnAddedElementState<T, S extends ISort<?>> extends IActionColumnBuildState<T, S> {

	IActionColumnAddedElementState<T, S> showLabel();

	IActionColumnAddedElementState<T, S> showLabel(Condition showLabelCondition);

	IActionColumnAddedElementState<T, S> hideLabel();

	IActionColumnAddedElementState<T, S> hideLabel(Condition hideLabelCondition);

	IActionColumnAddedElementState<T, S> showTooltip();

	IActionColumnAddedElementState<T, S> showTooltip(Condition showTooltipCondition);

	IActionColumnAddedElementState<T, S> hideTooltip();

	IActionColumnAddedElementState<T, S> hideTooltip(Condition hideTooltipCondition);

	IActionColumnAddedElementState<T, S> showIcon();

	IActionColumnAddedElementState<T, S> showIcon(Condition showIconCondition);

	IActionColumnAddedElementState<T, S> hideIcon();

	IActionColumnAddedElementState<T, S> hideIcon(Condition hideIconCondition);

	IActionColumnAddedElementState<T, S> showPlaceholder();

	IActionColumnAddedElementState<T, S> showPlaceholder(Condition showPlaceholderCondition);

	IActionColumnAddedElementState<T, S> hidePlaceholder();

	IActionColumnAddedElementState<T, S> hidePlaceholder(Condition hidePlaceholderCondition);

	IActionColumnAddedElementState<T, S> when(Condition condition);

	IActionColumnAddedElementState<T, S> when(Predicate<? super T> predicate);

	IActionColumnAddedElementState<T, S> whenPermission(String permission);

	IActionColumnAddedElementState<T, S> whenPermission(Permission permission);

	IActionColumnAddedElementState<T, S> withClass(String cssClass);

}
