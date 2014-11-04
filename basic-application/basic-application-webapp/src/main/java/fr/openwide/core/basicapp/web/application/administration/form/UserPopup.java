package fr.openwide.core.basicapp.web.application.administration.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;

public class UserPopup<U extends User> extends AbstractUserPopup<U> {

	private static final long serialVersionUID = 5977011506936352812L;

	public UserPopup(String id, IModel<U> userModel, UserTypeDescriptor<U> typeDescriptor) {
		super(id, userModel, typeDescriptor);
	}
	
	public UserPopup(String id, UserTypeDescriptor<U> typeDescriptor) {
		super(id, typeDescriptor);
	}
	
	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		
		userForm = new Form<Void>("form");
		body.add(userForm);
		
		userForm.add(createStandardUserFields("fields"));
		
		return body;
	}
}
