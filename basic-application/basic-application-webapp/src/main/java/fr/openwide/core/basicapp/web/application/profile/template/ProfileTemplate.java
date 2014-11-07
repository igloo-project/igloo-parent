package fr.openwide.core.basicapp.web.application.profile.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.common.template.MainTemplate;
import fr.openwide.core.basicapp.web.application.profile.page.ProfilePage;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class ProfileTemplate extends MainTemplate {

	private static final long serialVersionUID = 1029271113953538262L;

	public ProfileTemplate(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.profile")));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return ProfilePage.class;
	}

}
