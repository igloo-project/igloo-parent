package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.fluid;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.action.IAction;
import org.iglooproject.wicket.more.markup.html.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.action.IOneParameterAction;
import org.iglooproject.wicket.more.markup.html.action.IOneParameterAjaxAction;

public interface IConfirmLinkBuilderStepOnclick<L extends AbstractLink, O> {

	IConfirmLinkBuilderStepOneParameterTerminal<L, O> onClick(IOneParameterAjaxAction<? super IModel<O>> onClick);

	IConfirmLinkBuilderStepOneParameterTerminal<L, O> onClick(IOneParameterAction<? super IModel<O>> onClick);

	IConfirmLinkBuilderStepTerminal<L, O> onClick(IAjaxAction onClick);

	IConfirmLinkBuilderStepTerminal<L, O> onClick(IAction onClick);

}
