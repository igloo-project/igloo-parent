package test.wicket.more.link.descriptor.resource;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class TestLinkDescriptorNoParameterResource extends AbstractResource {

  private static final long serialVersionUID = 1L;

  public static final ResourceReference REFERENCE =
      new ResourceReference(
          TestLinkDescriptorNoParameterResource.class, "TestLinkDescriptorResource1") {
        private static final long serialVersionUID = 1L;

        @Override
        public IResource getResource() {
          return new TestLinkDescriptorNoParameterResource();
        }
      };

  public TestLinkDescriptorNoParameterResource() {
    super();
  }

  @Override
  protected ResourceResponse newResourceResponse(Attributes attributes) {
    return new ResourceResponse();
  }
}
