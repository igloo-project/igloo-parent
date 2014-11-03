package fr.openwide.core.basicapp.web.application.navigation.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.common.template.ServiceTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class MaintenancePage extends ServiceTemplate {

	private static final long serialVersionUID = 7371109597310862894L;

	public MaintenancePage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("maintenance.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("maintenance.help.title");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new DelegatedMarkupPanel(wicketId, "contentFragment", getClass());
	}

	@Override
	protected boolean maintenanceRestriction() {
		return false;
	}

}
