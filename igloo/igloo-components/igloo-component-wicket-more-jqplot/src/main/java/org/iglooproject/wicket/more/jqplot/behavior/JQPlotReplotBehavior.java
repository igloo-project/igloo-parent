package org.iglooproject.wicket.more.jqplot.behavior;

import java.io.Serializable;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.jqplot.plugin.adddomreference.JQPlotAddDomReferenceResourceReference;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.EventLabel;
import org.wicketstuff.wiquery.core.events.WiQueryEventBehavior;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

/**
 * A behavior that will replot JQPlot's plots whenever a JS event occurs.
 *
 * <p>This behavior is most commonly used to replot when the visibility of a plot changes, for
 * instance when switching tabs.
 */
public class JQPlotReplotBehavior extends WiQueryEventBehavior {

  private static final long serialVersionUID = 8757328427949400346L;

  /**
   * @param eventLabel The label of the event on which to trigger the replot.
   */
  public JQPlotReplotBehavior(String eventLabel) {
    this(null, eventLabel);
  }

  /**
   * @param eventLabel The label of the event on which to trigger the replot.
   */
  public JQPlotReplotBehavior(EventLabel eventLabel) {
    this(null, eventLabel);
  }

  /**
   * @param context A parent component that contains every JQPlot to replot (and only those).
   * @param eventLabel The label of the event on which to trigger the replot.
   */
  public JQPlotReplotBehavior(Component context, String eventLabel) {
    this(context, new ArbitraryEventLabel(eventLabel));
  }

  /**
   * @param context A parent component that contains every JQPlot to replot (and only those).
   * @param eventLabel The labels of the events on which to trigger the replot.
   */
  public JQPlotReplotBehavior(final Component context, EventLabel... eventLabels) {
    super(
        new Event(eventLabels) {
          private static final long serialVersionUID = 1L;

          @Override
          public JsScope callback() {
            return JsScope.quickScope(
                new JsStatement()
                    .$(context, ".jqplot-target:visible")
                    .each(
                        JsScope.quickScope(
                            new JsStatement()
                                .append("$(this)")
                                .chain("data", JsUtils.quotes("jqplot"))
                                .chain("replot"))));
          }
        });
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);
    response.render(
        JavaScriptHeaderItem.forReference(JQPlotAddDomReferenceResourceReference.get()));
  }

  private static final class ArbitraryEventLabel implements EventLabel, Serializable {
    private static final long serialVersionUID = 1L;
    private final String eventLabel;

    public ArbitraryEventLabel(String eventLabel) {
      super();
      this.eventLabel = eventLabel;
    }

    @Override
    public String getEventLabel() {
      return eventLabel;
    }
  }
}
