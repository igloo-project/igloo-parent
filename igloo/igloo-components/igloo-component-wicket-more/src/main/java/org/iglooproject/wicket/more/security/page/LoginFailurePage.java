package org.iglooproject.wicket.more.security.page;

import org.apache.wicket.Session;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;

public class LoginFailurePage extends CoreWebPage {

  private static final long serialVersionUID = 7256074713578695000L;

  public LoginFailurePage() {
    this(CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor());
  }

  protected LoginFailurePage(IPageLinkDescriptor signInPageLinkDescriptor) {
    Session.get().error(getLocalizer().getString("login.failed", this));

    throw signInPageLinkDescriptor.newRestartResponseException();
  }
}
