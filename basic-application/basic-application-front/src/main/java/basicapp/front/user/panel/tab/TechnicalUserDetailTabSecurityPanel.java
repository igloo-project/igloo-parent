package basicapp.front.user.panel.tab;

import basicapp.back.business.user.model.User;
import basicapp.front.user.panel.UserDetailSecurityHistoryLogsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class TechnicalUserDetailTabSecurityPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public TechnicalUserDetailTabSecurityPanel(String id, final IModel<User> userModel) {
    super(id, userModel);

    add(new UserDetailSecurityHistoryLogsPanel("historyLogs", userModel));
  }
}
