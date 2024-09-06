package basicapp.front.security.login.page;

import basicapp.front.common.template.ApplicationAccessTemplate;
import basicapp.front.security.login.component.SignInContentPanel;
import basicapp.front.security.login.component.SignInFooterPanel;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SignInPage extends ApplicationAccessTemplate {

  private static final long serialVersionUID = 7361718534092594202L;

  public static ILinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(SignInPage.class);
  }

  public SignInPage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("navigation.signIn")));
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("signIn.title");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new SignInContentPanel(wicketId);
  }

  @Override
  protected Component getFooterComponent(String wicketId) {
    return new SignInFooterPanel(wicketId);
  }
}
