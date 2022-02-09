package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;

public interface IConfirmLinkBuilder<L extends AbstractLink, O> extends
	IConfirmLinkBuilderStepContent<L, O>,
	IConfirmLinkBuilderStepEndContent<L, O>,
	IConfirmLinkBuilderStepNo<L, O>,
	IConfirmLinkBuilderStepOnclick<L, O>,
	IConfirmLinkBuilderStepOneParameterTerminal<L, O>,
	IConfirmLinkBuilderStepStart<L, O>,
	IConfirmLinkBuilderStepTerminal<L, O> {

}
