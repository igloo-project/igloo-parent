package org.iglooproject.jpa.more.business.history.model.bean;

import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;

/**
 * An abstract base for beans containing parameters used when creating an {@link
 * AbstractHistoryLog}.
 */
public abstract class AbstractHistoryLogAdditionalInformationBean {

  /**
   * Secondary objects of the action, i.e. parameters that may not be modified as a result of the
   * action, but are at least used as an input.
   *
   * <p><strong>Caution: you should use this very rarely.</strong> Typically, the secondary objects
   * are useful when recording a two-or-more-object operation, which is rare. When the only thing
   * you need is recording contextual information, such as objects that are pointed to by links from
   * the main object that can change over time, you should really consider adding typed fields to
   * this bean and to the HistoryLog.
   */
  private Object[] secondaryObjects;

  /**
   * @param secondaryObjects see {@link #secondaryObjects}
   */
  protected AbstractHistoryLogAdditionalInformationBean(Object... secondaryObjects) {
    this.secondaryObjects = CloneUtils.clone(secondaryObjects);
  }

  public Object[] getSecondaryObjects() {
    return CloneUtils.clone(secondaryObjects);
  }
}
