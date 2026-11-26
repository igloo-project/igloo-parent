package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IVueComponent {
  void jsAppendCssClass(AjaxRequestTarget target, String cssClass);

  void jsRemoveCssClass(AjaxRequestTarget target, String cssClass);
}
