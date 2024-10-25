package basicapp.front.user.component.tab;

import basicapp.back.business.user.model.User;
import basicapp.front.user.component.BasicUserDetailGeneralDescriptionPanel;
import basicapp.front.user.component.UserDetailRolesPanel;
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
