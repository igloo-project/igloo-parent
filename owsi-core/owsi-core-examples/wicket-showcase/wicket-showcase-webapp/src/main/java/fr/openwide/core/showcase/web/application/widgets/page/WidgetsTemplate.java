package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public abstract class WidgetsTemplate extends MainTemplate {

	private static final long serialVersionUID = 3206542664239463727L;
	
	public WidgetsTemplate(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.root"), WidgetsMainPage.class));
	}
	
	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList(
				new NavigationMenuItem(new ResourceModel("widgets.menu.calendar"), CalendarPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.autocomplete"), AutocompletePage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.fancybox"), FancyboxPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.dateParser"), DateParserPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.listFilter"), ListFilterPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.tooltip"), TooltipPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.carousel"), CarouselPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.statistics"), StatisticsPage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.autosize"), AutosizePage.class),
				new NavigationMenuItem(new ResourceModel("widgets.menu.sortable"), SortableListPage.class)
		);
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
	}

}
