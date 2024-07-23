package org.iglooproject.wicket.more.markup.html.form.observer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.io.IClusterable;

public interface IFormComponentChangeObserver extends IClusterable {
  void onChange(AjaxRequestTarget target);
}
