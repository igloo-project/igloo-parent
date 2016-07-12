package fr.openwide.core.test.wicket.more.link.descriptor;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;
import fr.openwide.core.test.wicket.more.link.descriptor.resource.TestLinkDescriptorNoParameterResource;
import fr.openwide.core.test.wicket.more.link.descriptor.resource.TestLinkDescriptorOneParameterResource;
import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;

public class TestImageResourceLinkDescriptor extends AbstractAnyTargetTestLinkDescriptor {

	@Override
	protected ILinkDescriptor buildWithNullTarget(
			ILateTargetDefinitionTerminalState<
					IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
					> builder) {
		return builder.imageResource(Model.of((ResourceReference) null));
	}

	@Override
	protected ILinkDescriptor buildWithNoParameterTarget(
			ILateTargetDefinitionTerminalState<
					IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
					> builder) {
		return builder.imageResource(TestLinkDescriptorNoParameterResource.REFERENCE);
	}

	@Override
	protected ILinkDescriptor buildWithOneParameterTarget(
			ILateTargetDefinitionTerminalState<
					IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
					> builder) {
		return builder.imageResource(TestLinkDescriptorOneParameterResource.REFERENCE);
	}

	@Override
	protected String getNoParameterTargetPathPrefix() {
		return WicketMoreTestLinkDescriptorApplication.RESOURCE_NO_PARAMETER_PATH_PREFIX;
	}

	@Override
	protected String getOneParameterTargetPathPrefix() {
		return WicketMoreTestLinkDescriptorApplication.RESOURCE_ONE_PARAMETER_PATH_PREFIX;
	}
	
}
