package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;

public interface IBaseImageResourceState {
	
	INoParameterMapperState<IImageResourceLinkDescriptor> imageResource(ResourceReference resourceReference);
	
	INoParameterMapperState<IImageResourceLinkDescriptor> imageResource(IModel<? extends ResourceReference> resourceReferenceModel);

}
