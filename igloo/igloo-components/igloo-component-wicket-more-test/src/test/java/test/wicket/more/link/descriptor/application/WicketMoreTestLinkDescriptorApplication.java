package test.wicket.more.link.descriptor.application;

import org.apache.wicket.Page;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import test.wicket.more.application.WicketMoreTestApplication;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorNoParameterPage;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorOneParameterPage;
import test.wicket.more.link.descriptor.resource.TestLinkDescriptorNoParameterResource;
import test.wicket.more.link.descriptor.resource.TestLinkDescriptorOneParameterResource;

public class WicketMoreTestLinkDescriptorApplication extends WicketMoreTestApplication {

  public static final String PAGE_NO_PARAMETER_PATH_PREFIX = "/test/page/0/";
  public static final String PAGE_ONE_PARAMETER_PATH_PREFIX = "/test/page/1/";
  public static final String RESOURCE_NO_PARAMETER_PATH_PREFIX = "/test/resource/0/";
  public static final String RESOURCE_ONE_PARAMETER_PATH_PREFIX = "/test/resource/1/";

  @Override
  protected void mountApplicationPages() {
    mountParameterizedPage(PAGE_NO_PARAMETER_PATH_PREFIX, TestLinkDescriptorNoParameterPage.class);
    mountParameterizedPage(
        PAGE_ONE_PARAMETER_PATH_PREFIX + "${" + CommonParameters.ID + "}",
        TestLinkDescriptorOneParameterPage.class);
  }

  @Override
  protected void mountApplicationResources() {
    mountResource(
        RESOURCE_NO_PARAMETER_PATH_PREFIX, TestLinkDescriptorNoParameterResource.REFERENCE);
    mountResource(
        RESOURCE_ONE_PARAMETER_PATH_PREFIX + "${" + CommonParameters.ID + "}",
        TestLinkDescriptorOneParameterResource.REFERENCE);
  }

  @Override
  public Class<? extends Page> getHomePage() {
    return Page.class;
  }
}
