package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;


public interface IPageInstanceState<TLinkDescriptor extends IPageLinkGenerator> extends ITerminalState<TLinkDescriptor> {
	
	<P extends Page> IPageInstanceState<TLinkDescriptor> validate(Class<P> expectedPageClass);
	
	IPageInstanceState<TLinkDescriptor> validate(IModel<? extends Class<? extends Page>> expectedPageClassModel);

}
