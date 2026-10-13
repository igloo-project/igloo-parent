package basicapp.front.profile.page;

import basicapp.back.business.user.model.User;
import basicapp.back.security.model.BasicApplicationPermissionConstants;
import basicapp.back.util.binding.Bindings;
import basicapp.front.BasicApplicationSession;
import basicapp.front.profile.component.ProfileDescriptionPanel;
import basicapp.front.profile.popup.ProfilePasswordEditPopup;
import basicapp.front.profile.template.ProfileTemplate;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class ProfilePage extends ProfileTemplate {

  private static final long serialVersionUID = -8757939680257114559L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ProfilePage.class);
  }

  private final IModel<User> userModel = BasicApplicationSession.get().getUserModel();

  public ProfilePage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));

    ProfilePasswordEditPopup passwordEditPopup =
        new ProfilePasswordEditPopup("passwordEditPopup", userModel);
    add(passwordEditPopup);

    add(new CoreLabel("title", BindingModel.of(userModel, Bindings.user().fullName())));

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("passwordEdit")
                    .add(new AjaxModalOpenBehavior(passwordEditPopup, MouseEvent.CLICK))
                    .add(
                        Condition.permission(
                                userModel,
                                BasicApplicationPermissionConstants.USER_PASSWORD_UPDATE_USER)
                            .thenShow())));

    add(new ProfileDescriptionPanel("description", userModel));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel);
  }
}
