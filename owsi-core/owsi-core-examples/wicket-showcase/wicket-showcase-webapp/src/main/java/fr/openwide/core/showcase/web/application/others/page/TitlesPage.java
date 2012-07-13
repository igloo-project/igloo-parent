package fr.openwide.core.showcase.web.application.others.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public class TitlesPage extends MainTemplate {

	private static final long serialVersionUID = 182545144170932682L;

	public TitlesPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList();
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return TitlesPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}

}
