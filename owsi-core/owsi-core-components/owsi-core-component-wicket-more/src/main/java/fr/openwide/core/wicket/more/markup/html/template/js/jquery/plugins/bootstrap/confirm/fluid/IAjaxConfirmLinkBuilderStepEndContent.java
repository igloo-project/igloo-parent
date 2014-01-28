package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

public interface IAjaxConfirmLinkBuilderStepEndContent<O> {
	
	IAjaxConfirmLinkBuilderStepEndContent<O> keepMarkup();
	
	IAjaxConfirmLinkBuilderStepNo<O> yes(IModel<String> yesLabelModel);
	
	IAjaxConfirmLinkBuilderStepOnclick<O> yesNo();
	
	IAjaxConfirmLinkBuilderStepOnclick<O> confirm();
	
	IAjaxConfirmLinkBuilderStepOnclick<O> validate();
	
	IAjaxConfirmLinkBuilderStepOnclick<O> save();
	
}
