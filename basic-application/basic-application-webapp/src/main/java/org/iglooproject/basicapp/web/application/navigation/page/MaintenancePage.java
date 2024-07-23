package org.iglooproject.basicapp.web.application.navigation.page;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.common.template.ApplicationAccessTemplate;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class MaintenancePage extends ApplicationAccessTemplate {

  private static final long serialVersionUID = 7371109597310862894L;

  public MaintenancePage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("maintenance.pageTitle")));
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("maintenance.help.title");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new Fragment(wicketId, "contentFragment", this);
  }

  @Override
  protected boolean hasMaintenanceRestriction() {
    return false;
  }
}
