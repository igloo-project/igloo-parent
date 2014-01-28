package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.openwide.core.commons.util.functional.SerializableFunction;

public interface IAjaxConfirmLinkBuilderStepOnclick<O> {
	
	IAjaxConfirmLinkBuilderStepTerminal<O> onClick(SerializableFunction<AjaxRequestTarget, Void> onClick);

}
