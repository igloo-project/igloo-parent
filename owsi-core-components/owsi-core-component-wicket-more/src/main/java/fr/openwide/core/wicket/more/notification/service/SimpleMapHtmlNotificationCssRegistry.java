package fr.openwide.core.wicket.more.notification.service;

import java.util.Map;

import org.apache.wicket.markup.ComponentTag;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;

public class SimpleMapHtmlNotificationCssRegistry implements IHtmlNotificationCssRegistry {
	
	private static final Splitter CSS_CLASSES_SPLITTER = Splitter.on(CharMatcher.WHITESPACE);

	private final Map<String, String> tagNameStyles;
	private final Map<String, String> cssClassStyles;
	
	public SimpleMapHtmlNotificationCssRegistry(Map<String, String> tagNameStyles, Map<String, String> cssClassStyles) {
		super();
		this.tagNameStyles = ImmutableMap.copyOf(tagNameStyles);
		this.cssClassStyles = ImmutableMap.copyOf(cssClassStyles);
	}

	@Override
	public String getStyle(ComponentTag tag) {
		StringBuilder builder = new StringBuilder();
		
		String tagNameStyle = tagNameStyles.get(tag.getName());
		if (tagNameStyle != null) {
			builder.append(tagNameStyle);
		}
		
		String cssClassesString = tag.getAttribute("class");
		if (cssClassesString != null) {
			for (String cssClass : CSS_CLASSES_SPLITTER.split(cssClassesString)) {
				String cssClassStyle = cssClassStyles.get(cssClass);
				if (cssClassStyle != null) {
					builder.append(cssClassStyle);
				}
			}
		}
		
		return builder.toString();
	}

}
