package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.factory.IDetachableFactory;

public interface IConfirmLinkBuilderStepStart<L extends AbstractLink, O> {

	IConfirmLinkBuilderStepContent<L, O> title(IModel<String> titleModel);

	IConfirmLinkBuilderStepContent<L, O> title(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory);

	IConfirmLinkBuilderStepOnclick<L, O> deleteConfirmation();

}
