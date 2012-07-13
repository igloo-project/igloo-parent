package fr.openwide.core.basicapp.web.application.administration.template;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupPortfolioPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.common.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public abstract class AdministrationTemplate extends MainTemplate {

	private static final long serialVersionUID = -5571981353426833725L;

	public AdministrationTemplate(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));
	}

	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList(
				new NavigationMenuItem(new ResourceModel("navigation.administration.user"),
				AdministrationUserPortfolioPage.class),
				new NavigationMenuItem(new ResourceModel("navigation.administration.usergroup"),
				AdministrationUserGroupPortfolioPage.class));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return AdministrationUserPortfolioPage.class;
	}

	@Override
	protected abstract Class<? extends WebPage> getSecondMenuPage();
}
