package fr.openwide.core.wicket.more.link.descriptor.builder.state.pageinstance;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IPageInstanceTargetTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public interface IPageInstanceState<TLinkDescriptor extends IPageLinkGenerator>
		extends IPageInstanceTargetTerminalState<TLinkDescriptor> {
	
	<P extends Page> IPageInstanceState<TLinkDescriptor> validate(Class<P> expectedPageClass);
	
	IPageInstanceState<TLinkDescriptor> validate(IModel<? extends Class<? extends Page>> expectedPageClassModel);

}
