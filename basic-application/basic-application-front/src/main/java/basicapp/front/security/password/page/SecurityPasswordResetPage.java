package basicapp.front.security.password.page;

import basicapp.back.business.user.model.User;
import basicapp.back.security.service.ISecurityManagementService;
import basicapp.front.BasicApplicationApplication;
import basicapp.front.BasicApplicationSession;
import basicapp.front.security.password.component.SecurityPasswordResetContentPanel;
import basicapp.front.security.password.template.SecurityPasswordTemplate;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class SecurityPasswordResetPage extends SecurityPasswordTemplate {

  private static final long serialVersionUID = -5308279301239220694L;

  public static final ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, User, String> MAPPER =
      LinkDescriptorBuilder.start()
          .model(User.class)
          .model(String.class)
          .pickFirst()
          .map(CommonParameters.ID)
          .mandatory()
          .pickSecond()
          .map(CommonParameters.TOKEN)
          .mandatory()
          .page(SecurityPasswordResetPage.class);

  private final IModel<User> userModel = new GenericEntityModel<>();

  @SpringBean private ISecurityManagementService securityManagementService;

  public SecurityPasswordResetPage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("security.password.reset.title")));

    final IModel<String> tokenModel = Model.of("");

    MAPPER
        .map(userModel, tokenModel)
        .extractSafely(
            parameters,
            BasicApplicationApplication.get().getHomePageLinkDescriptor(),
            getString("common.error.unexpected"));

    parameters.remove(CommonParameters.TOKEN);

    if (!tokenModel
        .getObject()
        .equals(userModel.getObject().getPasswordRecoveryRequest().getToken())) {
      Session.get().error(getString("security.password.reset.wrongToken"));
      throw BasicApplicationApplication.get()
          .getHomePageLinkDescriptor()
          .newRestartResponseException();
    }

    if (securityManagementService.isPasswordRecoveryRequestExpired(userModel.getObject())) {
      BasicApplicationSession.get().error(getString("security.password.reset.expired"));
      throw BasicApplicationApplication.get()
          .getHomePageLinkDescriptor()
          .newRestartResponseException();
    }
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("security.password.reset.title");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new SecurityPasswordResetContentPanel(wicketId, userModel);
  }

  @Override
  public Condition keepSignedIn() {
    return Condition.alwaysFalse();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel);
  }
}
