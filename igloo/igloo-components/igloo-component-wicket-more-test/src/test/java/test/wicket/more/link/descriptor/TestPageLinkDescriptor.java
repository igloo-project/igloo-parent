package test.wicket.more.link.descriptor;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorNoParameterPage;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorOneParameterPage;

public class TestPageLinkDescriptor extends AbstractAnyTargetTestLinkDescriptor {

  @Override
  protected ILinkDescriptor buildWithNullTarget(
      ILateTargetDefinitionTerminalState<
              IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
          builder) {
    return builder.page(Model.of((Class<Page>) null));
  }

  @Override
  protected ILinkDescriptor buildWithNoParameterTarget(
      ILateTargetDefinitionTerminalState<
              IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
          builder) {
    return builder.page(TestLinkDescriptorNoParameterPage.class);
  }

  @Override
  protected ILinkDescriptor buildWithOneParameterTarget(
      ILateTargetDefinitionTerminalState<
              IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
          builder) {
    return builder.page(TestLinkDescriptorOneParameterPage.class);
  }

  @Override
  protected String getNoParameterTargetPathPrefix() {
    return WicketMoreTestLinkDescriptorApplication.PAGE_NO_PARAMETER_PATH_PREFIX;
  }

  @Override
  protected String getOneParameterTargetPathPrefix() {
    return WicketMoreTestLinkDescriptorApplication.PAGE_ONE_PARAMETER_PATH_PREFIX;
  }
}
