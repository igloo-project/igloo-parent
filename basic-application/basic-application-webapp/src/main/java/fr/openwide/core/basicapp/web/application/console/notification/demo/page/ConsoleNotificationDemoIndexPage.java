package fr.openwide.core.basicapp.web.application.console.notification.demo.page;

import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import fr.openwide.core.basicapp.core.business.notification.service.INotificationService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.console.notification.demo.template.ConsoleNotificationDemoTemplate;
import fr.openwide.core.basicapp.web.application.console.notification.demo.util.NotificationDemoEntry;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;

public class ConsoleNotificationDemoIndexPage extends ConsoleNotificationDemoTemplate {

	private static final long serialVersionUID = -6767518941118385548L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleNotificationDemoIndexPage.class);

	public static final String DEFAULT_USERNAME = "admin";

	private static final Range<Long> DEFAULT_ID_RANGE = Range.closed(1L, 100L);

	@SpringBean
	private INotificationService notificationService;

	public ConsoleNotificationDemoIndexPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.notifications");
		
		add(new Link<Void>("sendExample") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				try {
					notificationService.sendExampleNotification(BasicApplicationSession.get().getUser());
					getSession().success(getString("console.notifications.example.send.success"));
				} catch (ServiceException e) {
					LOGGER.error("Error while sending example notification", e);
					getSession().error(getString("common.error.unexpected"));
				}
			}
		});
		
		add(new ListView<NotificationDemoEntry>("notifications", createDemoEntries()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(final ListItem<NotificationDemoEntry> item) {
				Link<Void> link = new Link<Void>("link") {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new NotificationDemoPage(item.getModelObject().getDescriptor()));
					}
				};
				link.add(new Label("label", item.getModelObject().getLabelModel()));
				item.add(link);
			}
		});
		
		add(new WebMarkupContainer("emptyList") {
			private static final long serialVersionUID = 6700720373087584498L;
			@Override
			public boolean isVisible() {
				return createDemoEntries().isEmpty();
			}
		});
	}

	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder().page(ConsoleNotificationDemoIndexPage.class).build();
	}

	private List<NotificationDemoEntry> createDemoEntries() {
		return Lists.<NotificationDemoEntry>newArrayList(
				new NotificationDemoEntry("example") {
					private static final long serialVersionUID = 1L;
					@Override
					public IWicketNotificationDescriptor getDescriptor() {
						return descriptorService.example(getFirstInRange(User.class, DEFAULT_ID_RANGE), new Date());
					}
				}
				// Add new demo entries here
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
