package igloo.bootstrap4.markup.html.template.js.bootstrap.collapse;

import igloo.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import igloo.jquery.util.WebjarsJQueryPluginResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapCollapseJavaScriptResourceReference
    extends WebjarsJQueryPluginResourceReference {

  private static final long serialVersionUID = -1442288640907214154L;

  private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES =
      WebjarUtil.memoizeHeaderItemsforReferences(BootstrapUtilJavaScriptResourceReference.get());

  private static final BootstrapCollapseJavaScriptResourceReference INSTANCE =
      new BootstrapCollapseJavaScriptResourceReference();

  private BootstrapCollapseJavaScriptResourceReference() {
    super("bootstrap4/current/js/dist/collapse.js");
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.addAll(DEPENDENCIES.get());
    return dependencies;
  }

  public static BootstrapCollapseJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
