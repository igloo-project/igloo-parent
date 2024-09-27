package basicapp.front.user.panel.tab;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.front.user.panel.TechnicalUserDetailGeneralDescriptionPanel;
import basicapp.front.user.panel.TechnicalUserDetailGeneralGroupsPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class TechnicalUserDetailTabGeneralPanel extends GenericPanel<TechnicalUser> {

  private static final long serialVersionUID = 1L;

  public TechnicalUserDetailTabGeneralPanel(String id, IModel<TechnicalUser> userModel) {
    super(id, userModel);

    add(
        new TechnicalUserDetailGeneralDescriptionPanel("description", userModel),
        new TechnicalUserDetailGeneralGroupsPanel("groups", userModel));
  }
}
