package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.multivaluedexpand;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

/**
 * Add an iconic button "+/-" to expand/skrink a ".multivalued" component if it contains more than
 * one ".multivalued-item". The ".multivalued" component is typically a cell in a table, but it
 * could be a simple div.
 *
 * <p>This plugin needs to add styles on the following components to display multivalued items and
 * toggle button.
 *
 * <ul>
 *   <li>.multivalued
 *   <li>.multivalued .multivalued-item
 *   <li>.multivalued a.expand-toggle
 *   <li>.multivalued.closed
 * </ul>
 */
public class MultivaluedExpandBehavior extends Behavior {

  private static final long serialVersionUID = -4702519833307463534L;

  private final MultivaluedExpand multivaluedExpand;

  public MultivaluedExpandBehavior() {
    this(new MultivaluedExpand());
  }

  public MultivaluedExpandBehavior(MultivaluedExpand multivaluedExpand) {
    super();
    this.multivaluedExpand = multivaluedExpand;
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    response.render(JavaScriptHeaderItem.forReference(MultivaluedExpandResourceReference.get()));
    response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
  }

  protected JsStatement statement(Component component) {
    return new JsStatement().$(component).chain(multivaluedExpand);
  }
}
