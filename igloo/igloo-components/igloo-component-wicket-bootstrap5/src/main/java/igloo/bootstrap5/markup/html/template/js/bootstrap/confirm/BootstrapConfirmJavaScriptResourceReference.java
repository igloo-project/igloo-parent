package igloo.bootstrap5.markup.html.template.js.bootstrap.confirm;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import igloo.bootstrap5.markup.html.template.js.bootstrap.Bootstrap5JavaScriptResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapConfirmJavaScriptResourceReference
    extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = -1442288640907214154L;

  private static final BootstrapConfirmJavaScriptResourceReference INSTANCE =
      new BootstrapConfirmJavaScriptResourceReference();

  private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES =
      WebjarUtil.memoizeHeaderItemsforReferences(Bootstrap5JavaScriptResourceReference.get());

  private BootstrapConfirmJavaScriptResourceReference() {
    super("bootstrap5-override/current/js/dist/confirm.js");
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.addAll(DEPENDENCIES.get());
    return dependencies;
  }

  public static BootstrapConfirmJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
