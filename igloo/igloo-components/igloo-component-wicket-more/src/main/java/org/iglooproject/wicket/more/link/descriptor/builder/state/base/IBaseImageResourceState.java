package org.iglooproject.wicket.more.link.descriptor.builder.state.base;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;

/**
 * @deprecated Use the late target definition syntax instead. See {@link LinkDescriptorBuilder#LinkDescriptorBuilder()}.
 */
@Deprecated
public interface IBaseImageResourceState {
	
	INoMappableParameterMainState<
			IImageResourceLinkDescriptor,
			Void, Void, Void
			> imageResource(ResourceReference resourceReference);
	
	INoMappableParameterMainState<
			IImageResourceLinkDescriptor,
			Void, Void, Void
			> imageResource(IModel<? extends ResourceReference> resourceReferenceModel);

}
