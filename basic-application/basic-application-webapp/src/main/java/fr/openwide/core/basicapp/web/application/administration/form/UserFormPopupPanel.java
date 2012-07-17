package fr.openwide.core.basicapp.web.application.administration.form;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox.FormPanelMode;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component.AbstractAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserFormPopupPanel extends AbstractAjaxModalPopupPanel<User> {

	private static final long serialVersionUID = -3575009149241618972L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserFormPopupPanel.class);

	private static final String USERNAME_VALIDATION_PATTERN = "[A-Za-z0-9\\-\\_]+";

	private static final PatternValidator USERNAME_PATTERN_VALIDATOR;

	@SpringBean
	private IUserService userService;

	private FormPanelMode mode;

	private Form<User> userForm;
	
	private PasswordTextField newPasswordField;
	
	private PasswordTextField confirmPasswordField;

	static {
		USERNAME_PATTERN_VALIDATOR = new PatternValidator(USERNAME_VALIDATION_PATTERN) {
			private static final long serialVersionUID = -6755079891321764827L;
			
			@Override
			protected String resourceKey() {
				return "administration.user.form.userName.malformed";
			}
		};
	}

	public UserFormPopupPanel(String id, IModel<User> userModel) {
		this(id, userModel, FormPanelMode.EDIT);
	}

	public UserFormPopupPanel(String id) {
		this(id, new GenericEntityModel<Long, User>(new User()), FormPanelMode.ADD);
	}

	public UserFormPopupPanel(String id, IModel<User> userModel, FormPanelMode mode) {
		super(id, userModel);
		
		this.mode = mode;
	}

	@Override
	protected Component createHeader(String wicketId) {
		if (isAddMode()) {
			return new Label(wicketId, new ResourceModel("administration.user.form.addTitle"));
		} else {
			return new Label(wicketId, new StringResourceModel("administration.user.form.editTitle", null,
					new Object[] { getModelObject().getFullName() }));
		}
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, UserFormPopupPanel.class);
		
		userForm = new Form<User>("form", getModel());
		body.add(userForm);
		
		TextField<String> firstNameField = new RequiredTextField<String>("firstName", BindingModel.of(userForm.getModel(),
				Binding.user().firstName()));
		firstNameField.setLabel(new ResourceModel("administration.user.field.firstName"));
		userForm.add(firstNameField);
		
		TextField<String> lastNameField = new RequiredTextField<String>("lastName", BindingModel.of(userForm.getModel(),
				Binding.user().lastName()));
		lastNameField.setLabel(new ResourceModel("administration.user.field.lastName"));
		userForm.add(lastNameField);
		
		TextField<String> userNameField = new RequiredTextField<String>("userName", BindingModel.of(userForm.getModel(),
				Binding.user().userName()));
		userNameField.add(USERNAME_PATTERN_VALIDATOR);
		userNameField.setLabel(new ResourceModel("administration.user.field.userName"));
		userForm.add(userNameField);
		
		TextField<String> emailField = new EmailTextField("email", BindingModel.of(userForm.getModel(),
				Binding.user().email()));
		emailField.setLabel(new ResourceModel("administration.user.field.email"));
		userForm.add(emailField);
		
		CheckBox activeField = new CheckBox("active", BindingModel.of(userForm.getModel(), Binding.user().active()));
		activeField.setLabel(new ResourceModel("administration.user.field.active"));
		userForm.add(activeField);
		
		WebMarkupContainer passwordContainer = new WebMarkupContainer("passwordContainer") {
			private static final long serialVersionUID = 2727669661139358058L;
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(UserFormPopupPanel.this.isAddMode());
			}
		};
		userForm.add(passwordContainer);
		
		newPasswordField = new PasswordTextField("newPassword", Model.of(""));
		newPasswordField.setLabel(new ResourceModel("administration.user.field.newPassword"));
		newPasswordField.setRequired(true);
		passwordContainer.add(newPasswordField);
		
		confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
		confirmPasswordField.setLabel(new ResourceModel("administration.user.field.confirmPassword"));
		confirmPasswordField.setRequired(true);
		passwordContainer.add(confirmPasswordField);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, UserFormPopupPanel.class);
		
		// Bouton valider
		AjaxButton valider = new AjaxButton("save", userForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					User user = userForm.getModelObject();
					
					if (isAddMode()) {
						String newPasswordValue = newPasswordField.getModelObject();
						String confirmPasswordValue = confirmPasswordField.getModelObject();
						
						if (newPasswordValue != null && confirmPasswordValue != null) {
							if (confirmPasswordValue.equals(newPasswordValue)) {
								if (newPasswordValue.length() >= User.MIN_PASSWORD_LENGTH && 
										newPasswordValue.length() <= User.MAX_PASSWORD_LENGTH) {
									userService.create(user);
									userService.setPasswords(user, newPasswordValue);
									
									getSession().success(getString("administration.user.form.add.success"));
								} else {
									LOGGER.warn("Password does not fit criteria.");
									form.error(getString("administration.user.form.password.malformed"));
								}
							} else {
								LOGGER.warn("Password confirmation does not match.");
								form.error(getString("administration.user.form.password.wrongConfirmation"));
							}
						}
					} else {
						userService.update(user);
						getSession().success(getString("administration.user.form.edit.success"));
					}
					
					closePopup(target);
					target.add(getPage());
				} catch (ConstraintViolationException e) {
					LOGGER.warn("Username must be unique");
					Session.get().error(getString("administration.user.form.userName.notUnique"));
				} catch (Exception e) {
					if (isAddMode()) {
						LOGGER.error("Error occured while creating user", e);
						Session.get().error(getString("administration.user.form.add.error"));
					} else {
						LOGGER.error("Error occured while updating user", e);
						Session.get().error(getString("administration.user.form.edit.error"));
					}
				}
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		Label validerLabel;
		if (isAddMode()) {
			validerLabel = new Label("validerLabel", new ResourceModel("common.action.create"));
		} else {
			validerLabel = new Label("validerLabel", new ResourceModel("common.action.update"));
		}
		valider.add(validerLabel);
		footer.add(valider);
		
		// Bouton annuler
		AbstractLink annuler = new AbstractLink("cancel") {
			private static final long serialVersionUID = 1L;
		};
		addCancelBehavior(annuler);
		footer.add(annuler);
		
		return footer;
	}

	protected boolean isEditMode() {
		return FormPanelMode.EDIT.equals(mode);
	}

	protected boolean isAddMode() {
		return FormPanelMode.ADD.equals(mode);
	}

	@Override
	public IModel<String> classNamesModel() {
		return Model.of("modal-user-form");
	}
}
