package basicapp.front.profile.component;

import basicapp.back.business.user.model.User;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.back.util.binding.Bindings;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;

public class ProfileDescriptionPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISecurityManagementControllerService securityManagementController;

  public ProfileDescriptionPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    IModel<String> emailAddressValueModel =
        BindingModel.of(userModel, Bindings.user().emailAddress().value());

    add(
        new CoreLabel("username", BindingModel.of(userModel, Bindings.user().username()))
            .showPlaceholder(),
        new BooleanIcon("enabled", BindingModel.of(userModel, Bindings.user().enabled())),
        new EmailLink("emailAddress", emailAddressValueModel),
        new DefaultPlaceholderPanel("emailAddressPlaceholder")
            .condition(Condition.modelNotNull(emailAddressValueModel)),
        new CoreLabel("locale", BindingModel.of(userModel, Bindings.user().locale()))
            .showPlaceholder(),
        new CoreLabel("creationDate", BindingModel.of(userModel, Bindings.user().creation().date()))
            .showPlaceholder(),
        new CoreLabel(
                "modificationDate",
                BindingModel.of(userModel, Bindings.user().modification().date()))
            .showPlaceholder(),
        new CoreLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()))
            .showPlaceholder());
  }
}
