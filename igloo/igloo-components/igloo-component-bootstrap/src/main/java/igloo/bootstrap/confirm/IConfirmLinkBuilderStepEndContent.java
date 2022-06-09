package igloo.bootstrap.confirm;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

public interface IConfirmLinkBuilderStepEndContent<L extends AbstractLink, O> {
	
	IConfirmLinkBuilderStepEndContent<L, O> keepMarkup();
	
	IConfirmLinkBuilderStepEndContent<L, O> cssClassNamesModel(IModel<String> cssClassNamesModel);
	
	IConfirmLinkBuilderStepNo<L, O> yes(IModel<String> yesLabelModel);
	
	IConfirmLinkBuilderStepNo<L, O> yes(IModel<String> yesLabelModel, IModel<String> yesIconModel);
	
	IConfirmLinkBuilderStepNo<L, O> yes(IModel<String> yesLabelModel, IModel<String> yesIconModel, IModel<String> yesButtonModel);
	
	IConfirmLinkBuilderStepOnclick<L, O> yesNo();
	
	IConfirmLinkBuilderStepOnclick<L, O> confirm();
	
	IConfirmLinkBuilderStepOnclick<L, O> validate();
	
	IConfirmLinkBuilderStepOnclick<L, O> save();
	
	IConfirmLinkBuilderStepEndContent<L, O> submit(Form<?> form);
	
}
