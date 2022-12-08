package org.iglooproject.basicapp.web.application.administration.component.tab;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.web.application.administration.component.UserDetailHistoryLogPanel;

import igloo.wicket.markup.html.panel.GenericPanel;

public class AdministrationTechnicalUserDetailTabSecurityPanel extends GenericPanel<TechnicalUser> {

	private static final long serialVersionUID = -341225850371484771L;

	public AdministrationTechnicalUserDetailTabSecurityPanel(String id, final IModel<? extends TechnicalUser> userModel) {
		super(id, userModel);
		
		add(
			new UserDetailHistoryLogPanel("audits", userModel)
		);
	}

}
