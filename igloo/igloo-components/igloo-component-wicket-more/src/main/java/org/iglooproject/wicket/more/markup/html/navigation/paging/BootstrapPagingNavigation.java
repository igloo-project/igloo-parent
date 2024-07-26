package org.iglooproject.wicket.more.markup.html.navigation.paging;

import igloo.wicket.behavior.ClassAttributeAppender;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigationLink;

public class BootstrapPagingNavigation extends PagingNavigation {

  private static final long serialVersionUID = -1227712391251278582L;

  public BootstrapPagingNavigation(
      String id, IPageable pageable, IPagingLabelProvider labelProvider) {
    super(id, pageable, labelProvider);
  }

  @Override
  protected void populateItem(LoopItem loopItem) {
    super.populateItem(loopItem);

    Component link = loopItem.get("pageLink");
    link.add(
        new Behavior() {
          private static final long serialVersionUID = 1L;

          @Override
          public void onComponentTag(Component component, ComponentTag tag) {
            super.onComponentTag(component, tag);
            tag.remove("title");
          }
        });

    long pageIndex = getStartIndex() + loopItem.getIndex();
    if (pageable.getCurrentPage() == pageIndex) {
      loopItem.add(new ClassAttributeAppender("active"));
    }
  }

  public boolean isBeginning() {
    return getStartIndex() == 0;
  }

  public boolean isEnding() {
    return getStartIndex() + getViewSize() >= pageable.getPageCount();
  }

  public boolean lessThanViewSize() {
    return pageable.getPageCount() < getViewSize();
  }

  @Override
  protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, long pageIndex) {
    return new PagingNavigationLink<Void>(id, pageable, pageIndex).setAutoEnable(false);
  }
}
