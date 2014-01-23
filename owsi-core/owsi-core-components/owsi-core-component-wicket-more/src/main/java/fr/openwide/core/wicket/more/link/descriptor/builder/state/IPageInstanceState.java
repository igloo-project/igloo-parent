package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;


public interface IPageInstanceState<L extends IPageLinkGenerator> extends ITerminalState<L> {
	
	<P extends Page> IPageInstanceState<L> validate(Class<P> expectedPageClass);
	
	IPageInstanceState<L> validate(IModel<? extends Class<? extends Page>> expectedPageClassModel);

}
