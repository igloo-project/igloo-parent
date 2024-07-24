package basicapp.front.administration.component.tab;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.front.administration.component.UserDetailHistoryLogPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class AdministrationTechnicalUserDetailTabSecurityPanel extends GenericPanel<TechnicalUser> {

  private static final long serialVersionUID = -341225850371484771L;

  public AdministrationTechnicalUserDetailTabSecurityPanel(
      String id, final IModel<? extends TechnicalUser> userModel) {
    super(id, userModel);

    add(new UserDetailHistoryLogPanel("audits", userModel));
  }
}
