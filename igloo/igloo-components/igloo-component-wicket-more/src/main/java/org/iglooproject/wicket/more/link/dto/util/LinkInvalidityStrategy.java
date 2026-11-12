package org.iglooproject.wicket.more.link.dto.util;

import org.apache.wicket.markup.html.link.Link;

public enum LinkInvalidityStrategy {
  HIDE {
    @Override
    public <L extends Link<?>> void onConfigure(L link, boolean accessible) {
      link.setVisibilityAllowed(accessible);
      link.setEnabled(true);
    }
  },
  DISABLE {
    @Override
    public <L extends Link<?>> void onConfigure(L link, boolean accessible) {
      link.setVisibilityAllowed(true);
      link.setEnabled(accessible);
    }
  },
  THROW_EXCEPTION {
    @Override
    public <L extends Link<?>> void onConfigure(L link, boolean accessible) {
      // If invalid, the exception will be thrown when rendering the URL
      link.setVisibilityAllowed(true);
      link.setEnabled(true);
    }
  };

  public abstract <L extends Link<?>> void onConfigure(L link, boolean accessible);
}
