package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.factory.IDetachableFactory;

public interface IConfirmLinkBuilderStepContent<L extends AbstractLink, O> {

	IConfirmLinkBuilderStepEndContent<L, O> content(IModel<String> contentModel);

	IConfirmLinkBuilderStepEndContent<L, O> content(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory);

}
