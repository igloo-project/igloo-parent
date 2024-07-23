package org.iglooproject.wicket.more.markup.html.template.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

public class BreadCrumbListView extends ListView<BreadCrumbElement> {

  private static final long serialVersionUID = -6354654324654295238L;

  private final BreadCrumbMarkupTagRenderingBehavior renderingBehavior;

  private final IModel<String> dividerModel;

  public BreadCrumbListView(
      String id,
      IModel<List<BreadCrumbElement>> breadCrumb,
      BreadCrumbMarkupTagRenderingBehavior renderingBehavior,
      IModel<String> dividerModel) {
    super(id, breadCrumb);
    this.renderingBehavior = renderingBehavior;
    this.dividerModel = dividerModel;
  }

  @Override
  protected void populateItem(ListItem<BreadCrumbElement> item) {
    item.add(
        new CoreLabel("divider", dividerModel)
            .hideIfEmpty()
            .setVisibilityAllowed(item.getIndex() > 0));

    Component breadCrumbLink =
        item.getModelObject().component("breadCrumbElement", renderingBehavior);
    item.add(breadCrumbLink);

    item.add(Condition.componentVisible(breadCrumbLink).thenShow());
  }
}
