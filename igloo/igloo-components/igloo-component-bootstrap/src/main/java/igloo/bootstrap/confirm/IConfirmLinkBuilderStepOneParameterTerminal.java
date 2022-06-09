package igloo.bootstrap.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import igloo.wicket.factory.IOneParameterComponentFactory;

public interface IConfirmLinkBuilderStepOneParameterTerminal<L extends AbstractLink, O> extends IOneParameterComponentFactory<L, IModel<O>> {

}
