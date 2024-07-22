package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop;

import java.io.Serializable;
import org.wicketstuff.wiquery.core.javascript.ChainableStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

public class ScrollToTop implements ChainableStatement, Serializable {

  private static final long serialVersionUID = 7606503484797745560L;

  private static final String SCROLL_TO_TOP = "scrollToTop";

  private String scrollableSelector;

  private String scrollableItemSelector;

  public ScrollToTop() {
    super();
  }

  @Override
  public String chainLabel() {
    return SCROLL_TO_TOP;
  }

  @Override
  public CharSequence[] statementArgs() {
    Options options = new Options();

    if (scrollableSelector != null) {
      options.put("scrollableSelector", scrollableSelector);
    }

    if (scrollableItemSelector != null) {
      options.put("scrollableItemSelector", scrollableItemSelector);
    }

    return new CharSequence[] {options.getJavaScriptOptions()};
  }

  public String getScrollableSelector() {
    return scrollableSelector;
  }

  public void setScrollableSelector(String scrollableSelector) {
    setScrollableSelector(scrollableSelector, true);
  }

  public void setScrollableSelector(String scrollableSelector, boolean quotes) {
    if (quotes) {
      this.scrollableSelector = JsUtils.quotes(scrollableSelector);
    } else {
      this.scrollableSelector = scrollableSelector;
    }
  }

  public String getScrollableItemSelector() {
    return scrollableItemSelector;
  }

  public void setScrollableItemSelector(String scrollableItemSelector) {
    setScrollableItemSelector(scrollableItemSelector, true);
  }

  public void setScrollableItemSelector(String scrollableItemSelector, boolean quotes) {
    if (quotes) {
      this.scrollableItemSelector = JsUtils.quotes(scrollableItemSelector);
    } else {
      this.scrollableItemSelector = scrollableItemSelector;
    }
  }
}
