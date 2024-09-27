package basicapp.front.user.panel;

import static basicapp.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import basicapp.back.business.user.service.IUserGroupService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.common.renderer.ActionRenderers;
import basicapp.front.usergroup.form.UserGroupDropDownSingleChoice;
import basicapp.front.usergroup.model.UserGroupDataProvider;
import basicapp.front.usergroup.page.UserGroupDetailPage;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicUserDetailGeneralGroupsPanel extends GenericPanel<BasicUser> {

  private static final long serialVersionUID = 7856143985509286978L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(BasicUserDetailGeneralGroupsPanel.class);

  @SpringBean private IUserGroupService userGroupService;

  @SpringBean private IPropertyService propertyService;

  private final UserGroupDataProvider dataProvider;

  public BasicUserDetailGeneralGroupsPanel(String id, final IModel<BasicUser> userModel) {
    super(id, userModel);

    dataProvider =
        new UserGroupDataProvider(
            dataModel ->
                dataModel.bind(Bindings.userGroupSearchQueryData().basicUser(), userModel));

    add(
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(new ResourceModel("business.userGroup.name"))
            .withLink(UserGroupDetailPage.MAPPER)
            .withClass("cell-w-300")
            .addActionColumn()
            .addConfirmAction(ActionRenderers.remove())
            .title(new ResourceModel("common.action.remove"))
            .content(new ResourceModel("common.action.confirm.content"))
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
            .count("userGroup.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION)));
  }

  private class UserGroupAddFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public UserGroupAddFragment(String id, IModel<? extends BasicUser> userModel) {
      super(id, "addGroup", BasicUserDetailGeneralGroupsPanel.this);

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
                          LOGGER.warn("User already in this user group.");
                          Session.get()
                              .warn(getString("user.detail.general.groups.add.error.duplicate"));
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
