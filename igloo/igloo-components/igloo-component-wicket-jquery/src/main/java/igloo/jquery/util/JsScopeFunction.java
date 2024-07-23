package igloo.jquery.util;

import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;

public class JsScopeFunction extends JsScope {
  private static final long serialVersionUID = 1684041623069205814L;

  private String function;

  public JsScopeFunction(String function) {
    super();
    this.function = function;
  }

  @Override
  protected void execute(JsScopeContext scopeContext) {
    // nothing
  }

  @Override
  public CharSequence render() {
    return function;
  }
}
