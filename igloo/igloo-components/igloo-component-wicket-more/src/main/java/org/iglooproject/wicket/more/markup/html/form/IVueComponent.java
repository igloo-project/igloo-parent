package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IVueComponent {
  void appendCssClass(AjaxRequestTarget target, String cssClass);

  void removeCssClass(AjaxRequestTarget target, String cssClass);
}
