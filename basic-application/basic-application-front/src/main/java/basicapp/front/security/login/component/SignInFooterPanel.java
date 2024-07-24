package basicapp.front.security.login.component;

import basicapp.back.business.user.model.User;
import basicapp.back.security.service.ISecurityManagementService;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestCreationPage;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SignInFooterPanel extends Panel {

  private static final long serialVersionUID = -7042210777928535702L;

  @SpringBean private ISecurityManagementService securityManagementService;

  public SignInFooterPanel(String wicketId) {
    super(wicketId);

    add(Condition.anyChildVisible(this).thenShow());

    add(
        SecurityPasswordRecoveryRequestCreationPage.linkDescriptor()
            .link("passwordRecoveryRequestCreation")
            .add(
                Condition.isTrue(
                        () ->
                            securityManagementService
                                .getSecurityOptions(User.class)
                                .isPasswordUserRecoveryEnabled())
                    .thenShow()),
        SecurityPasswordRecoveryRequestResetPage.linkDescriptor()
            .link("passwordRecoveryRequestReset")
            .add(
                Condition.isTrue(
                        () ->
                            securityManagementService
                                .getSecurityOptions(User.class)
                                .isPasswordUserRecoveryEnabled())
                    .thenShow()));
  }
}
