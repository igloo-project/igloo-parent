package org.iglooproject.wicket.more.security.page;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;

public class LogoutPage extends CoreWebPage {

  private static final long serialVersionUID = -1336719504268894384L;

  public LogoutPage() {
    this(CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor());
  }

  protected LogoutPage(IPageLinkDescriptor signInPageLinkDescriptor) {
    if (AuthenticatedWebSession.exists()) {
      AuthenticatedWebSession.get().invalidate();
    }

    throw signInPageLinkDescriptor.newRestartResponseException();
  }
}
