package basicapp.front.profile.template;

import basicapp.front.common.template.MainTemplate;
import basicapp.front.profile.page.ProfilePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ProfileTemplate extends MainTemplate {

  private static final long serialVersionUID = 1029271113953538262L;

  protected ProfileTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.profile"), ProfilePage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ProfilePage.class;
  }
}
