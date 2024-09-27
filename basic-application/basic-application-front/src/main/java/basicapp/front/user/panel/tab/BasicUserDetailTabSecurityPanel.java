package basicapp.front.user.panel.tab;

import basicapp.back.business.user.model.BasicUser;
import basicapp.front.user.panel.UserDetailSecurityHistoryLogsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class BasicUserDetailTabSecurityPanel extends GenericPanel<BasicUser> {

  private static final long serialVersionUID = -3900528127687137340L;

  public BasicUserDetailTabSecurityPanel(String id, final IModel<? extends BasicUser> userModel) {
    super(id, userModel);

    add(new UserDetailSecurityHistoryLogsPanel("historyLogs", userModel));
  }
}
