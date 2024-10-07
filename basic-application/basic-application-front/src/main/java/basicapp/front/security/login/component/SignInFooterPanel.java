package basicapp.front.security.login.component;

import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestCreationPage;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SignInFooterPanel extends Panel {

  private static final long serialVersionUID = -7042210777928535702L;

  @SpringBean private ISecurityManagementControllerService securityManagementController;

  public SignInFooterPanel(String wicketId) {
    super(wicketId);

    add(Condition.anyChildVisible(this).thenShow());

    add(
        SecurityPasswordRecoveryRequestCreationPage.linkDescriptor()
            .link("passwordRecoveryRequestCreation")
            .add(
                Condition.isTrue(
                        () ->
                            securityManagementController
                                .getSecurityOptions(UserType.BASIC)
                                .isPasswordUserRecoveryEnabled())
                    .thenShow()),
        SecurityPasswordRecoveryRequestResetPage.linkDescriptor()
            .link("passwordRecoveryRequestReset")
            .add(
                Condition.isTrue(
                        () ->
                            securityManagementController
                                .getSecurityOptions(UserType.BASIC)
                                .isPasswordUserRecoveryEnabled())
                    .thenShow()));
  }
}
