package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.component.UserAuditsPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserMembershipsPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserProfilePanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationUserTypeDescriptor;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class AdministrationBasicUserDescriptionPage extends AdministrationUserDescriptionTemplate<BasicUser> {

	private static final long serialVersionUID = 1198343613141377913L;

	public AdministrationBasicUserDescriptionPage(PageParameters parameters) {
		super(parameters, AdministrationUserTypeDescriptor.BASIC_USER);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user.basic"), typeDescriptor.liste()));
		
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));

		add(new UserProfilePanel<>("profile", userModel, typeDescriptor));
		IModel<User> abstractUserModel = ReadOnlyModel.<User>of(userModel);
		add(new UserMembershipsPanel("groups", abstractUserModel));
		add(new UserAuditsPanel("audits", userModel));
	}
}
