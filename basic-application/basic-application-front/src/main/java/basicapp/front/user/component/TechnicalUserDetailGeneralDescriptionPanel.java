package basicapp.front.user.component;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_TECHNICAL_WRITE;

import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import basicapp.front.user.popup.TechnicalUserSavePopup;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class TechnicalUserDetailGeneralDescriptionPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public TechnicalUserDetailGeneralDescriptionPanel(String id, final IModel<User> userModel) {
    super(id, userModel);

    TechnicalUserSavePopup editPopup = new TechnicalUserSavePopup("editPopup");
    add(editPopup);

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
                        })
                    .add(Condition.permission(userModel, USER_TECHNICAL_WRITE).thenShow())));
  }
}
