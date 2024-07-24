package test.wicket.more.link.descriptor;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;
import test.wicket.more.link.descriptor.resource.TestLinkDescriptorNoParameterResource;
import test.wicket.more.link.descriptor.resource.TestLinkDescriptorOneParameterResource;

public class TestResourceLinkDescriptor extends AbstractAnyTargetTestLinkDescriptor {

  @Override
  protected ILinkDescriptor buildWithNullTarget(
      ILateTargetDefinitionTerminalState<
              IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
          builder) {
    return builder.resource(Model.of((ResourceReference) null));
  }

  @Override
  protected ILinkDescriptor buildWithNoParameterTarget(
      ILateTargetDefinitionTerminalState<
              IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
          builder) {
    return builder.resource(TestLinkDescriptorNoParameterResource.REFERENCE);
  }

  @Override
  protected ILinkDescriptor buildWithOneParameterTarget(
      ILateTargetDefinitionTerminalState<
              IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
          builder) {
    return builder.resource(TestLinkDescriptorOneParameterResource.REFERENCE);
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
