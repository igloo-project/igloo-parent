package org.iglooproject.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import org.iglooproject.showcase.web.application.widgets.component.CalendarPanel;
import org.iglooproject.showcase.web.application.widgets.component.CalendarPopupPanel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class CalendarPage extends WidgetsTemplate {

	private static final long serialVersionUID = -3963117430192776716L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(CalendarPage.class);
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
