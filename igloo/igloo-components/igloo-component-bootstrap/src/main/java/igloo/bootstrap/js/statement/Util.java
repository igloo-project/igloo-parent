package igloo.bootstrap.js.statement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

class Util {

  private Util() {}

  private static final Pattern PATTERN = Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$]*");

  public static String escapeIdentifier(String identifier) {
    if (!PATTERN.matcher(identifier).matches()) {
      return escape(identifier);
    } else {
      return identifier;
    }
  }

  public static long epochMilli(LocalDate date) {
    return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  public static long epochMilli(LocalDateTime dateTime) {
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Renders {@code value} as a JS string literal, safe inside an inline HTML script: the value may
   * come from the user. Besides quotes, backslashes and line terminators, {@code <} and {@code >}
   * are escaped, so that the value can neither close the script ({@code </script>}) nor change how
   * the browser parses it ({@code <!--}, {@code ]]>}).
   */
  public static String escape(String value) {
    StringBuilder result = new StringBuilder(value.length() + 2).append('"');
    value
        .chars()
        .forEach(c -> {
          switch (c) {
            case '"' -> result.append("\\\"");
            case '\\' -> result.append("\\\\");
            case '\n' -> result.append("\\n");
            case '\r' -> result.append("\\r");
            case '\t' -> result.append("\\t");
            case '<', '>', '\u2028', '\u2029' -> result.append("\\u%04X".formatted(c));
            default -> {
              if (c < 0x20) {
                result.append("\\u%04X".formatted(c));
              } else {
                result.append((char) c);
              }
            }
          }
        });
    return result.append('"').toString();
  }
}
