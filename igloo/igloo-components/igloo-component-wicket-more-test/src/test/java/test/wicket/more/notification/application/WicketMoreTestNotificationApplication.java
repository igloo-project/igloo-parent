package test.wicket.more.notification.application;

import org.apache.wicket.Page;
import test.wicket.more.application.WicketMoreTestApplication;

public class WicketMoreTestNotificationApplication extends WicketMoreTestApplication {

  @Override
  protected void mountApplicationPages() {}

  @Override
  protected void mountApplicationResources() {}

  @Override
  public Class<? extends Page> getHomePage() {
    return Page.class;
  }
}
