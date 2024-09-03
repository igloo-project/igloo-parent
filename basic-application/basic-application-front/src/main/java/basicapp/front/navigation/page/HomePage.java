package basicapp.front.navigation.page;

import basicapp.front.administration.page.AdministrationBasicUserListPage;
import basicapp.front.common.template.MainTemplate;
import basicapp.front.profile.page.ProfilePage;
import basicapp.front.referencedata.page.ReferenceDataPage;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class HomePage extends MainTemplate {

  private static final long serialVersionUID = -6767518941118385548L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(HomePage.class);
  }

  public HomePage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("home.pageTitle"), HomePage.linkDescriptor()));

    add(new CoreLabel("pageTitle", new ResourceModel("home.pageTitle")));

    add(
        AdministrationBasicUserListPage.linkDescriptor().link("users").hideIfInvalid(),
        ReferenceDataPage.linkDescriptor().link("referenceData").hideIfInvalid(),
        ProfilePage.linkDescriptor().link("profile").hideIfInvalid());
  }

  @Override
  protected Condition displayBreadcrumb() {
    return Condition.alwaysFalse();
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return HomePage.class;
  }
}