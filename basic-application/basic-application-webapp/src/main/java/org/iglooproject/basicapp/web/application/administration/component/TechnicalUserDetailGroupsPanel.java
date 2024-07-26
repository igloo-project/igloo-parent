package org.iglooproject.basicapp.web.application.administration.component;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.model.UserGroupDataProvider;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupDetailPage;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnicalUserDetailGroupsPanel extends GenericPanel<TechnicalUser> {

  private static final long serialVersionUID = 7856143985509286978L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(TechnicalUserDetailGroupsPanel.class);

  @SpringBean private IUserGroupService userGroupService;

  @SpringBean private IPropertyService propertyService;

  private final UserGroupDataProvider dataProvider;

  public TechnicalUserDetailGroupsPanel(
      String id, final IModel<? extends TechnicalUser> userModel) {
    super(id, userModel);

    this.dataProvider = new UserGroupDataProvider(userModel);

    add(
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(new ResourceModel("business.userGroup.name"))
            .withLink(AdministrationUserGroupDetailPage.MAPPER)
            .withClass("cell-w-300")
            .addActionColumn()
            .addConfirmAction(ActionRenderers.remove())
            .title(
                new ResourceModel(
                    "administration.userGroup.detail.users.action.remove.confirmation.title"))
            .content(
                parameter ->
                    new StringResourceModel(
                            "administration.userGroup.detail.users.action.remove.confirmation.content")
                        .setParameters(
                            userModel.getObject().getFullName(), parameter.getObject().getName()))
            .confirm()
            .onClick(
                new IOneParameterAjaxAction<IModel<UserGroup>>() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void execute(AjaxRequestTarget target, IModel<UserGroup> parameter) {
                    try {
                      UserGroup userGroup = parameter.getObject();
                      User user = userModel.getObject();

                      userGroupService.removeUser(userGroup, user);
                      Session.get().success(getString("common.success"));
                      throw new RestartResponseException(getPage());
                    } catch (RestartResponseException e) {
                      throw e;
                    } catch (Exception e) {
                      LOGGER.error(
                          "Unknown error occured while removing a user from a usergroup", e);
                      Session.get().error(getString("common.error.unexpected"));
                      FeedbackUtils.refreshFeedback(target, getPage());
                    }
                  }
                })
            .hideLabel()
            .withClassOnElements(BTN_TABLE_ROW_ACTION)
            .end()
            .withClass("cell-w-actions-1x cell-w-fit")
            .bootstrapCard()
            .addIn(
                AddInPlacement.FOOTER_MAIN,
                (wicketId, table) -> new UserGroupAddFragment(wicketId, userModel))
            .ajaxPager(AddInPlacement.HEADING_RIGHT)
            .count("administration.user.detail.groups.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION)));
  }

  private class UserGroupAddFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public UserGroupAddFragment(String id, IModel<? extends TechnicalUser> userModel) {
      super(id, "addGroup", TechnicalUserDetailGroupsPanel.this);

      IModel<UserGroup> userGroupModel = new GenericEntityModel<>();

      add(
          new Form<UserGroup>("form", userGroupModel)
              .add(
                  new UserGroupDropDownSingleChoice("userGroup", userGroupModel)
                      .setLabel(new ResourceModel("business.userGroup"))
                      .setRequired(true)
                      .add(new LabelPlaceholderBehavior()),
                  new AjaxButton("submit") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                      try {
                        UserGroup userGroup = userGroupModel.getObject();
                        User user = userModel.getObject();

                        if (!user.getGroups().contains(userGroup)) {
                          userGroupService.addUser(userGroup, user);
                          Session.get().success(getString("common.success"));
                        } else {
                          LOGGER.warn("User already added to this group.");
                          Session.get()
                              .warn(
                                  getString(
                                      "administration.userGroup.detail.users.action.add.error.duplicate"));
                        }

                        userGroupModel.setObject(null);
                        userGroupModel.detach();
                        target.add(getPage());
                      } catch (Exception e) {
                        LOGGER.error("Error when adding user to user group.", e);
                        Session.get().error(getString("common.error.unexpected"));
                      }
                      FeedbackUtils.refreshFeedback(target, getPage());
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target) {
                      FeedbackUtils.refreshFeedback(target, getPage());
                    }
                  }));
    }
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(dataProvider);
  }
}
