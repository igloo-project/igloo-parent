package igloo.bootstrap.util;

import org.apache.wicket.Application;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.core.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.markup.ContainerInfo;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

public class Utils {

  private Utils() {}

  public static IResourceStream findResourceStream(
      final MarkupContainer container, final Class<?> originalContainerClass) {
    Class<?> containerClass = originalContainerClass;
    final IResourceStreamLocator locator =
        Application.get().getResourceSettings().getResourceStreamLocator();
    IResourceStream resourceStream = null;
    while (resourceStream == null) {
      String path = containerClass.getName().replace('.', '/');
      resourceStream = locator.locate(containerClass, path, null, null, null, "html", false);
      if (resourceStream != null) {
        return new MarkupResourceStream(
            resourceStream, new ContainerInfo(containerClass, container), containerClass);
      }
      containerClass = containerClass.getSuperclass();
    }
    throw new IllegalStateException(
        String.format("Markup cannot be found for %s", originalContainerClass));
  }
}
