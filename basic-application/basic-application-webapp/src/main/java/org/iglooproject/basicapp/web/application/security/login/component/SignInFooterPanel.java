package org.iglooproject.basicapp.web.application.security.login.component;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;

public class SignInFooterPanel<U extends User> extends Panel {

	private static final long serialVersionUID = -7042210777928535702L;
	
	@SpringBean
	private ISecurityManagementService securityManagementService;
	
	public SignInFooterPanel(String wicketId, UserTypeDescriptor<U> typeDescriptor) {
		super(wicketId);
		
		boolean passwordRecoveryEnabled = securityManagementService.getOptions(typeDescriptor.getEntityClass())
				.isPasswordUserRecoveryEnabled();
		
		add(
				typeDescriptor.securityTypeDescriptor().passwordRecoveryPageLinkDescriptor().link("passwordRecovery")
						.setVisibilityAllowed(passwordRecoveryEnabled)
		);
	}

}
