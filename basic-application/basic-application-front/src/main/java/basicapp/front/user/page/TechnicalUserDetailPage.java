package basicapp.front.user.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.ADMIN_RECOVERY_PASSWORD;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_DISABLE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_ENABLE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_READ;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.service.controller.IUserControllerService;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.BasicApplicationSession;
import basicapp.front.common.util.BootstrapTabsUtils;
import basicapp.front.navigation.link.LinkFactory;
import basicapp.front.user.panel.tab.TechnicalUserDetailTabGeneralPanel;
import basicapp.front.user.panel.tab.TechnicalUserDetailTabSecurityPanel;
import basicapp.front.user.popup.UserPasswordEditPopup;
import basicapp.front.user.renderer.UserEnabledRenderer;
import basicapp.front.user.template.UserTemplate;
import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.bootstrap5.markup.html.bootstrap.component.BootstrapBadge;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

@AuthorizeInstantiation(ROLE_ADMIN)
public class TechnicalUserDetailPage extends UserTemplate {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalUserDetailPage.class);

  public static final ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, User, Page> MAPPER =
      LinkDescriptorBuilder.start()
          .model(User.class)
          .permission(USER_READ)
          .model(Page.class)
          .pickFirst()
          .map(CommonParameters.ID)
          .mandatory()
          .pickSecond()
          .map(CommonParameters.SOURCE_PAGE_ID)
          .optional()
          .page(TechnicalUserDetailPage.class);

  public static final String TAB_GENERAL_PANEL_ID = "general";
  public static final String TAB_GENERAL_TAB_ID =
      BootstrapTabsUtils.getTabMarkupId(TAB_GENERAL_PANEL_ID);
  public static final String TAB_SECURITY_PANEL_ID = "security";
  public static final String TAB_SECURITY_TAB_ID =
      BootstrapTabsUtils.getTabMarkupId(TAB_SECURITY_PANEL_ID);

  @SpringBean protected IUserControllerService userControllerService;

  @SpringBean protected ISecurityManagementControllerService securityManagementControllerService;

  protected final IModel<User> userModel = new GenericEntityModel<>();

  protected final IModel<Page> sourcePageModel = new PageModel<>();

  public TechnicalUserDetailPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.administration.technicalUser"),
            TechnicalUserListPage.linkDescriptor()));
    addBreadCrumbElement(
        new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));

    MAPPER
        .map(userModel, sourcePageModel)
        .extractSafely(
            parameters,
            TechnicalUserListPage.linkDescriptor(),
            getString("common.error.unexpected"));

    Component backToSourcePage =
        LinkFactory.get()
            .linkGenerator(sourcePageModel, TechnicalUserListPage.class)
            .link("backToSourcePage")
            .hideIfInvalid();

    add(
        backToSourcePage,
        TechnicalUserListPage.linkDescriptor()
            .link("backToList")
            .add(Condition.componentVisible(backToSourcePage).thenHide()),
        new CoreLabel("title", BindingModel.of(userModel, Bindings.user().fullName())));

    UserPasswordEditPopup passwordEditPopup =
        new UserPasswordEditPopup("passwordEditPopup", userModel);
    add(passwordEditPopup);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("informationContainer")
            .anyChildVisible()
            .add(
                new BootstrapBadge<>("enabled", userModel, UserEnabledRenderer.get()).badgePill()));

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("passwordEdit")
                    .add(new AjaxModalOpenBehavior(passwordEditPopup, MouseEvent.CLICK))
                    .add(Condition.permission(userModel, ADMIN_RECOVERY_PASSWORD).thenShow()),
                AjaxConfirmLink.<User>build()
                    .title(new ResourceModel("user.password.recovery.reset.confirm.title"))
                    .content(new ResourceModel("common.action.confirm.content"))
                    .confirm()
                    .onClick(
                        new IAjaxAction() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void execute(AjaxRequestTarget target) {
                            try {
                              securityManagementControllerService.initiatePasswordRecoveryRequest(
                                  userModel.getObject(),
                                  UserPasswordRecoveryRequestType.RESET,
                                  UserPasswordRecoveryRequestInitiator.ADMIN,
                                  BasicApplicationSession.get().getUser());
                              Session.get().success(getString("common.success"));
                              target.add(getPage());
                            } catch (Exception e) {
                              LOGGER.error(
                                  "Error occured while sending a password recovery request", e);
                              Session.get().error(getString("common.error.unexpected"));
                            }
                            FeedbackUtils.refreshFeedback(target, getPage());
                          }
                        })
                    .create("passwordReset", userModel)
                    .add(Condition.permission(userModel, ADMIN_RECOVERY_PASSWORD).thenShow()),
                new AjaxLink<>("enable", userModel) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    try {
                      userControllerService.enable(getModelObject());
                      Session.get().success(getString("common.success"));
                      target.add(getPage());
                    } catch (Exception e) {
                      LOGGER.error("Error occured while enabling user", e);
                      Session.get().error(getString("common.error.unexpected"));
                    }
                    FeedbackUtils.refreshFeedback(target, getPage());
                  }
                }.add(Condition.permission(userModel, USER_ENABLE).thenShow()),
                AjaxConfirmLink.<User>build()
                    .title(new ResourceModel("common.action.disable"))
                    .content(new ResourceModel("common.action.confirm.content"))
                    .confirm()
                    .onClick(
                        new IAjaxAction() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void execute(AjaxRequestTarget target) {
                            try {
                              userControllerService.disable(userModel.getObject());
                              Session.get().success(getString("common.success"));
                            } catch (Exception e) {
                              LOGGER.error("Error occured while disabling user", e);
                              Session.get().error(getString("common.error.unexpected"));
                            }
                            target.add(getPage());
                            FeedbackUtils.refreshFeedback(target, getPage());
                          }
                        })
                    .create("disable", userModel)
                    .add(Condition.permission(userModel, USER_DISABLE).thenShow())));

    add(
        BootstrapTabsUtils.build(
            TAB_GENERAL_TAB_ID,
            TAB_GENERAL_PANEL_ID,
            new WebMarkupContainer("generalTab"),
            new TechnicalUserDetailTabGeneralPanel("general", userModel),
            () -> true));

    add(
        BootstrapTabsUtils.build(
            TAB_SECURITY_TAB_ID,
            TAB_SECURITY_PANEL_ID,
            new WebMarkupContainer("securityTab"),
            new TechnicalUserDetailTabSecurityPanel("security", userModel),
            () -> false));
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return TechnicalUserListPage.class;
  }
}
