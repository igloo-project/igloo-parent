package fr.openwide.core.wicket.more.link.descriptor.builder.state.base;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;

/**
 * @deprecated Use the late target definition syntax instead. See {@link LinkDescriptorBuilder#LinkDescriptorBuilder()}.
 */
@Deprecated
public interface IBaseResourceState {
	
	INoMappableParameterMainState<
			IResourceLinkDescriptor,
			Void, Void, Void
			> resource(ResourceReference resourceReference);
	
	INoMappableParameterMainState<
			IResourceLinkDescriptor,
			Void, Void, Void
			> resource(IModel<? extends ResourceReference> resourceReferenceModel);

}
