package org.iglooproject.basicapp.web.application.security.login.component;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.security.password.page.SecurityPasswordRecoveryRequestCreationPage;
import org.iglooproject.basicapp.web.application.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import org.iglooproject.wicket.api.condition.Condition;

public class SignInFooterPanel extends Panel {

	private static final long serialVersionUID = -7042210777928535702L;
	
	@SpringBean
	private ISecurityManagementService securityManagementService;
	
	public SignInFooterPanel(String wicketId) {
		super(wicketId);
		
		add(
			Condition.anyChildVisible(this)
				.thenShow()
		);
		
		add(
			SecurityPasswordRecoveryRequestCreationPage.linkDescriptor()
				.link("passwordRecoveryRequestCreation")
				.add(
					Condition.isTrue(() -> securityManagementService.getSecurityOptions(User.class).isPasswordUserRecoveryEnabled())
						.thenShow()
				),
			SecurityPasswordRecoveryRequestResetPage.linkDescriptor()
				.link("passwordRecoveryRequestReset")
				.add(
					Condition.isTrue(() -> securityManagementService.getSecurityOptions(User.class).isPasswordUserRecoveryEnabled())
						.thenShow()
				)
		);
	}

}
