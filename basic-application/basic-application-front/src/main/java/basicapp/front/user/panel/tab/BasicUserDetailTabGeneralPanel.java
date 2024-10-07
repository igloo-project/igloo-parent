package basicapp.front.user.panel.tab;

import basicapp.back.business.user.model.User;
import basicapp.front.user.panel.BasicUserDetailGeneralDescriptionPanel;
import basicapp.front.user.panel.UserDetailRolesPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class BasicUserDetailTabGeneralPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public BasicUserDetailTabGeneralPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    add(
        new BasicUserDetailGeneralDescriptionPanel("description", userModel),
        new UserDetailRolesPanel("roles", userModel));
  }
}
