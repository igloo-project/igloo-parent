package basicapp.front.administration.component.tab;

import org.apache.wicket.model.IModel;

import basicapp.back.business.user.model.BasicUser;
import basicapp.front.administration.component.BasicUserDetailDescriptionPanel;
import basicapp.front.administration.component.BasicUserDetailGroupsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;

public class AdministrationBasicUserDetailTabMainInformationPanel extends GenericPanel<BasicUser> {

	private static final long serialVersionUID = -3900528127687137340L;

	public AdministrationBasicUserDetailTabMainInformationPanel(String id, IModel<BasicUser> userModel) {
		super(id, userModel);
		
		add(
			new BasicUserDetailDescriptionPanel("description", userModel),
			new BasicUserDetailGroupsPanel("groups", userModel)
		);
	}

}
