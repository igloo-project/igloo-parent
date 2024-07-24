package basicapp.front.administration.component.tab;

import basicapp.back.business.user.model.BasicUser;
import basicapp.front.administration.component.UserDetailHistoryLogPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class AdministrationBasicUserDetailTabSecurityPanel extends GenericPanel<BasicUser> {

  private static final long serialVersionUID = -3900528127687137340L;

  public AdministrationBasicUserDetailTabSecurityPanel(
      String id, final IModel<? extends BasicUser> userModel) {
    super(id, userModel);

    add(new UserDetailHistoryLogPanel("audits", userModel));
  }
}
