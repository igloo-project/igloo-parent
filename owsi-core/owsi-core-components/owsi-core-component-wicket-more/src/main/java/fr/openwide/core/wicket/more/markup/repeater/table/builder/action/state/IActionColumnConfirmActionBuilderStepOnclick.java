package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;

public interface IActionColumnConfirmActionBuilderStepOnclick<T, S extends ISort<?>> {

	IActionColumnAddedConfirmActionState<T, S> onClick(IOneParameterAjaxAction<IModel<T>> onClick);

}
