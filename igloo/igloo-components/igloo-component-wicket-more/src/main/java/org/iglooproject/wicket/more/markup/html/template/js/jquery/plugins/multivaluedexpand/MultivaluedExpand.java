package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.multivaluedexpand;

import java.io.Serializable;
import org.wicketstuff.wiquery.core.javascript.ChainableStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

public class MultivaluedExpand implements ChainableStatement, Serializable {

  private static final long serialVersionUID = 4804809311019571322L;

  private static final String MULTIVALUED_EXPAND = "multivaluedExpand";

  private String toggleButtonHtml;

  public MultivaluedExpand() {
    super();
  }

  @Override
  public String chainLabel() {
    return MULTIVALUED_EXPAND;
  }

  @Override
  public CharSequence[] statementArgs() {
    Options options = new Options();

    if (toggleButtonHtml != null) {
      options.put("toggleButtonHtml", JsUtils.quotes(toggleButtonHtml, true));
    }

    return new CharSequence[] {options.getJavaScriptOptions()};
  }

  /**
   * Allows to override default "toggle" button.<br>
   * <br>
   * Default button: see JS file.<br>
   * Override sample:<br>
   * {@code <a><span class="fa fa-fw fa-plus-circle"></span><span class="fa fa-fw
   * fa-minus-circle"></span></a>}
   *
   * @param toggleButtonHtml HTML code that represents the toggle button
   * @return this
   */
  public MultivaluedExpand toggleButtonHtml(String toggleButtonHtml) {
    this.toggleButtonHtml = toggleButtonHtml;
    return this;
  }
}
