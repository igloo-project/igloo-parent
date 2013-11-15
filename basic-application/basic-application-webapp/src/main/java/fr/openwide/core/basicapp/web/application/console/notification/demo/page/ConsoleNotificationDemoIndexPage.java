package fr.openwide.core.basicapp.web.application.console.notification.demo.page;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.web.application.console.notification.demo.template.ConsoleNotificationDemoTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ConsoleNotificationDemoIndexPage extends ConsoleNotificationDemoTemplate {

	private static final long serialVersionUID = -6767518941118385548L;
	
	public static final String DEFAULT_USERNAME = "admin";
	
	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder().page(ConsoleNotificationDemoIndexPage.class).build();
	}
	
	public ConsoleNotificationDemoIndexPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.notifications");
		
		add(new ListView<PageProvider>("notifications", getNotificationPages()) {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(ListItem<PageProvider> item) {
				Class<? extends Page> pageClass = (Class<? extends Page>) item.getModelObject().getPageClass();
				Link<Void> link = new BookmarkablePageLink<Void>("link", pageClass);
				link.add(new Label("label", new ResourceModel("console.notifications." + pageClass.getSimpleName(), pageClass.getSimpleName())));
				item.add(link);
			}
		});
		
		add(new WebMarkupContainer("emptyList") {
			private static final long serialVersionUID = 6700720373087584498L;
			@Override
			public boolean isVisible() {
				return getNotificationPages().isEmpty();
			}
		});
	}
	
	private List<PageProvider> getNotificationPages() {
		return Lists.newArrayList(
				new PageProvider(ExampleHtmlNotificationDemoPage.class)
		);
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuSectionPageClass() {
		return ConsoleNotificationDemoIndexPage.class;
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleNotificationDemoIndexPage.class;
	}
}
