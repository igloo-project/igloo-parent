package basicapp.front.pagelink.util;

import basicapp.front.pagelink.component.PageLinkBookmarkablePageLink;

public enum PageLinkInvalidityStrategy {
  HIDE {
    @Override
    public void onConfigure(PageLinkBookmarkablePageLink<?> link, boolean accessible) {
      link.setVisibilityAllowed(accessible);
      link.setEnabled(true);
    }
  },
  DISABLE {
    @Override
    public void onConfigure(PageLinkBookmarkablePageLink<?> link, boolean accessible) {
      link.setVisibilityAllowed(true);
      link.setEnabled(accessible);
    }
  };

  public abstract void onConfigure(PageLinkBookmarkablePageLink<?> link, boolean accessible);
}
