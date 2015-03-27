package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;

public interface IBasePageState {
	
	<P extends Page> INoParameterMapperState<IPageLinkDescriptor> page(Class<P> pageClass);
	
	INoParameterMapperState<IPageLinkDescriptor> page(IModel<? extends Class<? extends Page>> pageClassModel);

}
