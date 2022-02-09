package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.factory.IComponentFactory;
import org.iglooproject.wicket.api.factory.IOneParameterComponentFactory;

public interface IConfirmLinkBuilderStepTerminal<L extends AbstractLink, O> extends IComponentFactory<L>, IOneParameterComponentFactory<L, IModel<O>> {

	@Override
	default void detach() {
		IComponentFactory.super.detach();
		IOneParameterComponentFactory.super.detach();
	}

}
