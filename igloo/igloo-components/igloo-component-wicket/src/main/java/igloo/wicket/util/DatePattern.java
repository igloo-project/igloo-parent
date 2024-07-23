package igloo.wicket.util;

public enum DatePattern implements IDatePattern {
  DAY("date.format.day", "javascript.date.format.day"),
  DAY_FIXED("date.format.dayFixed", "javascript.date.format.dayFixed"),

  SHORT_DATE("date.format.shortDate", "javascript.date.format.shortDate"),
  SHORT_DATETIME("date.format.shortDateTime", null),

  REALLY_SHORT_DATE("date.format.reallyShortDate", "javascript.date.format.reallyShortDate"),
  REALLY_SHORT_DATETIME("date.format.reallyShortDateTime", null),

  YEAR("date.format.year", "javascript.date.format.year"),

  MONTH_YEAR("date.format.monthYear", "javascript.date.format.monthYear"),
  SHORT_MONTH_YEAR("date.format.shortMonthYear", "javascript.date.format.shortMonthYear"),
  COMPLETE_MONTH_YEAR(
      "date.format.completeMonthYear", "javascript.date.format.completeMonthYear", true),

  TIME("date.format.time", null);

  private String javaPatternKey;

  private String javascriptPatternKey;

  private boolean capitalize;

  private DatePattern(String javaPatternKey, String javascriptPatternKey) {
    this(javaPatternKey, javascriptPatternKey, false);
  }

  private DatePattern(String javaPatternKey, String javascriptPatternKey, boolean capitalize) {
    this.javaPatternKey = javaPatternKey;
    this.javascriptPatternKey = javascriptPatternKey;
    this.capitalize = capitalize;
  }

  @Override
  public String getJavaPatternKey() {
    if (javaPatternKey != null) {
      return javaPatternKey;
    } else {
      throw new IllegalStateException("Java format not supported for this format");
    }
  }

  @Override
  public String getJavascriptPatternKey() {
    if (javascriptPatternKey != null) {
      return javascriptPatternKey;
    } else {
      throw new IllegalStateException("Javascript format not supported for this format");
    }
  }

  @Override
  public boolean capitalize() {
    return capitalize;
  }
}
