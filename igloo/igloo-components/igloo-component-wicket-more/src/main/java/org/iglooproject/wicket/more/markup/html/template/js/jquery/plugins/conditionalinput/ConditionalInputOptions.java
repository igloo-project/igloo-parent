package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.conditionalinput;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

public class ConditionalInputOptions extends Options {

  private static final long serialVersionUID = 3560026007808787034L;

  public ConditionalInputOptions(Component enableOption, ConditionalInputAction action) {
    super();
    put("enableOption", new JsStatement().$(enableOption).render(false).toString());
    if (action != null) {
      put("action", JsUtils.quotes(action.getLabel()));
    }
  }
}
