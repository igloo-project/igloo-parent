package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterModelFactory;

public interface IAjaxConfirmLinkBuilderStepStart<O> {

	IAjaxConfirmLinkBuilderStepContent<O> title(IModel<String> titleModel);

	IAjaxConfirmLinkBuilderStepContent<O> title(IOneParameterModelFactory<? super IModel<O>, String> titleModelFactory);

	IAjaxConfirmLinkBuilderStepOnclick<O> deleteConfirmation();

}
