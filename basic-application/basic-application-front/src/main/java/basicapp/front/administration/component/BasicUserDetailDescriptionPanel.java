package basicapp.front.administration.component;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.service.IUserService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.administration.form.BasicUserPopup;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
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
        new CoreLabel("creationDate", BindingModel.of(userModel, Bindings.user().creationDate()))
            .showPlaceholder(),
        new CoreLabel(
                "lastUpdateDate", BindingModel.of(userModel, Bindings.user().lastUpdateDate()))
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
                        })));
  }
}
