package basicapp.front.user.component.tab;

import basicapp.back.business.user.model.User;
import basicapp.front.user.component.UserDetailSecurityHistoryLogsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class BasicUserDetailTabSecurityPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public BasicUserDetailTabSecurityPanel(String id, final IModel<User> userModel) {
    super(id, userModel);

    add(new UserDetailSecurityHistoryLogsPanel("historyLogs", userModel));
  }
}
