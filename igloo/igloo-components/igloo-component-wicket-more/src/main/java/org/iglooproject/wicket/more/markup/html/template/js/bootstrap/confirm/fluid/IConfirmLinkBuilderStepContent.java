package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.fluid;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;

public interface IConfirmLinkBuilderStepContent<L extends AbstractLink, O> {

	IConfirmLinkBuilderStepEndContent<L, O> content(IModel<String> contentModel);

	IConfirmLinkBuilderStepEndContent<L, O> content(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory);

}
