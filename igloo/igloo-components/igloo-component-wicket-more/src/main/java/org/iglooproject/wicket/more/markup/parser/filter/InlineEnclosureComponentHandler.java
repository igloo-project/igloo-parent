package org.iglooproject.wicket.more.markup.parser.filter;

import java.text.ParseException;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketParseException;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.util.string.Strings;
import org.iglooproject.wicket.more.markup.html.internal.InternalInlineEnclosureComponent;

public final class InlineEnclosureComponentHandler extends AbstractMarkupFilter
    implements IComponentResolver {

  private static final long serialVersionUID = 7296604782230893979L;

  public static final String INLINE_ENCLOSURE_COMPONENT_ID_PREFIX = "InlineEnclosureComponent-";

  public static final String INLINE_ENCLOSURE_COMPONENT_ATTRIBUTE_NAME = "enclosure-component";

  public InlineEnclosureComponentHandler() {
    this(null);
  }

  public InlineEnclosureComponentHandler(MarkupResourceStream resourceStream) {
    super(resourceStream);
  }

  @Override
  protected MarkupElement onComponentTag(final ComponentTag tag) throws ParseException {
    // We only need ComponentTags
    if (tag instanceof WicketTag) {
      return tag;
    }

    // Has wicket:enclosure-component attribute?
    String enclosureAttr = getAttribute(tag, null);
    if (enclosureAttr != null) {
      if (!tag.isOpen()) {
        throw new WicketParseException(
            "Close or open-close tags don't make sense for "
                + getInlineEnclosureComponentAttributeName(null)
                + ". Tag:",
            tag);
      }

      // if it doesn't have a wicket-id already, then assign one now.
      if (Strings.isEmpty(tag.getId())) {
        String htmlId = tag.getAttribute("id");

        if (Strings.isEmpty(htmlId)) {
          String id =
              getWicketNamespace()
                  + "_"
                  + INLINE_ENCLOSURE_COMPONENT_ID_PREFIX
                  + getRequestUniqueId();
          tag.setId(id);
        } else {
          tag.setId(htmlId);
        }

        tag.setAutoComponentTag(true);
        tag.setAutoComponentFactory(
            new ComponentTag.IAutoComponentFactory() {
              @Override
              public Component newComponent(MarkupContainer container, ComponentTag tag) {
                return new InternalInlineEnclosureComponent(tag.getId(), getAttribute(tag, null));
              }
            });
        tag.setModified(true);
      }
    }

    return tag;
  }

  private String getAttribute(final ComponentTag tag, MarkupStream markupStream) {
    return tag.getAttributes().getString(getInlineEnclosureComponentAttributeName(markupStream));
  }

  private String getInlineEnclosureComponentAttributeName(MarkupStream markupStream) {
    return getWicketNamespace(markupStream) + ':' + INLINE_ENCLOSURE_COMPONENT_ATTRIBUTE_NAME;
  }

  @Override
  public Component resolve(
      final MarkupContainer container, final MarkupStream markupStream, final ComponentTag tag) {
    String inlineEnclosureComponentChildId = getAttribute(tag, markupStream);

    if (!Strings.isEmpty(inlineEnclosureComponentChildId)) {
      return new InternalInlineEnclosureComponent(tag.getId(), inlineEnclosureComponentChildId);
    }

    return null;
  }
}
