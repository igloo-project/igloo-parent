package org.iglooproject.basicapp.web.application.administration.page;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.business.user.predicate.UserPredicates;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.component.tab.AdministrationTechnicalUserDetailTabMainInformationPanel;
import org.iglooproject.basicapp.web.application.administration.component.tab.AdministrationTechnicalUserDetailTabSecurityPanel;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordEditPopup;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.common.renderer.UserActiveRenderer;
import org.iglooproject.basicapp.web.application.common.util.BootstrapTabsUtils;
import org.iglooproject.basicapp.web.application.navigation.link.LinkFactory;
import org.iglooproject.wicket.bootstrap4.markup.html.bootstrap.component.BootstrapBadge;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tab.BootstrapTabBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.BindingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class AdministrationTechnicalUserDetailPage extends AdministrationUserDetailTemplate<TechnicalUser> {

	private static final long serialVersionUID = -446179167337478755L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationTechnicalUserDetailPage.class);

	public static final ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, TechnicalUser, Page> MAPPER =
		AdministrationUserDetailTemplate.mapper(TechnicalUser.class);

	public static final String ANCHOR_TAB_MAIN_INFORMATION = "tab-main-information";
	public static final String ANCHOR_TAB_SECURITY = "tab-security";

	public AdministrationTechnicalUserDetailPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("navigation.administration.user.basicUser"),
			AdministrationTechnicalUserListPage.linkDescriptor()
		));
		addBreadCrumbElement(new BreadCrumbElement(
			BindingModel.of(userModel, Bindings.user().fullName())
		));
		
		MAPPER
			.map(userModel, sourcePageModel)
			.extractSafely(
				parameters,
				AdministrationTechnicalUserListPage.linkDescriptor(),
				getString("common.error.unexpected")
			);
		
		Component backToSourcePage =
			LinkFactory.get().linkGenerator(
				sourcePageModel,
				AdministrationTechnicalUserListPage.class
			)
			.link("backToSourcePage").hideIfInvalid();
		
		add(
			backToSourcePage,
			AdministrationTechnicalUserListPage.linkDescriptor()
				.link("backToList")
				.add(Condition.componentVisible(backToSourcePage).thenHide()),
			
			new CoreLabel("pageTitle", BindingModel.of(userModel, Bindings.user().fullName()))
		);
		
		UserPasswordEditPopup<TechnicalUser> passwordEditPopup = new UserPasswordEditPopup<>("passwordEditPopup", userModel);
		add(passwordEditPopup);
		
		EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
		add(headerElementsSection.anyChildVisible());
		
		headerElementsSection
			.add(
				new EnclosureContainer("informationContainer")
					.anyChildVisible()
					.add(
						new BootstrapBadge<>("active", userModel, UserActiveRenderer.get())
					)
			);
		
		headerElementsSection
			.add(
				new EnclosureContainer("actionsContainer")
					.anyChildVisible()
					.add(
						new BlankLink("passwordEdit")
							.add(new AjaxModalOpenBehavior(passwordEditPopup, MouseEvent.CLICK))
							.add(
								Condition.isTrue(() -> securityManagementService.getSecurityOptions(userModel.getObject()).isPasswordAdminUpdateEnabled())
									.thenShow()
							),
						
						AjaxConfirmLink.<TechnicalUser>build()
							.title(new ResourceModel("administration.user.action.password.recovery.reset.confirmation.title"))
							.content(new StringResourceModel("administration.user.action.password.recovery.reset.confirmation.content", userModel))
							.confirm()
							.onClick(new IAjaxAction() {
								private static final long serialVersionUID = 1L;
								@Override
								public void execute(AjaxRequestTarget target) {
									try {
										securityManagementService.initiatePasswordRecoveryRequest(
											userModel.getObject(),
											UserPasswordRecoveryRequestType.RESET,
											UserPasswordRecoveryRequestInitiator.ADMIN,
											BasicApplicationSession.get().getUser()
										);
										Session.get().success(getString("administration.user.action.password.recovery.reset.success"));
										target.add(getPage());
									} catch (Exception e) {
										LOGGER.error("Error occured while sending a password recovery request", e);
										Session.get().error(getString("common.error.unexpected"));
									}
									FeedbackUtils.refreshFeedback(target, getPage());
								}
							})
							.create("passwordReset", userModel)
							.add(
								Condition.isTrue(() -> securityManagementService.getSecurityOptions(userModel.getObject()).isPasswordAdminRecoveryEnabled())
									.thenShow()
							),
						
						new AjaxLink<TechnicalUser>("enable", userModel) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick(AjaxRequestTarget target) {
								try {
									userService.setActive(getModelObject(), true);
									Session.get().success(getString("administration.user.action.enable.success"));
									target.add(getPage());
								} catch (Exception e) {
									LOGGER.error("Error occured while enabling user", e);
									Session.get().error(getString("common.error.unexpected"));
								}
								FeedbackUtils.refreshFeedback(target, getPage());
							}
						}
							.add(
								Condition.predicate(userModel, UserPredicates.inactive())
									.thenShow()
							),
						
						AjaxConfirmLink.<TechnicalUser>build()
							.title(new ResourceModel("administration.user.action.disable.confirmation.title"))
							.content(new StringResourceModel("administration.user.action.disable.confirmation.content", userModel))
							.confirm()
							.onClick(new IAjaxAction() {
								private static final long serialVersionUID = 1L;
								@Override
								public void execute(AjaxRequestTarget target) {
									try {
										userService.setActive(userModel.getObject(), false);
										Session.get().success(getString("administration.user.action.disable.success"));
									} catch (Exception e) {
										LOGGER.error("Error occured while disabling user", e);
										Session.get().error(getString("common.error.unexpected"));
									}
									target.add(getPage());
									FeedbackUtils.refreshFeedback(target, getPage());
								}
							})
							.create("disable", userModel)
							.add(
								Condition.and(
									Condition.isEqual(BasicApplicationSession.get().getUserModel(), userModel).negate(),
									Condition.predicate(userModel, UserPredicates.active())
								)
									.thenShow()
							)
					)
			);
		
		add(
			BootstrapTabsUtils.buildTabLink("mainInformationTabLink", ANCHOR_TAB_MAIN_INFORMATION),
			new AdministrationTechnicalUserDetailTabMainInformationPanel("mainInformation", userModel),
			
			BootstrapTabsUtils.buildTabLink("securityTabLink", ANCHOR_TAB_SECURITY),
			new AdministrationTechnicalUserDetailTabSecurityPanel("security", userModel)
		);
		
		add(new BootstrapTabBehavior());
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationTechnicalUserListPage.class;
	}

}
