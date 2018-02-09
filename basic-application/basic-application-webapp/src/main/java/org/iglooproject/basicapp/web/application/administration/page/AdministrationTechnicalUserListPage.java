package org.iglooproject.basicapp.web.application.administration.page;

import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.web.application.administration.form.AbstractUserPopup;
import org.iglooproject.basicapp.web.application.administration.form.UserPopup;
import org.iglooproject.basicapp.web.application.administration.model.AbstractUserDataProvider;
import org.iglooproject.basicapp.web.application.administration.model.TechnicalUserDataProvider;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserListTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationTechnicalUserListPage extends AdministrationUserListTemplate<TechnicalUser> {

	private static final long serialVersionUID = -2263140539100206080L;
	
	public AdministrationTechnicalUserListPage(PageParameters parameters) {
		super(parameters, UserTypeDescriptor.TECHNICAL_USER, new ResourceModel("administration.user.technical.list.title"));
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user.technical")));
	}
	
	@Override
	protected AbstractUserDataProvider<TechnicalUser> newDataProvider() {
		return new TechnicalUserDataProvider();
	}

	@Override
	protected AbstractUserPopup<TechnicalUser> createAddPopup(String wicketId) {
		return new UserPopup<>(wicketId, typeDescriptor);
	}

}
