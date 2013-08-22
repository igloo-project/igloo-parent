package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;

public interface IBaseResourceState {
	
	IParameterMappingState<IResourceLinkDescriptor> resource(ResourceReference resourceReference);
	
	IParameterMappingState<IResourceLinkDescriptor> resource(IModel<? extends ResourceReference> resourceReferenceModel);

}
