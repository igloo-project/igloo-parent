package org.iglooproject.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class AjaxBootstrapPagingNavigation extends BootstrapPagingNavigation {

  private static final long serialVersionUID = -8839242459796577683L;

  public AjaxBootstrapPagingNavigation(
      String id, IPageable pageable, IPagingLabelProvider labelProvider) {
    super(id, pageable, labelProvider);
  }

  @Override
  protected Link<?> newPagingNavigationLink(String id, IPageable pageable, long pageIndex) {
    return new AjaxBootstrapPagingNavigationLink(id, pageable, pageIndex).setAutoEnable(false);
  }
}
