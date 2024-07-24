package test.wicket.more.application;

import org.apache.wicket.Page;
import org.iglooproject.wicket.more.application.CoreWicketApplication;

/** Stub. */
public class WicketMoreTestApplication extends CoreWicketApplication {

  @Override
  protected void mountApplicationPages() {
    // Nothing to do
  }

  @Override
  protected void mountApplicationResources() {
    // Nothing to do
  }

  @Override
  public Class<? extends Page> getHomePage() {
    return Page.class;
  }
}
