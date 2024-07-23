package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask;

import igloo.jquery.util.JQueryAbstractBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.lang.Args;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public class MaskBehavior extends JQueryAbstractBehavior {

  private static final long serialVersionUID = 942354041972092047L;

  private static final String MASK = "mask";

  private final String maskPattern;

  private final MaskOptions options;

  public MaskBehavior(String maskPattern) {
    this(maskPattern, null);
  }

  public MaskBehavior(String maskPattern, MaskOptions options) {
    Args.notEmpty(maskPattern, "maskPattern");
    this.maskPattern = maskPattern;
    this.options = options;
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    response.render(JavaScriptHeaderItem.forReference(MaskJavaScriptResourceReference.get()));
    response.render(OnDomReadyHeaderItem.forScript(statement().render()));
  }

  public JsStatement statement() {
    if (options != null) {
      return new JsStatement()
          .$(getComponent())
          .chain(MASK, JsUtils.quotes(maskPattern), options.getJavaScriptOptions());
    } else {
      return new JsStatement().$(getComponent()).chain(MASK, JsUtils.quotes(maskPattern));
    }
  }
}
