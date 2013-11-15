package fr.openwide.core.wicket.more.notification.markup.parser;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.ContainerInfo;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.IMarkupFilter;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

public class MarkupFactoryWithHtmlNotificationSupport extends MarkupFactory {
	
	@SpringBean
	private IHtmlNotificationCssService cssService;

	public MarkupFactoryWithHtmlNotificationSupport() {
		Injector.get().inject(this);
	}

	@Override
	public MarkupParser newMarkupParser(final MarkupResourceStream resource) {
		ContainerInfo containerInfo = resource.getContainerInfo();
		if (containerInfo != null) {
			IHtmlNotificationCssService.IHtmlNotificationCssRegistry registry = cssService.getRegistry(containerInfo.getVariation());
			if (registry != null) {
				return newMarkupParserWithHtmlNotificationSupport(resource, registry);
			}
		}
		
		return super.newMarkupParser(resource);
	}
	
	private MarkupParser newMarkupParserWithHtmlNotificationSupport(
			final MarkupResourceStream resource,
			final IHtmlNotificationCssService.IHtmlNotificationCssRegistry registry
			) {
		return new MarkupParser(newXmlPullParser(), resource) {
			@Override
			protected IMarkupFilter onAppendMarkupFilter(final IMarkupFilter filter) {
				return MarkupFactoryWithHtmlNotificationSupport.this.onAppendMarkupFilter(filter);
			}
			@Override
			protected MarkupFilterList initializeMarkupFilters(Markup markup) {
				MarkupFilterList list = super.initializeMarkupFilters(markup);
				list.add(new NotificationCssClassMarkupFilter(resource, registry));
				list.add(new NotificationLinksBlankTargetMarkupFilter(resource));
				return list;
			}
		};
	}
}
