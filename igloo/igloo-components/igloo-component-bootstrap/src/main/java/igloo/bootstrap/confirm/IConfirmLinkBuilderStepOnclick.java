package igloo.bootstrap.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import igloo.wicket.action.IAction;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.action.IOneParameterAction;
import igloo.wicket.action.IOneParameterAjaxAction;

public interface IConfirmLinkBuilderStepOnclick<L extends AbstractLink, O> {

	IConfirmLinkBuilderStepOneParameterTerminal<L, O> onClick(IOneParameterAjaxAction<? super IModel<O>> onClick);

	IConfirmLinkBuilderStepOneParameterTerminal<L, O> onClick(IOneParameterAction<? super IModel<O>> onClick);

	IConfirmLinkBuilderStepTerminal<L, O> onClick(IAjaxAction onClick);

	IConfirmLinkBuilderStepTerminal<L, O> onClick(IAction onClick);

}
