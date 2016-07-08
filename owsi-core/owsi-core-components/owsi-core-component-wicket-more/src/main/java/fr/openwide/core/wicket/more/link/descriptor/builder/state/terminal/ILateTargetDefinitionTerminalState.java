package fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

public interface ILateTargetDefinitionTerminalState<TPageResult, TResourceResult, TImageResourceResult> {
	
	TPageResult page(Class<? extends Page> pageClass);
	
	TResourceResult resource(ResourceReference resourceReference);

	TImageResourceResult imageResource(ResourceReference resourceReference);
	
	TPageResult page(IModel<? extends Class<? extends Page>> pageClassModel);
	
	TResourceResult resource(IModel<? extends ResourceReference> resourceReferenceModel);

	TImageResourceResult imageResource(IModel<? extends ResourceReference> resourceReferenceModel);

}
