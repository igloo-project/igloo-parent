package fr.openwide.core.wicket.more.notification.markup.parser;

import java.text.ParseException;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;

public class NotificationLinksBlankTargetMarkupFilter extends AbstractMarkupFilter {
	
	private static final String LINK_TAG = "a";
	private static final String LINK_TARGET_ATTRIBUTE = "target";
	private static final String LINK_TARGET_ATTRIBUTE_BLANK_VALUE = "_blank";

	public NotificationLinksBlankTargetMarkupFilter(MarkupResourceStream markupResourceStream) {
		super(markupResourceStream);
	}

	@Override
	protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {
		if ((tag.isOpen() || tag.isOpenClose()) && LINK_TAG.equalsIgnoreCase(tag.getName())) {
			tag.put(LINK_TARGET_ATTRIBUTE, LINK_TARGET_ATTRIBUTE_BLANK_VALUE);
		}
		
		return tag;
	}

}
