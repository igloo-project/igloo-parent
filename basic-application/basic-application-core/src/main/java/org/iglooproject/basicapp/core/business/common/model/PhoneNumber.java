package org.iglooproject.basicapp.core.business.common.model;

import com.google.common.base.CharMatcher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

public final class PhoneNumber extends AbstractMaterializedPrimitiveValue<String, PhoneNumber> {

  private static final long serialVersionUID = 7055290426664681655L;

  private static final CharMatcher SEPARATOR_MATCHER =
      CharMatcher.whitespace().or(CharMatcher.anyOf(".-"));

  private static final Pattern LOCAL_FRENCH_NUMBER_PATTERN = Pattern.compile("^\\d{10}$");
  private static final Pattern INTERNATIONAL_FRENCH_NUMBER_PATTERN =
      Pattern.compile("^(\\((00|\\+33)\\)0|(00|\\+)33)(?=\\d{9}$)");
  private static final String INTERNATION_FRENCH_NUMBER_TO_LOCAL_REPLACEMENT = "0$1";

  private static String clean(String value) {
    String noSeparator = SEPARATOR_MATCHER.removeFrom(value);
    if (StringUtils.isEmpty(noSeparator)) {
      return null;
    } else {
      if (LOCAL_FRENCH_NUMBER_PATTERN.matcher(noSeparator).find()) {
        return noSeparator;
      } else {
        Matcher internationalMatcher = INTERNATIONAL_FRENCH_NUMBER_PATTERN.matcher(noSeparator);
        if (internationalMatcher.find()) {
          return internationalMatcher.replaceFirst(INTERNATION_FRENCH_NUMBER_TO_LOCAL_REPLACEMENT);
        }
      }

      // Si le format n'est ni un numéro français tel que composé depuis la France,
      // ni un numéro français au format internationnal, on remplace simplement les séparateurs par
      // des espaces
      return SEPARATOR_MATCHER.trimAndCollapseFrom(value, ' ');
    }
  }

  public static PhoneNumber buildClean(String value) {
    String cleaned = clean(value);
    if (StringUtils.isEmpty(cleaned)) {
      return null;
    } else {
      return new PhoneNumber(cleaned);
    }
  }

  private PhoneNumber(String value) {
    super(value);
  }

  public boolean isLocalFrench() {
    return LOCAL_FRENCH_NUMBER_PATTERN.matcher(getValue()).find();
  }
}
