package basicapp.front.user.component.tab;

import basicapp.back.business.user.model.User;
import basicapp.front.user.component.TechnicalUserDetailGeneralDescriptionPanel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class TechnicalUserDetailTabGeneralPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public TechnicalUserDetailTabGeneralPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    add(new TechnicalUserDetailGeneralDescriptionPanel("description", userModel));
  }
}
