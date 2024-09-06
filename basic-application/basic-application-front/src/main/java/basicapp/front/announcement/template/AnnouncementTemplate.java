package basicapp.front.announcement.template;

import basicapp.front.announcement.page.AnnouncementListPage;
import basicapp.front.common.template.MainTemplate;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

@AuthorizeInstantiation(CoreAuthorityConstants.ROLE_ADMIN)
public class AnnouncementTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  public AnnouncementTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.administration.announcement"),
            AnnouncementListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return AnnouncementListPage.class;
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return AnnouncementListPage.class;
  }
}
