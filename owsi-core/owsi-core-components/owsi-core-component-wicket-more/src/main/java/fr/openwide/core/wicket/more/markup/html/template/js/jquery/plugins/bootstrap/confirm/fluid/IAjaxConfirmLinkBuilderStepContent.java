package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

public interface IAjaxConfirmLinkBuilderStepContent<O> {
	
	IAjaxConfirmLinkBuilderStepEndContent<O> content(IModel<String> contentModel);

}
