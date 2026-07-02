package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.markup.html.link.Link;

public interface IBookmarkablePageLink {
  Link<Void> hideIfInvalid();
}
