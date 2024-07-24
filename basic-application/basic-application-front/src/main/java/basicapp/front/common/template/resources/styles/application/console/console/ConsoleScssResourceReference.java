package basicapp.front.common.template.resources.styles.application.console.console;

import java.util.List;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.css.scss.ScssResourceReference;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

public final class ConsoleScssResourceReference extends ScssResourceReference {

  private static final long serialVersionUID = 7402497660522113371L;

  private static final ConsoleScssResourceReference INSTANCE = new ConsoleScssResourceReference();

  private ConsoleScssResourceReference() {
    super(ConsoleScssResourceReference.class, "console.scss");
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.add(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));
    return dependencies;
  }

  public static ConsoleScssResourceReference get() {
    return INSTANCE;
  }
}
