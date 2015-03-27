package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;

public interface IBaseResourceState {
	
	INoParameterMapperState<IResourceLinkDescriptor> resource(ResourceReference resourceReference);
	
	INoParameterMapperState<IResourceLinkDescriptor> resource(IModel<? extends ResourceReference> resourceReferenceModel);

}
