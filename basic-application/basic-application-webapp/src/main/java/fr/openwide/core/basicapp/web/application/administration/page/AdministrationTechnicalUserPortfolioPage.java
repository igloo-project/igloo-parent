package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.web.application.administration.component.AbstractUserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopup;
import fr.openwide.core.basicapp.web.application.administration.form.UserPopup;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserPortfolioTemplate;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationUserTypeDescriptor;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationTechnicalUserPortfolioPage extends AdministrationUserPortfolioTemplate<TechnicalUser> {

	private static final long serialVersionUID = -2263140539100206080L;
	
	public AdministrationTechnicalUserPortfolioPage(PageParameters parameters) {
		super(parameters, AdministrationUserTypeDescriptor.TECHNICAL_USER, new ResourceModel("administration.user.technical.title"));
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user.technical")));
	}

	@Override
	protected AbstractUserPopup<TechnicalUser> createAddPopup(String wicketId) {
		return new UserPopup<>(wicketId, typeDescriptor);
	}

	@Override
	protected AbstractUserPortfolioPanel<TechnicalUser> createPortfolioPanel(String wicketId,
			IDataProvider<TechnicalUser> dataProvider, int itemsPerPage) {
		return new UserPortfolioPanel<>(wicketId, dataProvider, typeDescriptor, itemsPerPage);
	}

}
