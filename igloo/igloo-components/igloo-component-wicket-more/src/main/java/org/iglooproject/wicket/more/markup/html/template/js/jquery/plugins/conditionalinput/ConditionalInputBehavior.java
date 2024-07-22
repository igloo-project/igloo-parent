package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.conditionalinput;

import igloo.jquery.util.JQueryAbstractBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Radio;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public class ConditionalInputBehavior extends JQueryAbstractBehavior {

  private static final long serialVersionUID = 4865316523856192642L;

  private static final String CONDITIONAL_INPUT = "conditionalInput";

  private final ConditionalInputOptions options;

  public ConditionalInputBehavior(CheckBox enableOption, ConditionalInputAction action) {
    this((Component) enableOption, action);
  }

  public ConditionalInputBehavior(Check<?> enableOption, ConditionalInputAction action) {
    this((Component) enableOption, action);
  }

  public ConditionalInputBehavior(Radio<?> enableOption, ConditionalInputAction action) {
    this((Component) enableOption, action);
  }

  private ConditionalInputBehavior(Component enableOption, ConditionalInputAction action) {
    super();
    options = new ConditionalInputOptions(enableOption, action);
  }

  @Override
  public boolean isEnabled(Component component) {
    return component.isVisibleInHierarchy()
    /**
     * Il ne faut pas tester isEnabledInHierarchy(), on peut vouloir activer la behavior même
     * lorsque le composant est désactivé
     */
    ;
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    response.render(
        JavaScriptHeaderItem.forReference(ConditionalInputJavaScriptResourceReference.get()));

    response.render(OnDomReadyHeaderItem.forScript(statement().render()));
  }

  public JsStatement statement() {
    return new JsStatement()
        .$(getComponent())
        .chain(CONDITIONAL_INPUT, JsUtils.quotes("init"), options.getJavaScriptOptions());
  }
}
