package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.web.application.administration.component.AbstractUserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopupPanel;
import fr.openwide.core.basicapp.web.application.administration.form.UserPopupPanel;
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
	protected AbstractUserPopupPanel<BasicUser> createAddPopupPanel(String wicketId) {
		return new UserPopupPanel<>(wicketId, typeDescriptor);
	}
	
	@Override
	protected AbstractUserPortfolioPanel<BasicUser> createPortfolioPanel(String wicketId,
			IDataProvider<BasicUser> dataProvider, int itemsPerPage) {
		return new UserPortfolioPanel<>(wicketId, dataProvider, typeDescriptor, itemsPerPage);
	}

}
