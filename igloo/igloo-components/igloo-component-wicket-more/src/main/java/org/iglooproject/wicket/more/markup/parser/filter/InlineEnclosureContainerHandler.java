package org.iglooproject.wicket.more.markup.parser.filter;

import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketParseException;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.util.string.Strings;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.internal.InternalInlineEnclosureContainer;

import com.google.common.base.Joiner;

/**
 * @deprecated Use {@link EnclosureContainer#anyChildVisible()} instead.
 */
@Deprecated
public final class InlineEnclosureContainerHandler extends AbstractMarkupFilter implements IComponentResolver {

	private static final long serialVersionUID = -6887170399988887069L;

	public static final String INLINE_ENCLOSURE_CONTAINER_ID_PREFIX = "InlineEnclosureContainer-";

	public static final String INLINE_ENCLOSURE_CONTAINER_ATTRIBUTE_NAME = "enclosure-container";
	public static final String INLINE_ENCLOSURE_CONTAINER_CHILDREN_ATTRIBUTE_NAME = "enclosure-container-children";

	public static final Joiner CHILDREN_ID_JOINER = Joiner.on(",").skipNulls();

	/*
	 * left: wicket:enclosure-container component tag
	 * middle: current last wicket:enclosure-item component tag
	 * right: computed wicket:enclosure-enclosure-children value for wicket:enclosure-container component tag
	 */
	private Deque<MutableTriple<ComponentTag, ComponentTag, String>> stack;

	public InlineEnclosureContainerHandler() {
		this(null);
	}

	public InlineEnclosureContainerHandler(MarkupResourceStream resourceStream) {
		super(resourceStream);
	}

	@Override
	protected MarkupElement onComponentTag(final ComponentTag tag) throws ParseException {
		String inlineEnclosureContainerAttr = getInlineEnclosureContainerAttribute(tag, null);
		
		// Has wicket:enclosure-container attribute?
		if (inlineEnclosureContainerAttr != null) {
			if (!tag.isOpen()) {
				throw new WicketParseException(
						"Close or open-close tags don't make sense for " + getInlineEnclosureContainerAttributeName(null) + ". Tag:",
						tag);
			}
			
			if (!Strings.isEmpty(tag.getId())) {
				throw new WicketParseException(
						"Found wicket-id on " + getInlineEnclosureContainerAttributeName(null) + ". Tag:",
						tag);
			}
			
			tag.setId(getInlineEnclosureContainerAttribute(tag, null));
			if (Strings.isEmpty(tag.getId())) {
				tag.setId(getWicketNamespace() + "_" + INLINE_ENCLOSURE_CONTAINER_ID_PREFIX +  getRequestUniqueId());
			}
			
			tag.setAutoComponentTag(true);
			tag.setAutoComponentFactory(new ComponentTag.IAutoComponentFactory() {
				@Override
				public Component newComponent(MarkupContainer container, ComponentTag tag) {
					return new InternalInlineEnclosureContainer(tag.getId(), getInlineEnclosureContainerChildrenAttribute(tag, null));
				}
			});
			tag.setModified(true);
		}
		
		// Is a wicket:enclosure-container direct child (among the ones with wicket-id)?
		if (stack != null && !stack.isEmpty() && !tag.isClose() && !Strings.isEmpty(tag.getId())) {
			MutableTriple<ComponentTag, ComponentTag, String> entry = stack.peek();
			
			if (entry.getMiddle() == null) {
				if (tag.isOpen()) {
					entry.setMiddle(tag);
				}
				entry.setRight(CHILDREN_ID_JOINER.join(entry.getRight(), tag.getId()));
			}
		}
		
		if (inlineEnclosureContainerAttr != null) {
			// Put the enclosure on the stack. The most current one will be on top
			if (stack == null) {
				stack = new ArrayDeque<>();
			}
			stack.push(MutableTriple.of(tag, null, ""));
		}
		
		if (stack != null && !stack.isEmpty() && tag.isClose()) {
			if (tag.closes(stack.peek().getMiddle())) {
				stack.peek().setMiddle(null);
			} else if (tag.closes(stack.peek().getLeft())) {
				// Remove the open tag from the stack
				MutableTriple<ComponentTag, ComponentTag, String> entry = stack.pop();
				ComponentTag lastEnclosureContainer = entry.getLeft();
				String childrenId = entry.getRight();
				
				lastEnclosureContainer.put(getInlineEnclosureContainerChildrenAttributeName(null), childrenId);
				lastEnclosureContainer.setModified(true);
				
				if (stack.size() == 0) {
					stack = null;
				}
			}
		}
		
		return tag;
	}

	private String getInlineEnclosureContainerAttribute(final ComponentTag tag, MarkupStream markupStream) {
		return tag.getAttributes().getString(getInlineEnclosureContainerAttributeName(markupStream));
	}

	private String getInlineEnclosureContainerChildrenAttribute(final ComponentTag tag, MarkupStream markupStream) {
		return tag.getAttributes().getString(getInlineEnclosureContainerChildrenAttributeName(markupStream));
	}

	private String getInlineEnclosureContainerAttributeName(MarkupStream markupStream) {
		return getWicketNamespace(markupStream) + ':' + INLINE_ENCLOSURE_CONTAINER_ATTRIBUTE_NAME;
	}

	private String getInlineEnclosureContainerChildrenAttributeName(MarkupStream markupStream) {
		return getWicketNamespace(markupStream) + ':' + INLINE_ENCLOSURE_CONTAINER_CHILDREN_ATTRIBUTE_NAME;
	}

	@Override
	public Component resolve(MarkupContainer container, MarkupStream markupStream, ComponentTag tag) {
		String inlineEnclosureComponentChildrenId = getInlineEnclosureContainerChildrenAttribute(tag, markupStream);
		
		if (!Strings.isEmpty(inlineEnclosureComponentChildrenId)) {
			return new InternalInlineEnclosureContainer(tag.getId(), inlineEnclosureComponentChildrenId);
		}
		
		return null;
	}

}
