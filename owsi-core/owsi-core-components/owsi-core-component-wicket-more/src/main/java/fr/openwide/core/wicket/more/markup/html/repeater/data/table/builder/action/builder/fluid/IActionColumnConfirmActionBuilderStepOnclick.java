package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.builder.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.action.IAjaxOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnAddedConfirmActionState;

public interface IActionColumnConfirmActionBuilderStepOnclick<T, S extends ISort<?>> {

	IActionColumnAddedConfirmActionState<T, S> onClick(IAjaxOneParameterAjaxAction<IModel<T>> onClick);

}
