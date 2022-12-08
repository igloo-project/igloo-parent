package org.iglooproject.basicapp.web.application.administration.component.tab;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.web.application.administration.component.TechnicalUserDetailDescriptionPanel;
import org.iglooproject.basicapp.web.application.administration.component.TechnicalUserDetailGroupsPanel;

import igloo.wicket.markup.html.panel.GenericPanel;

public class AdministrationTechnicalUserDetailTabMainInformationPanel extends GenericPanel<TechnicalUser> {

	private static final long serialVersionUID = -6822072984820432812L;

	public AdministrationTechnicalUserDetailTabMainInformationPanel(String id, final IModel<? extends TechnicalUser> userModel) {
		super(id, userModel);
		
		add(
			new TechnicalUserDetailDescriptionPanel("description", userModel),
			new TechnicalUserDetailGroupsPanel("groups", userModel)
		);
	}

}
