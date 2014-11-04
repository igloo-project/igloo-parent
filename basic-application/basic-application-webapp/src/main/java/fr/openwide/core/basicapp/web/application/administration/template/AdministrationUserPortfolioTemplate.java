package fr.openwide.core.basicapp.web.application.administration.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.administration.component.AbstractUserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserSearchPanel;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopup;
import fr.openwide.core.basicapp.web.application.administration.model.UserDataProvider;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationUserTypeDescriptor;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;

public abstract class AdministrationUserPortfolioTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = 1824247169136460059L;

	@SpringBean
	private BasicApplicationConfigurer configurer;
	
	protected AdministrationUserTypeDescriptor<U> typeDescriptor;
	
	public AdministrationUserPortfolioTemplate(PageParameters parameters, AdministrationUserTypeDescriptor<U> typeDescriptor, IModel<String> pageTitleModel) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		AbstractUserPopup<U> addPopup = createAddPopup("addPopup");
		
		UserDataProvider<U> dataProvider = new UserDataProvider<>(typeDescriptor.getEntityClass());
		AbstractUserPortfolioPanel<U> portfolioPanel = createPortfolioPanel("portfolio", dataProvider, configurer.getPortfolioItemsPerPage());
		
		add(
				new Label("title", pageTitleModel),
				
				new UserSearchPanel<>("searchPanel", portfolioPanel.getPageable(), typeDescriptor, dataProvider),
				
				portfolioPanel,
				
				addPopup,
				new BlankLink("addButton")
						.add(
								new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK)
						)
		);
	}
	
	protected abstract AbstractUserPopup<U> createAddPopup(String wicketId);
	
	protected abstract AbstractUserPortfolioPanel<U> createPortfolioPanel(String wicketId, IDataProvider<U> dataProvider, int itemsPerPage);

	@SuppressWarnings("rawtypes")
	@Override
	protected Class<? extends AdministrationUserPortfolioTemplate> getSecondMenuPage() {
		return getClass();
	}
}
