package org.iglooproject.wicket.more.link.descriptor.builder.state.base;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;

/**
 * @deprecated Use the late target definition syntax instead. See {@link LinkDescriptorBuilder#LinkDescriptorBuilder()}.
 */
@Deprecated
public interface IBasePageState {
	
	<P extends Page> INoMappableParameterMainState<
			IPageLinkDescriptor,
			Void, Void, Void
			> page(Class<P> pageClass);
	
	INoMappableParameterMainState<
			IPageLinkDescriptor,
			Void, Void, Void
			> page(IModel<? extends Class<? extends Page>> pageClassModel);

}
