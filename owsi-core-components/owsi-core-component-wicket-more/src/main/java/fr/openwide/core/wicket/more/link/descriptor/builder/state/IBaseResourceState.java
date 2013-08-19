package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

public interface IBaseResourceState {
	
	IResourceParametersState resource(ResourceReference resourceReference);
	
	IResourceParametersState resource(IModel<? extends ResourceReference> resourceReferenceModel);

}
