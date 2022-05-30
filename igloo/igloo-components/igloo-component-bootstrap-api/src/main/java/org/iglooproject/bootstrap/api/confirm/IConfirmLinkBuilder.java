package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.factory.IOneParameterComponentFactory;

public interface IConfirmLinkBuilder<L extends AbstractLink, O> extends
	IConfirmLinkBuilderStepContent<L, O>,
	IConfirmLinkBuilderStepEndContent<L, O>,
	IConfirmLinkBuilderStepNo<L, O>,
	IConfirmLinkBuilderStepOnclick<L, O>,
	IConfirmLinkBuilderStepOneParameterTerminal<L, O>,
	IConfirmLinkBuilderStepStart<L, O>,
	IConfirmLinkBuilderStepTerminal<L, O>,
	IOneParameterComponentFactory<L, IModel<O>> {

}
