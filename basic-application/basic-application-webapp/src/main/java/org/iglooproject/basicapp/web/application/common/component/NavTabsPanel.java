package org.iglooproject.basicapp.web.application.common.component;

import igloo.bootstrap.tab.BootstrapTabBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.web.application.common.util.BootstrapTabsUtils;

public class NavTabsPanel extends Panel {

  private static final long serialVersionUID = -2317592648347012891L;

  private RepeatingView tabsRepeatingView;
  private RepeatingView panelsRepeatingView;

  public NavTabsPanel(String id) {
    super(id);
    this.tabsRepeatingView = new RepeatingView("tabs");
    this.panelsRepeatingView = new RepeatingView("panels");

    add(tabsRepeatingView, panelsRepeatingView);
  }

  public interface ITabFactory extends IDetachable {

    String getMarkupId();

    Component createLabel(String wicketId);

    Component createContent(String wicketId);

    @Override
    default void detach() {
      // nothing to do
    }
  }

  public abstract static class SimpleTabFactory implements ITabFactory {

    private static final long serialVersionUID = 1L;

    private final String markupId;

    private final IModel<String> labelModel;

    public SimpleTabFactory(String markupId, String labelKey) {
      this(markupId, new ResourceModel(labelKey));
    }

    public SimpleTabFactory(String markupId, IModel<String> labelModel) {
      super();
      this.markupId = markupId;
      this.labelModel = labelModel;
    }

    @Override
    public String getMarkupId() {
      return markupId;
    }

    @Override
    public Component createLabel(String wicketId) {
      return new CoreLabel(wicketId, labelModel);
    }

    @Override
    public void detach() {
      ITabFactory.super.detach();
      Detachables.detach(labelModel);
    }
  }

  public NavTabsPanel add(ITabFactory factory) {
    IModel<Boolean> activeModel = Model.of(panelsRepeatingView.size() == 0);

    WebMarkupContainer tab = new WebMarkupContainer("tab");
    Component panel = factory.createContent(panelsRepeatingView.newChildId());

    tabsRepeatingView.add(
        new WebMarkupContainer(tabsRepeatingView.newChildId())
            .add(tab.add(factory.createLabel("label")))
            .add(new BootstrapTabBehavior()));

    panelsRepeatingView.add(panel);

    BootstrapTabsUtils.build(factory.getMarkupId(), tab, panel, activeModel);

    return this;
  }
}
