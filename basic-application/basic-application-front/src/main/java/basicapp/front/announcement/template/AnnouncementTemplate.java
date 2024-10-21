package basicapp.front.announcement.template;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_READ;

import basicapp.front.announcement.page.AnnouncementListPage;
import basicapp.front.common.template.MainTemplate;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_ANNOUNCEMENT_READ)
public abstract class AnnouncementTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected AnnouncementTemplate(PageParameters parameters) {
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
