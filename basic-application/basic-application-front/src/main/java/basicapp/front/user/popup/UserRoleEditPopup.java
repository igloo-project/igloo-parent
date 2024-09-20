package basicapp.front.user.popup;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.controller.IUserControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.common.renderer.RoleRenderer;
import basicapp.front.role.model.RoleDataProvider;
import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.CheckGroup;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoleEditPopup extends AbstractAjaxModalPopupPanel<User> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleEditPopup.class);

  @SpringBean private IUserControllerService userControllerService;

  private Form<User> userForm;

  public UserRoleEditPopup(String id, IModel<User> userModel) {
    super(id, userModel);
  }

  @Override
  protected Component createHeader(String wicketId) {
    return new CoreLabel(wicketId, new StringResourceModel("user.roles.edit.title", getModel()));
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, UserRoleEditPopup.class);

    userForm = new Form<>("form", getModel());
    body.add(
        userForm.add(
            new CheckGroup<>(
                    "rolesGroup",
                    BindingModel.of(userForm.getModel(), Bindings.user().roles()),
                    Suppliers2.treeSet())
                .add(
                    new SequenceView<>("roles", new RoleDataProvider()) {
                      private static final long serialVersionUID = 1L;

                      @Override
                      protected void populateItem(Item<Role> item) {
                        item.add(
                            new Check<>("roleCheck", item.getModel())
                                .setLabel(RoleRenderer.get().asModel(item.getModel())));
                      }
                    }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()))
                .setRenderBodyOnly(false)));

    return body;
  }

  @Override
  protected Component createFooter(String wicketId) {
    DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, UserRoleEditPopup.class);

    // Validate button
    AjaxButton validate =
        new AjaxButton("save", userForm) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            User user = UserRoleEditPopup.this.getModelObject();

            try {
              userControllerService.affectRoles(user);
              Session.get().success(getString("user.roles.edit.success"));
              closePopup(target);
              target.add(getPage());
            } catch (RestartResponseException e) { // NOSONAR
              throw e;
            } catch (Exception e) {
              LOGGER.error("Error occured while updating user roles", e);
              Session.get().error(getString("common.error.unexpected"));
            }
            FeedbackUtils.refreshFeedback(target, getPage());
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        };
    Label validateLabel;
    validateLabel = new CoreLabel("validateLabel", new ResourceModel("common.action.save"));
    validate.add(validateLabel);
    footer.add(validate);

    // Cancel button
    BlankLink cancel = new BlankLink("cancel");
    addCancelBehavior(cancel);
    footer.add(cancel);

    return footer;
  }
}
