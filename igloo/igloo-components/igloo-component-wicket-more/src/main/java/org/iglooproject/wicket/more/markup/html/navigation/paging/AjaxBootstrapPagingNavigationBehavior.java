package org.iglooproject.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.IAjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationBehavior;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class AjaxBootstrapPagingNavigationBehavior extends AjaxPagingNavigationBehavior {

  private static final long serialVersionUID = 4857502136969718743L;

  protected IAjaxLink owner;

  public AjaxBootstrapPagingNavigationBehavior(IAjaxLink owner, IPageable pageable, String event) {
    super(owner, pageable, event);
    this.owner = owner;
  }

  @Override
  protected void onEvent(AjaxRequestTarget target) {
    // handle the event
    owner.onClick(target);

    // find the PagingNavigator parent of this link
    HideableAjaxPagingNavigator navigator =
        ((Component) owner).findParent(HideableAjaxPagingNavigator.class);

    // if this is embedded inside a navigator
    if (navigator != null) {
      // tell the PagingNavigator to update the IPageable
      navigator.onAjaxEvent(target);
    }
  }
}
