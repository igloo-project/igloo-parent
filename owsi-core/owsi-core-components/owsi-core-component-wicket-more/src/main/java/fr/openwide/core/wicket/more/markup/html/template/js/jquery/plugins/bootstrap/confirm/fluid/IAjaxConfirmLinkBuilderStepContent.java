package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterModelFactory;

public interface IAjaxConfirmLinkBuilderStepContent<O> {

	IAjaxConfirmLinkBuilderStepEndContent<O> content(IModel<String> contentModel);

	IAjaxConfirmLinkBuilderStepEndContent<O> content(IOneParameterModelFactory<IModel<O>, String> contentModelFactory);

}
