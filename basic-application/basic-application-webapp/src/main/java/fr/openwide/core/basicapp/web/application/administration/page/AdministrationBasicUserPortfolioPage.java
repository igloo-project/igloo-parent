package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopup;
import fr.openwide.core.basicapp.web.application.administration.form.UserPopup;
import fr.openwide.core.basicapp.web.application.administration.model.AbstractUserDataProvider;
import fr.openwide.core.basicapp.web.application.administration.model.BasicUserDataProvider;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserPortfolioTemplate;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationBasicUserPortfolioPage extends AdministrationUserPortfolioTemplate<BasicUser> {

	private static final long serialVersionUID = 2645014338149595776L;

	public AdministrationBasicUserPortfolioPage(PageParameters parameters) {
		super(parameters, UserTypeDescriptor.BASIC_USER, new ResourceModel("administration.user.basic.title"));
		
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
