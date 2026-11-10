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
  };

  public abstract <L extends Link<?>> void onConfigure(L link, boolean accessible);
}
