package org.iglooproject.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.repeater.AbstractRepeater;

public class HideableAjaxPagingNavigator extends HideablePagingNavigator {
  private static final long serialVersionUID = -4406782762372796027L;

  public HideableAjaxPagingNavigator(String id, IPageable pageable) {
    super(id, pageable, null);
  }

  public HideableAjaxPagingNavigator(
      String id, IPageable pageable, IPagingLabelProvider labelProvider) {
    super(id, pageable, labelProvider);
  }

  public HideableAjaxPagingNavigator(String id, IPageable pageable, int viewSize) {
    super(id, pageable, viewSize);
  }

  public HideableAjaxPagingNavigator(
      String id, IPageable pageable, IPagingLabelProvider labelProvider, int viewSize) {
    super(id, pageable, labelProvider, viewSize);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    setOutputMarkupId(true);
  }

  @Override
  protected Link<?> newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
    return new AjaxBootstrapPagingNavigationLink(id, pageable, pageNumber);
  }

  @Override
  protected BootstrapPagingNavigation newNavigation(
      final String id, final IPageable pageable, final IPagingLabelProvider labelProvider) {
    return new AjaxBootstrapPagingNavigation(id, pageable, labelProvider);
  }

  protected void onAjaxEvent(AjaxRequestTarget target) {
    // update the container (parent) of the pageable, this assumes that
    // the pageable is a component, and that it is a child of a web
    // markup container.

    Component container = ((Component) getPageable());
    // no need for a nullcheck as there is bound to be a non-repeater
    // somewhere higher in the hierarchy
    while (container instanceof AbstractRepeater) {
      container = container.getParent();
    }
    target.add(container);

    // in case the navigator is not contained by the container, we have
    // to add it to the response
    if (((MarkupContainer) container).contains(this, true) == false) {
      target.add(this);
    }
  }
}
