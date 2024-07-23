package igloo.console.navigation.page;

import org.iglooproject.wicket.more.security.page.LoginFailurePage;

public class ConsoleLoginFailurePage extends LoginFailurePage {

  private static final long serialVersionUID = -9131590620848626896L;

  public ConsoleLoginFailurePage() {
    super(ConsoleSignInPage.linkDescriptor());
  }
}
