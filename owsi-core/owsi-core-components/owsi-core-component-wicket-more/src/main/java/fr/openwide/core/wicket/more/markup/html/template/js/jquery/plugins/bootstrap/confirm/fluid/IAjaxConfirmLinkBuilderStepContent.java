package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface IAjaxConfirmLinkBuilderStepContent<O> {

	IAjaxConfirmLinkBuilderStepEndContent<O> content(IModel<String> contentModel);

	IAjaxConfirmLinkBuilderStepEndContent<O> content(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory);

}
