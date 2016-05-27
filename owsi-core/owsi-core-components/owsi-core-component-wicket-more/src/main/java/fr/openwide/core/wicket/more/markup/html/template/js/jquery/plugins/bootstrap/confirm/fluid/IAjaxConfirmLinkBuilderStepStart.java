package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface IAjaxConfirmLinkBuilderStepStart<O> {

	IAjaxConfirmLinkBuilderStepContent<O> title(IModel<String> titleModel);

	IAjaxConfirmLinkBuilderStepContent<O> title(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory);

	IAjaxConfirmLinkBuilderStepOnclick<O> deleteConfirmation();

}
