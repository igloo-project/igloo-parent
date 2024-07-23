package org.iglooproject.basicapp.web.application.history.component;

import static igloo.wicket.condition.Condition.predicate;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;

public class CompositeHistoryDifferencePanel extends GenericPanel<HistoryDifference> {

  private static final long serialVersionUID = 1L;

  /**
   * Displays a composite difference as a block: first a line for the composite field, then one line
   * for each sub-field.
   *
   * <p>Sub-field lines are prefixed with paths by default. This can be overridden using CSS.
   */
  public static IHistoryComponentFactory block(final IHistoryComponentFactory childFactory) {
    return new IHistoryComponentFactory() {
      private static final long serialVersionUID = 1L;

      @Override
      public Component create(String wicketId, IModel<HistoryDifference> parameter1) {
        return new CompositeHistoryDifferencePanel(
            wicketId, parameter1, "history-difference-block", childFactory);
      }

      @Override
      public void detach() {
        Detachables.detach(childFactory);
      }
    };
  }

  /**
   * Displays a composite difference inline: one line for both the composite field, a a list of
   * sub-field values.
   *
   * <p>Sub-field paths are not displayed by default. This can be overridden using CSS.
   */
  public static IHistoryComponentFactory inline(final IHistoryComponentFactory childFactory) {
    return new IHistoryComponentFactory() {
      private static final long serialVersionUID = 1L;

      @Override
      public Component create(String wicketId, IModel<HistoryDifference> parameter1) {
        return new CompositeHistoryDifferencePanel(
            wicketId, parameter1, "history-difference-inline", childFactory);
      }

      @Override
      public void detach() {
        Detachables.detach(childFactory);
      }
    };
  }

  public CompositeHistoryDifferencePanel(
      String id,
      IModel<HistoryDifference> model,
      String cssClass,
      IHistoryComponentFactory historyComponentFactory) {
    super(id, model);

    IModel<HistoryDifferenceEventType> eventTypeModel =
        BindingModel.of(model, Bindings.historyDifference().eventType());

    add(
        new EnclosureContainer("updated")
            .condition(
                predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.UPDATED)))
            .add(newHistoryDifferenceListPanel("differences", cssClass, historyComponentFactory)),
        new EnclosureContainer("untouched")
            .condition(
                predicate(
                    eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.UNTOUCHED))),
        new EnclosureContainer("added")
            .condition(
                predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.ADDED)))
            .add(newHistoryDifferenceListPanel("differences", cssClass, historyComponentFactory)),
        new EnclosureContainer("removed")
            .condition(
                predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.REMOVED)))
            .add(newHistoryDifferenceListPanel("differences", cssClass, historyComponentFactory)));
  }

  private Component newHistoryDifferenceListPanel(
      String wicketId, String cssClass, IHistoryComponentFactory historyComponentFactory) {
    return new HistoryDifferenceListPanel(
            wicketId,
            BindingModel.of(getModel(), Bindings.historyDifference().differences()),
            historyComponentFactory)
        .add(new ClassAttributeAppender(cssClass));
  }
}
