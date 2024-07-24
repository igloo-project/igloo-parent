package org.iglooproject.wicket.more.markup.html.template.model;

import java.io.Serializable;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.markup.html.template.component.LinkGeneratorBreadCrumbElementPanel;
import org.iglooproject.wicket.more.markup.html.template.component.SimpleBreadCrumbElementPanel;

public class BreadCrumbElement implements Serializable {

  private static final long serialVersionUID = -44367801976105581L;

  private IModel<String> labelModel;

  private ILinkGenerator linkGenerator;

  public BreadCrumbElement(IModel<String> labelModel) {
    this.labelModel = labelModel;
  }

  public BreadCrumbElement(IModel<String> labelModel, ILinkGenerator linkGenerator) {
    this.labelModel = labelModel;
    this.linkGenerator = linkGenerator;
  }

  public IModel<String> getLabelModel() {
    return labelModel;
  }

  public ILinkGenerator getLinkGenerator() {
    return linkGenerator;
  }

  public Component component(
      String wicketId, BreadCrumbMarkupTagRenderingBehavior renderingBehavior) {
    if (linkGenerator != null) {
      return new LinkGeneratorBreadCrumbElementPanel(wicketId, this, renderingBehavior);
    } else {
      return new SimpleBreadCrumbElementPanel(wicketId, this, renderingBehavior);
    }
  }

  public void detach() {
    if (labelModel != null) {
      labelModel.detach();
    }
  }
}
