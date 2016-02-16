package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.action.IAjaxAction;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;

public interface IAjaxConfirmLinkBuilderStepOnclick<O> {

	IAjaxConfirmLinkBuilderStepOneParameterTerminal<O> onClick(IOneParameterAjaxAction<? super IModel<O>> onClick);

	IAjaxConfirmLinkBuilderStepTerminal<O> onClick(IAjaxAction onClick);

}
