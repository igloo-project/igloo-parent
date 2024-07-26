package org.iglooproject.wicket.more.jqplot.util;

import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.util.Locale;
import java.util.Set;

public final class ChartColors {

  private ChartColors() {}

  public static final Color TRANSPARENT = toColor(0xFFFFFF, 0x00);

  private static final int DEFAULT_ALPHA = 0xFF; // IE8 compatibility => no transparency

  public static final Color BACKGROUND = TRANSPARENT;

  public static final Color SUCCESS = toColor(0x5BB75B);

  public static final Color DANGER = toColor(0xB94A48);

  public static final Color ONGOING = toColor(0xD76D00);

  public static final Color INFO = toColor(0x3A87AD);

  public static final Color DARK_GREY = toColor(0x999999);

  public static final Color BLUE = toColor(0x376091);

  public static final Color RED = toColor(0x953735);

  public static final Color GREEN = toColor(0x4F6228);

  public static final Color POSITIVE = toColor(0x97bf0d);

  public static final Color NEGATIVE = toColor(0xd95152);

  @SuppressWarnings("squid:S2386")
  public static final Set<Color> BLUE_PALETTE =
      toColors(0x254061, 0x376091, 0x95B3D7, 0xB8CCE4, 0x3F3151, 0x60497B, 0xB3A1C7, 0xCCC0DA);

  @SuppressWarnings("squid:S2386")
  public static final Set<Color> RED_PALETTE = toColors(0x632527, 0x953735, 0xD99795, 0xE6B9B8);

  @SuppressWarnings("squid:S2386")
  public static final Set<Color> ORANGE_PALETTE = toColors(0x974807, 0xE46D0A, 0xFAC090, 0xFCD5B4);

  @SuppressWarnings("squid:S2386")
  public static final Set<Color> GREEN_PALETTE = toColors(0x4F6228, 0x75923C, 0xC2D69A, 0xD7E4BC);

  @SuppressWarnings("squid:S2386")
  public static final Set<Color> SCATTERED_PALETTE =
      toColors(
          0x4bb2c5, 0xEAA228, 0xc5b47f, 0x579575, 0x839557, 0x958c12, 0x953579, 0x4b5de4, 0xd8b83f,
          0xff5800, 0x0085cc, 0xc747a3, 0xcddf54, 0xFBD178, 0x26B4E3, 0xbd70c7);

  public static Color toColor(int rgb) {
    return toColor(rgb, DEFAULT_ALPHA);
  }

  public static Color toColor(int rgb, int alpha) {
    int alphaBits = (alpha << 24); // Shift 8*3 = 24 bits, 8bits for each component in R, G, B
    return new Color(rgb & 0x00FFFFFF | alphaBits, true);
  }

  public static Set<Color> toColors(int... rgbList) {
    ImmutableSet.Builder<Color> builder = ImmutableSet.builder();
    for (int rgb : rgbList) {
      builder.add(toColor(rgb));
    }
    return builder.build();
  }

  @SafeVarargs
  public static Set<Color> concat(Iterable<Color>... palettes) {
    ImmutableSet.Builder<Color> builder = ImmutableSet.builder();
    for (Iterable<Color> palette : palettes) {
      builder.addAll(palette);
    }
    return builder.build();
  }

  public static String toCssString(Color color) {
    float alpha = color.getRGBComponents(null)[3];
    if (alpha
        == 1.0f) { // Compatibilité IE8 : les couleurs avec alpha = 1.0 seront ainsi supportées.
      return String.format(
          Locale.ROOT, "rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    } else {
      return String.format(
          Locale.ROOT,
          "rgba(%d, %d, %d, %.3f)",
          color.getRed(),
          color.getGreen(),
          color.getBlue(),
          alpha);
    }
  }
}
