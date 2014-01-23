package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.showcase.web.application.widgets.component.CalendarPanel;
import fr.openwide.core.showcase.web.application.widgets.component.CalendarPopupPanel;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class CalendarPage extends WidgetsTemplate {
	private static final long serialVersionUID = -3963117430192776716L;
	
	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(CalendarPage.class)
				.build();
	}
	
	public CalendarPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.calendar"), CalendarPage.linkDescriptor()));
		
		add(new CalendarPanel("calendarPanel"));
		
		CalendarPopupPanel calendarPopupPanel = new CalendarPopupPanel("calendarPopupPanel");
		add(calendarPopupPanel);
		AbstractLink calendarPopupButton = new AbstractLink("calendarPopupButton") {
			private static final long serialVersionUID = 1L;
		};
		calendarPopupButton.add(new AjaxModalOpenBehavior(calendarPopupPanel, MouseEvent.CLICK));
		add(calendarPopupButton);
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return CalendarPage.class;
	}
}
