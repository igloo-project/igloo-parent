package igloo.bootstrap4.markup.html.template.js.bootstrap.popover;

import igloo.bootstrap4.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipJavaScriptResourceReference;
import igloo.jquery.util.WebjarsJQueryPluginResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapPopoverJavaScriptResourceReference
    extends WebjarsJQueryPluginResourceReference {

  private static final long serialVersionUID = -1442288640907214154L;

  private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES =
      WebjarUtil.memoizeHeaderItemsforReferences(BootstrapTooltipJavaScriptResourceReference.get());

  private static final BootstrapPopoverJavaScriptResourceReference INSTANCE =
      new BootstrapPopoverJavaScriptResourceReference();

  private BootstrapPopoverJavaScriptResourceReference() {
    super("bootstrap4/current/js/dist/popover.js");
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.addAll(DEPENDENCIES.get());
    return dependencies;
  }

  public static BootstrapPopoverJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
