package org.iglooproject.wicket.more.markup.html.template.component;

import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

public class LinkGeneratorBreadCrumbElementPanel extends GenericPanel<String> {

  private static final long serialVersionUID = 5385792712763242343L;

  public LinkGeneratorBreadCrumbElementPanel(
      String id,
      BreadCrumbElement breadCrumbElement,
      BreadCrumbMarkupTagRenderingBehavior renderingBehavior) {
    super(id, breadCrumbElement.getLabelModel());

    Link<Void> breadCrumbLink =
        breadCrumbElement.getLinkGenerator().link("breadCrumbElementLink").hideIfInvalid();
    breadCrumbLink.setBody(getModel());
    breadCrumbLink.add(renderingBehavior);
    add(breadCrumbLink);

    add(Condition.componentVisible(breadCrumbLink).thenShowInternal());
  }
}
