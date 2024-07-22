package igloo.bootstrap.confirm;

import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

public interface IConfirmLinkBuilderStepOneParameterTerminal<L extends AbstractLink, O>
    extends IOneParameterComponentFactory<L, IModel<O>> {}
