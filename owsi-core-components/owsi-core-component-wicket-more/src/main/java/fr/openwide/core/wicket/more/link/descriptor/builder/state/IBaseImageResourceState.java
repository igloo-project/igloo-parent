package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;

public interface IBaseImageResourceState {
	
	IParameterMappingState<? extends IImageResourceLinkDescriptor> imageResource(ResourceReference resourceReference);
	
	IParameterMappingState<? extends IImageResourceLinkDescriptor> imageResource(IModel<? extends ResourceReference> resourceReferenceModel);

}
