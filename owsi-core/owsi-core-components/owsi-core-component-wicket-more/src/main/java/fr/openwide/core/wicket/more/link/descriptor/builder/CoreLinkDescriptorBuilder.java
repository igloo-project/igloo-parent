package fr.openwide.core.wicket.more.link.descriptor.builder;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CorePageLinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreResourceLinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IPageParametersState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IResourceParametersState;

public class CoreLinkDescriptorBuilder implements IBaseState {
	
	public CoreLinkDescriptorBuilder() { }

	@Override
	public <P extends Page> IPageParametersState page(Class<P> pageClass) {
		return new CorePageLinkDescriptorBuilder().page(pageClass);
	}

	@Override
	public IPageParametersState page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return new CorePageLinkDescriptorBuilder().page(pageClassModel);
	}

	@Override
	public IResourceParametersState resource(ResourceReference resourceReference) {
		return new CoreResourceLinkDescriptorBuilder().resource(resourceReference);
	}

	@Override
	public IResourceParametersState resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new CoreResourceLinkDescriptorBuilder().resource(resourceReferenceModel);
	}

}
