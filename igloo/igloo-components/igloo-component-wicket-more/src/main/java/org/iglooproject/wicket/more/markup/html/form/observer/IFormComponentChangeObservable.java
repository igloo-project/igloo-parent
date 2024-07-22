package org.iglooproject.wicket.more.markup.html.form.observer;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IFormComponentChangeObservable {
  void notify(AjaxRequestTarget target);

  void subscribe(IFormComponentChangeObserver observer);

  void unsubscribe(IFormComponentChangeObserver observer);

  /**
   * @return True if the observed form component is being submitted via Ajax using the observer
   *     mechanism (in the current request cycle).
   */
  boolean isBeingSubmitted();
}
