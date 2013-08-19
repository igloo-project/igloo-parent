package fr.openwide.core.showcase.web.application.portfolio.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.model.BindingModel;

public class EditUserPopupPanel extends AbstractAjaxModalPopupPanel<User> {
	private static final long serialVersionUID = 3066059572097078436L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EditUserPopupPanel.class);
	
	private static final UserBinding USER_BINDING = new UserBinding();
	
	@SpringBean
	private IUserService userService;
	
	private Form<User> userEditForm;
	
	public EditUserPopupPanel(String id, IModel<User> userModel) {
		super(id, userModel);
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new Label(wicketId, new ResourceModel("user.form.editTitle"));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, EditUserPopupPanel.class);
		
		userEditForm = new Form<User>("userEditForm", getModel());
		body.add(userEditForm);
		
		TextField<String> userNameField = new RequiredTextField<String>("userName",
				BindingModel.of(getModel(), USER_BINDING.userName()));
		userNameField.setLabel(new ResourceModel("user.username"));
		userEditForm.add(userNameField);
		
		TextField<String> firstNameField = new RequiredTextField<String>("firstName",
				BindingModel.of(getModel(), USER_BINDING.firstName()));
		firstNameField.setLabel(new ResourceModel("user.firstname"));
		userEditForm.add(firstNameField);
		
		TextField<String> lastNameField = new RequiredTextField<String>("lastName",
				BindingModel.of(getModel(), USER_BINDING.lastName()));
		lastNameField.setLabel(new ResourceModel("user.lastname"));
		userEditForm.add(lastNameField);
		
		TextField<String> emailField = new RequiredTextField<String>("email",
				BindingModel.of(getModel(), USER_BINDING.email()));
		emailField.setLabel(new ResourceModel("user.email"));
		userEditForm.add(emailField);
		
		CheckBox activeCheckBox = new CheckBox("active", BindingModel.of(getModel(), USER_BINDING.active()));
		activeCheckBox.setLabel(new ResourceModel("user.active"));
		userEditForm.add(activeCheckBox);
		
		TextField<String> phoneNumberField = new RequiredTextField<String>("phoneNumber",
				BindingModel.of(getModel(), USER_BINDING.phoneNumber()));
		phoneNumberField.setLabel(new ResourceModel("user.phone"));
		userEditForm.add(phoneNumberField);
		
		TextField<String> gsmNumberField = new TextField<String>("gsmNumber",
				BindingModel.of(getModel(), USER_BINDING.gsmNumber()));
		gsmNumberField.setLabel(new ResourceModel("user.phone"));
		userEditForm.add(gsmNumberField);
		
		TextField<String> faxNumberField = new TextField<String>("faxNumber",
				BindingModel.of(getModel(), USER_BINDING.faxNumber()));
		faxNumberField.setLabel(new ResourceModel("user.phone"));
		userEditForm.add(faxNumberField);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, EditUserPopupPanel.class);
		
		// Bouton valider
		AjaxButton valider = new AjaxButton("save", userEditForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user = EditUserPopupPanel.this.getModelObject();
				
				try {
					userService.update(user);
					getSession().success(getString("common.success"));
					
					LinkFactory.get().userDescription(EditUserPopupPanel.this.getModel()).setResponsePage();
				} catch (Exception e) {
					LOGGER.error("Error during user update", e);
					getSession().error(getString("common.error"));
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		footer.add(valider);
		
		// Bouton annuler
		AbstractLink annuler = new AbstractLink("cancel"){
			private static final long serialVersionUID = 1L;
		};
		addCancelBehavior(annuler);
		footer.add(annuler);
		
		return footer;
	}

	@Override
	public IModel<String> getCssClassNamesModel() {
		return Model.of("user-form");
	}
}
