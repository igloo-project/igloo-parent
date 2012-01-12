package fr.openwide.core.showcase.web.application.portfolio.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.portfolio.page.UserDescriptionPage;
import fr.openwide.core.showcase.web.application.util.LinkUtils;
import fr.openwide.core.wicket.markup.html.form.FormComponentHelper;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox.AbstractGenericPopupFormPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox.FormPanelMode;
import fr.openwide.core.wicket.more.model.BindingModel;

public class UserEditForm extends AbstractGenericPopupFormPanel<User> {
	private static final long serialVersionUID = 3066059572097078436L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserEditForm.class);
	
	private static final UserBinding USER_BINDING = new UserBinding();
	
	@SpringBean
	private IUserService userService;
	
	public UserEditForm(String id, IModel<User> userModel) {
		super(id, userModel, FormPanelMode.EDIT);
		
		TextField<String> userNameField = new RequiredTextField<String>("userName",
				BindingModel.of(userModel, USER_BINDING.userName()));
		FormComponentHelper.setLabel(userNameField, new ResourceModel("user.username"));
		itemForm.add(userNameField);
		
		TextField<String> firstNameField = new RequiredTextField<String>("firstName",
				BindingModel.of(userModel, USER_BINDING.firstName()));
		FormComponentHelper.setLabel(firstNameField, new ResourceModel("user.firstname"));
		itemForm.add(firstNameField);
		
		TextField<String> lastNameField = new RequiredTextField<String>("lastName",
				BindingModel.of(userModel, USER_BINDING.lastName()));
		FormComponentHelper.setLabel(lastNameField, new ResourceModel("user.lastname"));
		itemForm.add(lastNameField);
		
		TextField<String> emailField = new RequiredTextField<String>("email",
				BindingModel.of(userModel, USER_BINDING.email()));
		FormComponentHelper.setLabel(emailField, new ResourceModel("user.email"));
		itemForm.add(emailField);
		
		CheckBox activeCheckBox = new CheckBox("active", BindingModel.of(userModel, USER_BINDING.active()));
		FormComponentHelper.setLabel(activeCheckBox, new ResourceModel("user.active"));
		itemForm.add(activeCheckBox);
		
		TextField<String> phoneNumberField = new RequiredTextField<String>("phoneNumber",
				BindingModel.of(userModel, USER_BINDING.phoneNumber()));
		FormComponentHelper.setLabel(phoneNumberField, new ResourceModel("user.phone"));
		itemForm.add(phoneNumberField);
		
		TextField<String> gsmNumberField = new TextField<String>("gsmNumber",
				BindingModel.of(userModel, USER_BINDING.gsmNumber()));
		FormComponentHelper.setLabel(gsmNumberField, new ResourceModel("user.phone"));
		itemForm.add(gsmNumberField);
		
		TextField<String> faxNumberField = new TextField<String>("faxNumber",
				BindingModel.of(userModel, USER_BINDING.faxNumber()));
		FormComponentHelper.setLabel(faxNumberField, new ResourceModel("user.phone"));
		itemForm.add(faxNumberField);
	}
	
	@Override
	protected User getEmptyModelObject() {
		return new User();
	}
	
	@Override
	protected void onSubmitInternal(AjaxRequestTarget target, Form<?> form) {
		User user = (User) form.getModelObject();
		
		try {
			userService.update(user);
			getSession().info(getString("common.success"));
			setResponsePage(UserDescriptionPage.class, LinkUtils.getUserDescriptionPageParameters(user));
		} catch (Exception e) {
			LOGGER.error("Error during user update", e);
			getSession().error(getString("common.error"));
			reloadPanel(target);
		}
	}
}
