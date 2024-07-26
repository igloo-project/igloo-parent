package igloo.jquery.util;

import igloo.jquery.resource.IglooUtilsJavaScriptResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;

public abstract class AbstractCoreJQueryPluginResourceReference
    extends JQueryPluginResourceReference {

  private static final long serialVersionUID = -1602756285653913404L;

  public AbstractCoreJQueryPluginResourceReference(Class<?> scope, String name) {
    super(scope, name);
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.add(
        JavaScriptHeaderItem.forReference(IglooUtilsJavaScriptResourceReference.get()));
    return dependencies;
  }
}
