package basicapp.front.user.panel.tab;

import basicapp.back.business.user.model.BasicUser;
import basicapp.front.user.panel.BasicUserDetailGeneralDescriptionPanel;
import basicapp.front.user.panel.BasicUserDetailGeneralGroupsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class BasicUserDetailTabGeneralPanel extends GenericPanel<BasicUser> {

  private static final long serialVersionUID = 1L;

  public BasicUserDetailTabGeneralPanel(String id, IModel<BasicUser> userModel) {
    super(id, userModel);

    add(
        new BasicUserDetailGeneralDescriptionPanel("description", userModel),
        new BasicUserDetailGeneralGroupsPanel("groups", userModel));
  }
}
