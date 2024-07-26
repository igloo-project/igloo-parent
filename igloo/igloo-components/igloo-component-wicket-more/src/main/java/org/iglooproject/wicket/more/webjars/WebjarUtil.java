package org.iglooproject.wicket.more.webjars;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;

public final class WebjarUtil {

  private WebjarUtil() {} // NOSONAR

  public static SerializableSupplier2<List<HeaderItem>> memoizeHeaderItemsforReferences(
      ResourceReference... references) {
    return Suppliers2.memoize(
        () ->
            Lists.newArrayList(references).stream()
                .map(WebjarUtil::forReference)
                .collect(Collectors.toList()));
  }

  private static HeaderItem forReference(ResourceReference reference) {
    if (reference instanceof JavaScriptResourceReference) {
      return JavaScriptHeaderItem.forReference(reference);
    } else if (reference instanceof CssResourceReference) {
      return CssHeaderItem.forReference(reference);
    } else {
      throw new IllegalArgumentException(
          String.format(
              "Type %s of [%s] cannot be linked to a javascript or css item",
              reference.getClass().getSimpleName(), reference));
    }
  }
}
