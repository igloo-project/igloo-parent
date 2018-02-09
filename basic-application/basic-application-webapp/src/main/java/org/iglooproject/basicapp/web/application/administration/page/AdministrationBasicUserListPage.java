package org.iglooproject.basicapp.web.application.administration.page;

import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.web.application.administration.form.AbstractUserPopup;
import org.iglooproject.basicapp.web.application.administration.form.UserPopup;
import org.iglooproject.basicapp.web.application.administration.model.AbstractUserDataProvider;
import org.iglooproject.basicapp.web.application.administration.model.BasicUserDataProvider;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserListTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationBasicUserListPage extends AdministrationUserListTemplate<BasicUser> {

	private static final long serialVersionUID = 2645014338149595776L;

	public AdministrationBasicUserListPage(PageParameters parameters) {
		super(parameters, UserTypeDescriptor.BASIC_USER, new ResourceModel("administration.user.basic.list.title"));
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user.basic")));
	}
	
	@Override
	protected AbstractUserDataProvider<BasicUser> newDataProvider() {
		return new BasicUserDataProvider();
	}

	@Override
	protected AbstractUserPopup<BasicUser> createAddPopup(String wicketId) {
		return new UserPopup<>(wicketId, typeDescriptor);
	}

}
