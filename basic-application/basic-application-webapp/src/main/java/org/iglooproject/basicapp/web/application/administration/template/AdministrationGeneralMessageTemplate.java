package org.iglooproject.basicapp.web.application.administration.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationGeneralMessageListPage;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationGeneralMessageTemplate extends AdministrationTemplate {

	private static final long serialVersionUID = 1L;

	public AdministrationGeneralMessageTemplate(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("navigation.administration.generalMessage"),
			AdministrationGeneralMessageListPage.linkDescriptor()
		));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return getClass();
	}

}
