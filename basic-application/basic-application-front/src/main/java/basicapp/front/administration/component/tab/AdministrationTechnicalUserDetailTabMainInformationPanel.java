package basicapp.front.administration.component.tab;

import org.apache.wicket.model.IModel;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.front.administration.component.TechnicalUserDetailDescriptionPanel;
import basicapp.front.administration.component.TechnicalUserDetailGroupsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;

public class AdministrationTechnicalUserDetailTabMainInformationPanel extends GenericPanel<TechnicalUser> {

	private static final long serialVersionUID = -6822072984820432812L;

	public AdministrationTechnicalUserDetailTabMainInformationPanel(String id, IModel<TechnicalUser> userModel) {
		super(id, userModel);
		
		add(
			new TechnicalUserDetailDescriptionPanel("description", userModel),
			new TechnicalUserDetailGroupsPanel("groups", userModel)
		);
	}

}
