package org.iglooproject.wicket.more.markup.html.internal;

import org.apache.wicket.markup.html.internal.Enclosure;

public class InternalInlineEnclosureComponent extends Enclosure {

  private static final long serialVersionUID = 1L;

  public InternalInlineEnclosureComponent(String id, String childId) {
    super(id, childId);

    setOutputMarkupId(true);
  }
}
