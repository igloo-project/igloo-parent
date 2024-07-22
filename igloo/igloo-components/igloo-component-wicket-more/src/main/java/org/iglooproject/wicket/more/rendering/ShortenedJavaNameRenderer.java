package org.iglooproject.wicket.more.rendering;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;

/**
 * A renderer that shortens Java names, such as package names, class names or fully-qualified
 * attribute names, by replacing the least-significant (e.g. leftmost) components by their first
 * letter.
 */
public class ShortenedJavaNameRenderer extends Renderer<String> {

  private static final long serialVersionUID = 1L;

  private static final String JAVA_SEPARATOR_REGEXP = "\\.";

  private static final String JAVA_SEPARATOR = ".";

  private static final ShortenedJavaNameRenderer INSTANCE = new ShortenedJavaNameRenderer(5);

  public static Renderer<String> get() {
    return INSTANCE;
  }

  private final int maximumFullComponents;

  /**
   * @param maximumFullComponents Maximum number of components in the package name that will be
   *     displayed in their full form.
   */
  public ShortenedJavaNameRenderer(int maximumFullComponents) {
    this.maximumFullComponents = maximumFullComponents;
  }

  @Override
  public String render(String value, Locale locale) {
    String[] packages = value.split(JAVA_SEPARATOR_REGEXP);
    StringBuilder displayStringBuilder = new StringBuilder();
    int packagesCount = packages.length;
    for (int cpt = 0; cpt < packagesCount; cpt++) {
      if (cpt > 0) {
        displayStringBuilder.append(JAVA_SEPARATOR);
      }
      String p = packages[cpt];
      if (cpt < packagesCount - maximumFullComponents) {
        displayStringBuilder.append(p, 0, 1);
      } else {
        displayStringBuilder.append(p);
      }
    }
    return displayStringBuilder.toString();
  }
}
