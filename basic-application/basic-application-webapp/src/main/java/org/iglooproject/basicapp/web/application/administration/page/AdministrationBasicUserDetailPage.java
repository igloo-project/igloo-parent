package org.iglooproject.basicapp.web.application.administration.page;

import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.component.UserDetailDescriptionPanel;
import org.iglooproject.basicapp.web.application.administration.component.UserDetailGroupsPanel;
import org.iglooproject.basicapp.web.application.administration.component.UserDetailHistoryLogPanel;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.BindingModel;

public class AdministrationBasicUserDetailPage extends AdministrationUserDetailTemplate<BasicUser> {

	private static final long serialVersionUID = 1198343613141377913L;

	public AdministrationBasicUserDetailPage(PageParameters parameters) {
		super(parameters, UserTypeDescriptor.BASIC_USER);
		
		addBreadCrumbElement(new BreadCrumbElement(
				new ResourceModel("navigation.administration.user.basic"),
				typeDescriptor.administrationTypeDescriptor().list()
		));
		addBreadCrumbElement(new BreadCrumbElement(
				BindingModel.of(userModel, Bindings.user().fullName())
		));
		
		add(
				new UserDetailDescriptionPanel<>("description", userModel, typeDescriptor),
				new UserDetailGroupsPanel("groups", userModel),
				new UserDetailHistoryLogPanel("audits", userModel)
		);
	}
}
