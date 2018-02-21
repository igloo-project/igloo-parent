package org.iglooproject.basicapp.web.application.administration.template;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordUpdatePopup;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.navigation.link.LinkFactory;
import org.iglooproject.commons.util.functional.SerializableFunction;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.action.AbstractAjaxAction;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.ReadOnlyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import com.google.common.base.Function;

public class AdministrationUserDetailTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = -550100874222819991L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationUserDetailTemplate.class);

	private static final Function<User, Class<? extends Page>> USER_TO_FICHE_CLASS_FUNCTION =
			new SerializableFunction<User, Class<? extends Page>>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Class<? extends Page> apply(User input) {
					return input == null
							? null
							: UserTypeDescriptor.get(input).administrationTypeDescriptor().getDetailPageClass();
				}
			};
			
	public static final <U extends User> ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, U, Page> mapper() {
		return mapper(User.class).<U>castParameter1();
	}
	
	private static final <U extends User> ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, U, Page> mapper(Class<U> clazz) {
		return LinkDescriptorBuilder.start()
				.model(clazz)
				.model(Page.class)
				.pickFirst().map(CommonParameters.ID).mandatory()
				.pickSecond().map(CommonParameters.SOURCE_PAGE_ID).optional()
				.pickFirst().page(DetachableFactories.forUnit(
						ReadOnlyModel.<U, Class<? extends Page>>factory(USER_TO_FICHE_CLASS_FUNCTION)
				));
	}
	
	@SpringBean
	private IUserService userService;
	
	@SpringBean
	private ISecurityManagementService securityManagementService;
	
	protected final UserTypeDescriptor<U> typeDescriptor;
	
	protected final IModel<U> userModel = new GenericEntityModel<Long, U>(null);
	
	protected final IModel<Page> sourcePageModel = new PageModel<Page>();
	
	public AdministrationUserDetailTemplate(PageParameters parameters, UserTypeDescriptor<U> typeDescriptor) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		mapper(typeDescriptor.getEntityClass())
				.map(userModel, sourcePageModel)
				.extractSafely(
						parameters,
						typeDescriptor.administrationTypeDescriptor().list(),
						getString("common.error.unexpected")
				);
		
		Component backToSourcePage =
				LinkFactory.get().linkGenerator(
						sourcePageModel,
						typeDescriptor.administrationTypeDescriptor().getListPageClass()
				)
				.link("backToSourcePage").hideIfInvalid();
		
		UserPasswordUpdatePopup<U> passwordEditPopup = new UserPasswordUpdatePopup<>("passwordEditPopup", userModel);
		
		add(
				new CoreLabel("pageTitle", BindingModel.of(userModel, Bindings.user().fullName())),
				
				passwordEditPopup,
				new BlankLink("passwordEdit")
						.add(new AjaxModalOpenBehavior(passwordEditPopup, MouseEvent.CLICK))
						.add(
								Condition.isTrue(
										Model.of(securityManagementService.getOptions(userModel.getObject()).isPasswordAdminUpdateEnabled())
								).thenShow()
						),
				
				AjaxConfirmLink.<U>build()
						.title(new ResourceModel("administration.user.action.password.recovery.reset.confirmation.title"))
						.content(new StringResourceModel("administration.user.action.password.recovery.reset.confirmation.content", userModel))
						.confirm()
						.onClick(new AbstractAjaxAction() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target) {
								try {
									securityManagementService.initiatePasswordRecoveryRequest(userModel.getObject(),
											UserPasswordRecoveryRequestType.RESET,
											UserPasswordRecoveryRequestInitiator.ADMIN,
											BasicApplicationSession.get().getUser()
									);
									getSession().success(getString("administration.user.action.password.recovery.reset.success"));
									target.add(getPage());
								} catch (Exception e) {
									LOGGER.error("Error occured while sending a password recovery request", e);
									getSession().error(getString("common.error.unexpected"));
								}
								FeedbackUtils.refreshFeedback(target, getPage());
							}
						})
						.create("passwordReset", userModel)
						.add(
								Condition.isTrue(
										Model.of(securityManagementService.getOptions(userModel.getObject()).isPasswordAdminRecoveryEnabled())
								).thenShow()
						),
				
				new AjaxLink<U>("enable", userModel) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							userService.setActive(getModelObject(), true);
							getSession().success(getString("administration.user.action.enable.success"));
							target.add(getPage());
						} catch (Exception e) {
							LOGGER.error("Error occured while enabling user", e);
							getSession().error(getString("common.error.unexpected"));
						}
						FeedbackUtils.refreshFeedback(target, getPage());
					}
				}
						.add(Condition.isFalse(BindingModel.of(userModel, Bindings.user().active())).thenShow()),
				
				AjaxConfirmLink.<U>build()
						.title(new ResourceModel("administration.user.action.disable.confirmation.title"))
						.content(new StringResourceModel("administration.user.action.disable.confirmation.content", userModel))
						.confirm()
						.onClick(
								new AbstractAjaxAction() {
									private static final long serialVersionUID = 1L;
									@Override
									public void execute(AjaxRequestTarget target) {
										try {
											userService.setActive(userModel.getObject(), false);
											getSession().success(getString("administration.user.action.disable.success"));
										} catch (Exception e) {
											LOGGER.error("Error occured while disabling user", e);
											getSession().error(getString("common.error.unexpected"));
										}
										target.add(getPage());
										FeedbackUtils.refreshFeedback(target, getPage());
									}
								}
						)
						.create("disable", userModel)
						.add(
								new Condition() {
									private static final long serialVersionUID = 1L;
									@Override
									public boolean applies() {
										User user = userModel.getObject();
										User currentUser = BasicApplicationSession.get().getUser();
										return !user.equals(currentUser) && user.isActive();
									}
								}.thenShow()
						),
						
				backToSourcePage,
				typeDescriptor.administrationTypeDescriptor().list().link("backToList")
						.add(Condition.componentVisible(backToSourcePage).thenHide())
		);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
		sourcePageModel.detach();
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return typeDescriptor.administrationTypeDescriptor().getListPageClass();
	}
}
