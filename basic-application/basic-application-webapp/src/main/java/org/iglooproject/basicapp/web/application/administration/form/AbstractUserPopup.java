package org.iglooproject.basicapp.web.application.administration.form;

import java.util.Collections;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.common.validator.EmailUnicityValidator;
import org.iglooproject.basicapp.web.application.common.validator.UserPasswordValidator;
import org.iglooproject.basicapp.web.application.common.validator.UsernamePatternValidator;
import org.iglooproject.basicapp.web.application.common.validator.UsernameUnicityValidator;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.form.LocaleDropDownChoice;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserPopup<U extends User> extends AbstractAjaxModalPopupPanel<U> {

	private static final long serialVersionUID = -3575009149241618972L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserPopup.class);

	private static final UsernamePatternValidator USERNAME_PATTERN_VALIDATOR =
			new UsernamePatternValidator() {
				private static final long serialVersionUID = 1L;
				@Override
				protected IValidationError decorate(IValidationError error, IValidatable<String> validatable) {
					((ValidationError) error).setKeys(Collections.singletonList("common.validator.username.pattern"));
					return error;
				}
			};

	@SpringBean
	private IUserService userService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	@SpringBean
	private IPropertyService propertyService;

	private final IModel<FormMode> formModeModel = new Model<>(FormMode.ADD);

	private final UserTypeDescriptor<U> typeDescriptor;

	protected Form<?> userForm;

	protected PasswordTextField passwordField;

	protected PasswordTextField confirmPasswordField;

	private final IModel<String> passwordModel = Model.of();

	private final IModel<String> confirmPasswordModel = Model.of();

	protected AbstractUserPopup(String id, UserTypeDescriptor<U> typeDescriptor) {
		super(id, new GenericEntityModel<Long, U>());
		
		this.typeDescriptor = typeDescriptor;
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(
				wicketId,
				addModeCondition()
						.then(new ResourceModel("administration.user.action.add.title"))
						.otherwise(new StringResourceModel("administration.user.action.edit.title", getModel()))
		);
	}

	protected final Component createStandardUserFields(String wicketId) {
		DelegatedMarkupPanel standardFields = new DelegatedMarkupPanel(wicketId, "standardFields", AbstractUserPopup.class);
		
		passwordField = new PasswordTextField("password", passwordModel);
		confirmPasswordField = new PasswordTextField("confirmPassword", confirmPasswordModel);
		
		boolean passwordRequired = securityManagementService.getOptions(typeDescriptor.getEntityClass()).isPasswordAdminUpdateEnabled()
				&& !securityManagementService.getOptions(typeDescriptor.getEntityClass()).isPasswordUserRecoveryEnabled();
		
		standardFields.add(
				new RequiredTextField<String>("firstName", BindingModel.of(getModel(), Bindings.user().firstName()))
						.setLabel(new ResourceModel("business.user.firstName")),
				new RequiredTextField<String>("lastName", BindingModel.of(getModel(), Bindings.user().lastName()))
						.setLabel(new ResourceModel("business.user.lastName")),
				new RequiredTextField<String>("username", BindingModel.of(getModel(), Bindings.user().username()))
						.setLabel(new ResourceModel("business.user.username"))
						.add(USERNAME_PATTERN_VALIDATOR)
						.add(new UsernameUnicityValidator(getModel())),
				new EnclosureContainer("addContainer")
						.condition(addModeCondition())
						.add(
								new EnclosureContainer("passwordContainer")
										.condition(Condition.predicate(Model.of(securityManagementService.getOptions(typeDescriptor.getEntityClass()).isPasswordAdminUpdateEnabled()), Predicates2.isTrue()))
										.add(
												passwordField
														.setLabel(new ResourceModel("business.user.password"))
														.setRequired(passwordRequired)
														.add(
																new UserPasswordValidator(typeDescriptor)
																		.userModel(getModel())
														),
												new CoreLabel("passwordHelp",
														new ResourceModel(
																typeDescriptor.securityTypeDescriptor().resourceKeyGenerator().resourceKey("password.help"),
																new ResourceModel(UserTypeDescriptor.USER.securityTypeDescriptor().resourceKeyGenerator().resourceKey("password.help"))
														)
												),
												confirmPasswordField
														.setLabel(new ResourceModel("business.user.confirmPassword"))
														.setRequired(passwordRequired)
										),
								
								new CheckBox("active", BindingModel.of(getModel(), Bindings.user().active()))
										.setLabel(new ResourceModel("business.user.active"))
										.setOutputMarkupId(true)
						),
				new EmailTextField("email", BindingModel.of(getModel(), Bindings.user().email()))
						.setLabel(new ResourceModel("business.user.email"))
						.add(EmailAddressValidator.getInstance())
						.add(new EmailUnicityValidator(getModel())),
				new LocaleDropDownChoice("locale", BindingModel.of(getModel(), Bindings.user().locale()))
						.setLabel(new ResourceModel("business.user.locale"))
						.setRequired(true)
		);
		
		standardFields.add(
				AjaxConfirmLink.<U>build()
				.title(new ResourceModel("administration.user.action.disable.confirmation.title"))
				.content(new StringResourceModel("administration.user.action.disable.confirmation.content", getModel()))
				.confirm()
				.onClick(
						new IAjaxAction() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target) {
								getSession().success(getString("administration.user.action.disable.success"));
								target.add(getPage());
								FeedbackUtils.refreshFeedback(target, getPage());
							}
						}
				)
				.create("disable", getModel())
		);
		
		return standardFields;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, AbstractUserPopup.class);
		
		footer.add(
				new AjaxButton("save", userForm) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onAfterSubmit(AjaxRequestTarget target) {
						User user = AbstractUserPopup.this.getModelObject();
						
						try {
							if (addModeCondition().applies()) {
								String password = passwordModel.getObject();
								
								userService.create(user);
								userService.onCreate(user, BasicApplicationSession.get().getUser());
								
								if (StringUtils.hasText(password)) {
									securityManagementService.updatePassword(user, password, BasicApplicationSession.get().getUser());
								} else {
									securityManagementService.initiatePasswordRecoveryRequest(
											user,
											UserPasswordRecoveryRequestType.CREATION,
											UserPasswordRecoveryRequestInitiator.ADMIN,
											BasicApplicationSession.get().getUser()
									);
									
									getSession().success(getString("administration.user.action.add.success.notification"));
								}
								
								getSession().success(getString("common.success"));
								
								throw AdministrationUserDetailTemplate.<U>mapper()
										.ignoreParameter2()
										.map(AbstractUserPopup.this.getModel())
										.newRestartResponseException();
							} else {
								User authenticatedUser = BasicApplicationSession.get().getUser();
								if (authenticatedUser != null && authenticatedUser.equals(user) && user.getLocale() != null) {
									BasicApplicationSession.get().setLocale(user.getLocale());
								}
								userService.update(user);
								getSession().success(getString("common.success"));
								closePopup(target);
								target.add(getPage());
							}
						} catch (RestartResponseException e) { // NOSONAR
							throw e;
						} catch (Exception e) {
							if (addModeCondition().applies()) {
								LOGGER.error("Error occured while creating user", e);
							} else {
								LOGGER.error("Error occured while updating user", e);
							}
							Session.get().error(getString("common.error.unexpected"));
						}
						FeedbackUtils.refreshFeedback(target, getPage());
					}
					
					@Override
					protected void onError(AjaxRequestTarget target) {
						FeedbackUtils.refreshFeedback(target, getPage());
					}
				}
						.add(new CoreLabel(
								"label",
								addModeCondition()
										.then(new ResourceModel("common.action.create"))
										.otherwise(new ResourceModel("common.action.save"))
						))
		);
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	public void setUpAdd(U user) {
		if (user.getLocale() == null) {
			user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		}
		
		getModel().setObject(user);
		formModeModel.setObject(FormMode.ADD);
	}

	public void setUpEdit(U user) {
		if (user.getLocale() == null) {
			user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		}
		
		getModel().setObject(user);
		formModeModel.setObject(FormMode.EDIT);
	}

	protected Condition addModeCondition() {
		return FormMode.ADD.condition(formModeModel);
	}

	protected Condition editModeCondition() {
		return FormMode.EDIT.condition(formModeModel);
	}

	@Override
	public IModel<String> getCssClassNamesModel() {
		return Model.of("modal-user");
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(passwordModel, confirmPasswordModel);
	}

}
