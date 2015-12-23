package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;

public interface IAjaxConfirmLinkBuilderStepTerminal<O> {

	/**
	 * @deprecated Use {@link #create(String, IModel)}
	 */
	@Deprecated
	AjaxConfirmLink<O> create();

	AjaxConfirmLink<O> create(String wicketId, IModel<O> model);

	IOneParameterComponentFactory<AjaxConfirmLink<O>, IModel<O>> factory();

}
