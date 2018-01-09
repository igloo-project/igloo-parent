package org.iglooproject.basicapp.web.application.administration.page;

import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.component.UserHistoryLogPanel;
import org.iglooproject.basicapp.web.application.administration.component.UserMembershipsPanel;
import org.iglooproject.basicapp.web.application.administration.component.UserProfilePanel;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.BindingModel;

public class AdministrationTechnicalUserDescriptionPage extends AdministrationUserDescriptionTemplate<TechnicalUser> {

	private static final long serialVersionUID = -550100874222819991L;

	public AdministrationTechnicalUserDescriptionPage(PageParameters parameters) {
		super(parameters, UserTypeDescriptor.TECHNICAL_USER);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user.technical"), typeDescriptor.administrationTypeDescriptor().portfolio()));
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));
		
		add(
				new UserProfilePanel<>("profile", userModel, typeDescriptor),
				new UserMembershipsPanel("groups", userModel),
				new UserHistoryLogPanel("audits", userModel)
		);
	}
}
