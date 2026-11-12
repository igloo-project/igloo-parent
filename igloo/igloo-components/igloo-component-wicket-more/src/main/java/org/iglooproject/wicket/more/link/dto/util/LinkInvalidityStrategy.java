package org.iglooproject.wicket.more.link.dto.util;

import org.apache.wicket.markup.html.link.Link;

public enum LinkInvalidityStrategy {
  // Same as legacy AbstractDynamicBookmarkableLink: each strategy only drives the flag it is
  // responsible for, so that visibilityAllowed / enabled set by the caller or by a behavior are
  // not overridden.
  HIDE {
    @Override
    public <L extends Link<?>> void onConfigure(L link, boolean accessible) {
      link.setVisible(accessible);
    }
  },
  DISABLE {
    @Override
    public <L extends Link<?>> void onConfigure(L link, boolean accessible) {
      link.setEnabled(accessible);
    }
  },
  THROW_EXCEPTION {
    @Override
    public <L extends Link<?>> void onConfigure(L link, boolean accessible) {
      // If invalid, the exception will be thrown when rendering the URL
      link.setVisible(true);
    }
  };

  public abstract <L extends Link<?>> void onConfigure(L link, boolean accessible);
}
