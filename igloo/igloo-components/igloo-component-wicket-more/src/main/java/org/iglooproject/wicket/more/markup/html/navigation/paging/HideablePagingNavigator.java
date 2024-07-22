package org.iglooproject.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigationLink;
import org.apache.wicket.markup.html.panel.Panel;

public class HideablePagingNavigator extends Panel {

  private static final long serialVersionUID = 1L;

  private static final int DEFAULT_VIEW_SIZE = 11;

  private BootstrapPagingNavigation pagingNavigation;
  private final IPageable pageable;
  private final IPagingLabelProvider labelProvider;
  private int viewSize;
  private AbstractLink first;
  private AbstractLink last;

  public HideablePagingNavigator(String id, IPageable pageable) {
    this(id, pageable, null);
  }

  public HideablePagingNavigator(final String id, final IPageable pageable, int viewSize) {
    this(id, pageable, null, viewSize);
  }

  public HideablePagingNavigator(
      final String id, final IPageable pageable, final IPagingLabelProvider labelProvider) {
    this(id, pageable, labelProvider, -1);
  }

  public HideablePagingNavigator(
      final String id,
      final IPageable pageable,
      final IPagingLabelProvider labelProvider,
      int viewSize) {
    super(id);
    this.pageable = pageable;
    this.labelProvider = labelProvider;
    if (viewSize > 0) {
      this.viewSize = viewSize;
    } else {
      this.viewSize = DEFAULT_VIEW_SIZE;
    }
  }

  public final IPageable getPageable() {
    return pageable;
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();

    // Get the navigation bar and add it to the hierarchy
    pagingNavigation = newNavigation("navigation", pageable, labelProvider);
    pagingNavigation.setViewSize(viewSize);
    add(pagingNavigation);

    // Add additional page links
    first = newPagingNavigationLink("first", pageable, 0);
    add(first);
    last = newPagingNavigationLink("last", pageable, -1);
    add(last);
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();
    IPageable pageable = getPageable();
    setVisible(pageable.getPageCount() > 1);
  }

  @Override
  protected void onBeforeRender() {
    super.onBeforeRender();
    first.setVisible(!pagingNavigation.isBeginning());
    last.setVisible(!pagingNavigation.isEnding());
  }

  protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
    return new PagingNavigationLink<Void>(id, pageable, pageNumber);
  }

  protected BootstrapPagingNavigation newNavigation(
      final String id, final IPageable pageable, final IPagingLabelProvider labelProvider) {
    return new BootstrapPagingNavigation(id, pageable, labelProvider);
  }
}
