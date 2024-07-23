package org.iglooproject.basicapp.web.application.profile.component;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DateLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.util.DatePattern;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordEditPopup;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class ProfileDescriptionPanel extends GenericPanel<User> {

  private static final long serialVersionUID = -1923855993008983060L;

  @SpringBean private ISecurityManagementService securityManagementService;

  public ProfileDescriptionPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    UserPasswordEditPopup<User> passwordEditPopup =
        new UserPasswordEditPopup<>("passwordEditPopup", userModel);
    add(passwordEditPopup);

    IModel<String> emailModel = BindingModel.of(userModel, Bindings.user().email());

    add(
        new BlankLink("passwordEdit")
            .add(new AjaxModalOpenBehavior(passwordEditPopup, MouseEvent.CLICK))
            .add(
                Condition.isTrue(
                        () ->
                            securityManagementService
                                .getSecurityOptions(BasicApplicationSession.get().getUser())
                                .isPasswordUserUpdateEnabled())
                    .thenShow()),
        new CoreLabel("username", BindingModel.of(userModel, Bindings.user().username()))
            .showPlaceholder(),
        new BooleanIcon("enabled", BindingModel.of(userModel, Bindings.user().enabled())),
        new EmailLink("email", emailModel),
        new DefaultPlaceholderPanel("emailPlaceholder")
            .condition(Condition.modelNotNull(emailModel)),
        new CoreLabel("locale", BindingModel.of(userModel, Bindings.user().locale()))
            .showPlaceholder(),
        new DateLabel(
                "creationDate",
                BindingModel.of(userModel, Bindings.user().creationDate()),
                DatePattern.SHORT_DATETIME)
            .showPlaceholder(),
        new DateLabel(
                "lastUpdateDate",
                BindingModel.of(userModel, Bindings.user().lastUpdateDate()),
                DatePattern.SHORT_DATETIME)
            .showPlaceholder(),
        new DateLabel(
                "lastLoginDate",
                BindingModel.of(userModel, Bindings.user().lastLoginDate()),
                DatePattern.SHORT_DATETIME)
            .showPlaceholder());
  }
}
