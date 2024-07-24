package org.iglooproject.wicket.more.markup.repeater;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.util.IHierarchical;
import org.iglooproject.wicket.more.ajax.AjaxListeners;

/**
 * An interface for {@link AbstractRepeater repeaters} that are able to refresh their items
 * on-demand, so that an external viewer may determine added or removed items.
 *
 * <p>Implementors may do nothing upon refresh, if they are constantly up-to-date.
 *
 * @see AjaxListeners#refreshNewAndRemovedItems(IRefreshableOnDemandRepeater)
 */
public interface IRefreshableOnDemandRepeater
    extends IHierarchical<Component>, Iterable<Component> {

  /**
   * Refresh items.
   *
   * <p>This method will trigger a configure if necessary, and will update the view's items. If the
   * items have already been updated, nothing will happen.
   */
  void refreshItems();
}
