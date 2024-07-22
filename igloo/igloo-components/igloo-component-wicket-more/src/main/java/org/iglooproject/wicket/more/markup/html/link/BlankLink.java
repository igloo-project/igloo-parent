package org.iglooproject.wicket.more.markup.html.link;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;

/** A link pointing to nothing in particular. Useful as a basis to add Javascript behaviors. */
public class BlankLink extends AbstractLink {

  private static final long serialVersionUID = 7324003053376463554L;

  public BlankLink(String id) {
    super(id);
  }

  @Override
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);

    if (isEnabledInHierarchy()) {
      if (tag.getName().equalsIgnoreCase("a")) {
        tag.put("href", "#");
      }
    } else {
      disableLink(tag);
    }
  }
}
