package basicapp.front.user.panel.tab;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.front.user.panel.UserDetailSecurityHistoryLogsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class TechnicalUserDetailTabSecurityPanel extends GenericPanel<TechnicalUser> {

  private static final long serialVersionUID = -341225850371484771L;

  public TechnicalUserDetailTabSecurityPanel(
      String id, final IModel<? extends TechnicalUser> userModel) {
    super(id, userModel);

    add(new UserDetailSecurityHistoryLogsPanel("historyLogs", userModel));
  }
}
