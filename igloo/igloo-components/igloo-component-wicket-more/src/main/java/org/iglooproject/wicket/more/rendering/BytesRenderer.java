package org.iglooproject.wicket.more.rendering;

import com.google.common.collect.ImmutableList;
import igloo.wicket.renderer.Renderer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import org.apache.wicket.util.lang.Bytes;

public class BytesRenderer extends Renderer<Bytes> {

  private static final long serialVersionUID = -5011963259015482288L;

  private static final double BYTES_IN_KB = 1024;

  private static final double DISPLAY_LIMIT = 1000;

  private static final String OUTPUT_DECIMAL_FORMAT = "#.#";

  private static final String BYTE_UNIT_KEY = "bytes.unit.byte";
  private static final String KB_UNIT_KEY = "bytes.unit.kilobyte";
  private static final String MB_UNIT_KEY = "bytes.unit.megabyte";
  private static final String GB_UNIT_KEY = "bytes.unit.gigabyte";
  private static final String TB_UNIT_KEY = "bytes.unit.terabyte";

  private static final List<String> DEFAULT_UNIT_KEYS =
      List.of(BYTE_UNIT_KEY, KB_UNIT_KEY, MB_UNIT_KEY, GB_UNIT_KEY, TB_UNIT_KEY);

  private static final BytesRenderer INSTANCE = new BytesRenderer();

  public static BytesRenderer get() {
    return INSTANCE;
  }

  private final List<String> unitKeys;

  private BytesRenderer() {
    this.unitKeys = List.copyOf(DEFAULT_UNIT_KEYS);
  }

  protected BytesRenderer(Iterable<String> unitKeys) {
    this.unitKeys = ImmutableList.copyOf(unitKeys);
  }

  @Override
  public String render(Bytes value, Locale locale) {
    double humanReadableSize = value.bytes();
    int unitKeyIndex = 0;

    while (humanReadableSize > DISPLAY_LIMIT && unitKeyIndex < unitKeys.size() - 1) {
      humanReadableSize = humanReadableSize / BYTES_IN_KB;
      unitKeyIndex++;
    }

    DecimalFormat twoDecimalsFormat =
        new DecimalFormat(OUTPUT_DECIMAL_FORMAT, DecimalFormatSymbols.getInstance(locale));

    return new StringBuilder(twoDecimalsFormat.format(humanReadableSize))
        .append(" ")
        .append(getString(unitKeys.get(unitKeyIndex), locale))
        .toString();
  }
}
