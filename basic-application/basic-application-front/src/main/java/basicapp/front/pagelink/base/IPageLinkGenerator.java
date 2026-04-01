package basicapp.front.pagelink.base;

import basicapp.front.pagelink.component.PageLinkBookmarkablePageLink;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IDetachable;

public interface IPageLinkGenerator<P extends Page> extends IDetachable {

  Class<P> getPageClass();

  IPageLinkGenerator<P> bypassPermissions();

  boolean isValid();

  PageLinkBookmarkablePageLink<P> link(String wicketId);

  RestartResponseException restartResponseException();

  String url();

  String fullUrl();
}
