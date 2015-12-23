package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.wicket.more.markup.html.action.IAjaxOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.util.AjaxResponseAction;

public interface IAjaxConfirmLinkBuilderStepOnclick<O> {

	/**
	 * @deprecated Use {@link #onClick(IAjaxOneParameterAjaxAction)}
	 */
	@Deprecated
	IAjaxConfirmLinkBuilderStepTerminal<O> onClick(SerializableFunction<AjaxRequestTarget, Void> onClick);

	/**
	 * @deprecated Use {@link #onClick(IAjaxOneParameterAjaxAction)}
	 */
	@Deprecated
	IAjaxConfirmLinkBuilderStepTerminal<O> onClick(AjaxResponseAction onClick);

	IAjaxConfirmLinkBuilderStepTerminal<O> onClick(IAjaxOneParameterAjaxAction<IModel<O>> onClick);

}
