package basicapp.front.security.password.page;

import basicapp.back.business.user.model.User;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.front.BasicApplicationApplication;
import basicapp.front.BasicApplicationSession;
import basicapp.front.security.password.component.SecurityPasswordCreationContentPanel;
import basicapp.front.security.password.template.SecurityPasswordTemplate;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
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

public class SecurityPasswordCreationPage extends SecurityPasswordTemplate {

  private static final long serialVersionUID = 3082299089180143270L;

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
          .page(SecurityPasswordCreationPage.class);

  private final IModel<User> userModel = new GenericEntityModel<>();

  private final IModel<String> tokenModel = Model.of("");

  @SpringBean private ISecurityManagementControllerService securityManagementControllerService;

  public SecurityPasswordCreationPage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("security.password.creation.title")));

    MAPPER
        .map(userModel, tokenModel)
        .extractSafely(
            parameters,
            BasicApplicationApplication.get().getHomePageLinkDescriptor(),
            getString("common.error.unexpected"));

    if (!securityManagementControllerService.isPasswordRecoveryRequestValid(
        userModel.getObject(), tokenModel.getObject())) {
      BasicApplicationSession.get().error(getString("security.password.creation.error"));
      throw BasicApplicationApplication.get()
          .getHomePageLinkDescriptor()
          .newRestartResponseException();
    }
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("security.password.creation.title");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new SecurityPasswordCreationContentPanel(wicketId, userModel, tokenModel);
  }

  @Override
  public Condition keepSignedIn() {
    return Condition.alwaysFalse();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel, tokenModel);
  }
}
