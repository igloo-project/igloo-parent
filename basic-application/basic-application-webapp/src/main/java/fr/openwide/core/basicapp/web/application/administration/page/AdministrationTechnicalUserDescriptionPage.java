package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.component.UserMembershipsPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserProfilePanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationUserTypeDescriptor;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class AdministrationTechnicalUserDescriptionPage extends AdministrationUserDescriptionTemplate<TechnicalUser> {

	private static final long serialVersionUID = -550100874222819991L;

	public AdministrationTechnicalUserDescriptionPage(PageParameters parameters) {
		super(parameters, AdministrationUserTypeDescriptor.TECHNICAL_USER);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user.technical"), type.liste()));
		
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));

		add(new UserProfilePanel<>("profile", userModel, type));
		IModel<User> abstractUserModel = ReadOnlyModel.<User>of(userModel);
		add(new UserMembershipsPanel("groups", abstractUserModel));
	}
}
