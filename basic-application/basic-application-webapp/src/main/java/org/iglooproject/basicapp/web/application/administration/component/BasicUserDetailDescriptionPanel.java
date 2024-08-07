package org.iglooproject.basicapp.web.application.administration.component;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DateLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.util.DatePattern;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.BasicUserPopup;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class BasicUserDetailDescriptionPanel extends GenericPanel<BasicUser> {

  private static final long serialVersionUID = 8651898170121443991L;

  @SpringBean private IUserService userService;

  public BasicUserDetailDescriptionPanel(String id, final IModel<? extends BasicUser> userModel) {
    super(id, userModel);

    BasicUserPopup editPopup = new BasicUserPopup("editPopup");
    add(editPopup);

    IModel<String> emailModel = BindingModel.of(userModel, Bindings.user().email());

    add(
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

    add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("edit")
                    .add(
                        new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            editPopup.setUpEdit(getModelObject());
                          }
                        })));
  }
}
