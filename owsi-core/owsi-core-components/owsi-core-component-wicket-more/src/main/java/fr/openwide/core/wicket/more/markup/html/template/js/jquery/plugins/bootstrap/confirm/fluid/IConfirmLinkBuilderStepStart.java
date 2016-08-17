package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface IConfirmLinkBuilderStepStart<L extends AbstractLink, O> {

	IConfirmLinkBuilderStepContent<L, O> title(IModel<String> titleModel);

	IConfirmLinkBuilderStepContent<L, O> title(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory);

	IConfirmLinkBuilderStepOnclick<L, O> deleteConfirmation();

}
