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
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.root"), WidgetsMainPage.linkDescriptor()));
	}
	
	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList(
				CalendarPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.calendar")),
				AutocompletePage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.autocomplete")),
				ModalPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.modal")),
				ListFilterPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.listFilter")),
				BootstrapJsPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.bootstrapJs")),
				CarouselPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.carousel")),
				StatisticsPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.statistics")),
				AutosizePage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.autosize")),
				SortableListPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.sortable")),
				SelectBoxPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.selectbox"))
		);
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
	}

}
