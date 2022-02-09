package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.factory.IOneParameterComponentFactory;

public interface IConfirmLinkBuilderStepOneParameterTerminal<L extends AbstractLink, O> extends IOneParameterComponentFactory<L, IModel<O>> {

}
