package org.iglooproject.basicapp.web.application.security.password.page;

import igloo.wicket.condition.Condition;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.security.password.component.SecurityPasswordRecoveryRequestResetContentPanel;
import org.iglooproject.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordRecoveryRequestResetPage extends SecurityPasswordTemplate {

  private static final long serialVersionUID = 547223775134254240L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(SecurityPasswordRecoveryRequestResetPage.class);
  }

  public SecurityPasswordRecoveryRequestResetPage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(
            new ResourceModel("security.password.recovery.request.reset.pageTitle")));
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("security.password.recovery.request.reset.pageTitle");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new SecurityPasswordRecoveryRequestResetContentPanel(wicketId);
  }

  @Override
  public Condition keepSignedIn() {
    return Condition.alwaysFalse();
  }
}
