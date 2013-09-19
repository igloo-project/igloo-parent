package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;

public interface IBaseImageResourceState {
	
	IParameterMappingState<IImageResourceLinkDescriptor> imageResource(ResourceReference resourceReference);
	
	IParameterMappingState<IImageResourceLinkDescriptor> imageResource(IModel<? extends ResourceReference> resourceReferenceModel);

}
