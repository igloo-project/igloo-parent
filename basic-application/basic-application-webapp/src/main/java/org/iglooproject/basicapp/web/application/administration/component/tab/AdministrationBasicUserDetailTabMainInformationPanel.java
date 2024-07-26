package org.iglooproject.basicapp.web.application.administration.component.tab;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.web.application.administration.component.BasicUserDetailDescriptionPanel;
import org.iglooproject.basicapp.web.application.administration.component.BasicUserDetailGroupsPanel;

public class AdministrationBasicUserDetailTabMainInformationPanel extends GenericPanel<BasicUser> {

  private static final long serialVersionUID = -3900528127687137340L;

  public AdministrationBasicUserDetailTabMainInformationPanel(
      String id, final IModel<? extends BasicUser> userModel) {
    super(id, userModel);

    add(
        new BasicUserDetailDescriptionPanel("description", userModel),
        new BasicUserDetailGroupsPanel("groups", userModel));
  }
}
