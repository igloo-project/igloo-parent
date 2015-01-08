package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.SecurityUserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.validator.EmailExistsValidator;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordRecoveryPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordRecoveryPage.class);

	private final IModel<String> emailModel = Model.of("");

	@SpringBean
	private IUserService userService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public SecurityPasswordRecoveryPage(PageParameters parameters) {
		super(parameters);
		
		// Ca n'a pas de sens d'être connecté sur cette page.
		BasicApplicationSession.get().signOut();
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("security.password.recovery.pageTitle")));
	}

	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(SecurityPasswordRecoveryPage.class)
				.build();
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.recovery.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new Fragment(wicketId, "introFragment", this);
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		Fragment content = new Fragment(wicketId, "contentFragment", this);
		
		Form<?> form = new Form<Void>("form");
		
		content.add(
				form
						.add(
								new RequiredTextField<>("email", emailModel)
										.setLabel(new ResourceModel("business.user.email"))
										.add(EmailAddressValidator.getInstance())
										.add(EmailExistsValidator.get())
										.add(new LabelPlaceholderBehavior()),
								
								new AjaxButton("validate", form) {
									private static final long serialVersionUID = 1L;
									
									@Override
									protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
										try {
											User user = userService.getByEmailCaseInsensitive(emailModel.getObject());
											securityManagementService.initiatePasswordRecoveryRequest(
													user,
													UserPasswordRecoveryRequestType.RESET,
													UserPasswordRecoveryRequestInitiator.USER
											);
											
											getSession().success(getString("security.password.recovery.validate.success"));
											
											throw SecurityUserTypeDescriptor.USER.signInPageLinkDescriptor().newRestartResponseException();
										} catch (RestartResponseException e) {
											throw e;
										} catch (Exception e) {
											LOGGER.error("Error occurred while password recovery", e);
											getSession().error(getString("common.error.unexpected"));
										}
										
										FeedbackUtils.refreshFeedback(target, getPage());
									}
									
									@Override
									protected void onError(AjaxRequestTarget target, Form<?> form) {
										FeedbackUtils.refreshFeedback(target, getPage());
									}
								}
						)
		);
		
		return content;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		emailModel.detach();
	}
}
