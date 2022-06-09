package igloo.bootstrap.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;

public interface IConfirmLinkBuilderStepTerminal<L extends AbstractLink, O> extends IComponentFactory<L>, IOneParameterComponentFactory<L, IModel<O>> {

	@Override
	default void detach() {
		IComponentFactory.super.detach();
		IOneParameterComponentFactory.super.detach();
	}

}
