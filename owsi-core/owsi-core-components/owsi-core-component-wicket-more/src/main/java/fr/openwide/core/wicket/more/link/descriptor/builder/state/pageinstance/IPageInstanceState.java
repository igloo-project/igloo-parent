package fr.openwide.core.wicket.more.link.descriptor.builder.state.pageinstance;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IEarlyTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

@SuppressWarnings("deprecation")
public interface IPageInstanceState<TLinkDescriptor extends IPageLinkGenerator>
		extends IEarlyTargetDefinitionTerminalState<TLinkDescriptor> {
	
	<P extends Page> IPageInstanceState<TLinkDescriptor> validate(Class<P> expectedPageClass);
	
	IPageInstanceState<TLinkDescriptor> validate(IModel<? extends Class<? extends Page>> expectedPageClassModel);

}
