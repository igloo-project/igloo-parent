package fr.openwide.core.wicket.more.notification.markup.parser;

import java.text.ParseException;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.springframework.util.StringUtils;

import fr.openwide.core.wicket.more.notification.listener.HtmlNotificationComponentCssClassHandler;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

/**
 * Append css style to tags that will not be attributed to a wicket component according to a given {@link IHtmlNotificationCssService.IHtmlNotificationCssRegistry CSS registry}.
 * @see HtmlNotificationComponentCssClassHandler how the same task is performed on wicket components
 */
public class NotificationCssClassMarkupFilter extends AbstractMarkupFilter {
	
	private static final String CSS_STYLE_ATTRIBUTE = "style";
	
	private final IHtmlNotificationCssService.IHtmlNotificationCssRegistry cssRegistry;

	public NotificationCssClassMarkupFilter(MarkupResourceStream markupResourceStream, IHtmlNotificationCssService.IHtmlNotificationCssRegistry cssRegistry) {
		super(markupResourceStream);
		this.cssRegistry = cssRegistry;
	}

	@Override
	protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {
		if (tag.isOpen() || tag.isOpenClose()) {
			final String wicketIdValue = tag.getAttributes().getString(getWicketNamespace() + ":id");
			if (!StringUtils.hasText(wicketIdValue)) {
				// This tag will not be attributed to a wicket component. Its class attribute won't change, we may append style right now.
				doAppendStyle(tag);
			}
		}
		
		return tag;
	}

	protected void doAppendStyle(ComponentTag tag) {
		String appendedStyle = cssRegistry.getStyle(tag);
		if (StringUtils.hasText(appendedStyle)) {
			tag.append(CSS_STYLE_ATTRIBUTE, appendedStyle, "");
		}
	}

}
