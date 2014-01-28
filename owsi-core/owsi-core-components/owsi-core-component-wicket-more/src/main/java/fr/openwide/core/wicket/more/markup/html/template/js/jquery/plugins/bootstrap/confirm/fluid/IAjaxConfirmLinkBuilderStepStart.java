package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

public interface IAjaxConfirmLinkBuilderStepStart<O> {
	
	IAjaxConfirmLinkBuilderStepContent<O> title(IModel<String> titleModel);
	
	IAjaxConfirmLinkBuilderStepOnclick<O> deleteConfirmation();

}
