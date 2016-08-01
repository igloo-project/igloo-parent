package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

public interface IAjaxConfirmLinkBuilderStepEndContent<O> {
	
	IAjaxConfirmLinkBuilderStepEndContent<O> keepMarkup();
	
	IAjaxConfirmLinkBuilderStepEndContent<O> cssClassNamesModel(IModel<String> cssClassNamesModel);
	
	IAjaxConfirmLinkBuilderStepNo<O> yes(IModel<String> yesLabelModel);
	
	IAjaxConfirmLinkBuilderStepNo<O> yes(IModel<String> yesLabelModel, IModel<String> yesIconModel);
	
	IAjaxConfirmLinkBuilderStepNo<O> yes(IModel<String> yesLabelModel, IModel<String> yesIconModel, IModel<String> yesButtonModel);
	
	IAjaxConfirmLinkBuilderStepOnclick<O> yesNo();
	
	IAjaxConfirmLinkBuilderStepOnclick<O> confirm();
	
	IAjaxConfirmLinkBuilderStepOnclick<O> validate();
	
	IAjaxConfirmLinkBuilderStepOnclick<O> save();
	
}
