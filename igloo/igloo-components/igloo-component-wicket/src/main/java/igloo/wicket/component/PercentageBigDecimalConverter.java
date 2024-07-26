package igloo.wicket.component;

import igloo.wicket.application.CoreApplication;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 * Convertisseur entre BigDecimal et String pour les pourcentages
 *
 * <p>Les pourcentages affichés entre 0 et 100 par exemple correspondent en BigDecimal à des valeurs
 * : - entre 0 et 1 si modelIsRatio est true - entre 0 et 100 si modelIsRatio est false
 *
 * <p>Le scale d'un BigDecimal correspond au nombre de chiffres après la virgule. La precision d'un
 * BigDecimal correspond au nombre total de chiffre.
 *
 * <p>Rmq : l'éventuel 0 devant la virgule ne compte pas dans la precision, au contraire des 0 à
 * droite. Ainsi, 0.504400 aurait une precision de 6 (et un scale de 6).
 *
 * <p>On attribue un scale fixe et une precision maximale autorisée aux instances de BigDecimal
 * manipulées.
 */
public class PercentageBigDecimalConverter implements IConverter<BigDecimal> {

  private static final long serialVersionUID = -4527255063711426051L;

  /**
   * Les # après la virgule sont importants car la valeur sera tronquée. Les # avant la virgule
   * n'ont pas d'importance.
   */
  private static final String PATTERN = "#.#########";

  private int scale;

  private RoundingMode roundingMode;

  private int maxPrecision;

  private boolean modelIsRatio;

  private boolean displayPercentSymbol;

  public PercentageBigDecimalConverter(int scale, RoundingMode roundingMode, int maxPrecision) {
    this(scale, roundingMode, maxPrecision, true);
  }

  public PercentageBigDecimalConverter(
      int scale, RoundingMode roundingMode, int maxPrecision, boolean modelIsRatio) {
    this(scale, roundingMode, maxPrecision, modelIsRatio, false);
  }

  public PercentageBigDecimalConverter(
      int scale,
      RoundingMode roundingMode,
      int maxPrecision,
      boolean modelIsRatio,
      boolean displayPercentSymbol) {
    super();
    if (maxPrecision < scale) {
      throw new IllegalArgumentException(
          "The maximum precision must be equal or superior to the scale.");
    }
    this.scale = scale;
    this.roundingMode = roundingMode;
    this.maxPrecision = maxPrecision;
    this.modelIsRatio = modelIsRatio;
    this.displayPercentSymbol = displayPercentSymbol;
  }

  @Override
  public BigDecimal convertToObject(String value, Locale locale) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }

    if (displayPercentSymbol) {
      value = value.replaceAll("%", "");
    }

    DecimalFormat decimalFormat = new DecimalFormat(PATTERN, getDecimalFormatSymbols());
    decimalFormat.setParseBigDecimal(true);

    getDecimalFormatSymbols().getDecimalSeparator();

    /*
     *  La vérification suivante a pour but de s'assurer que le parser envoie une exception pour des données
     *  commençant par des chiffres suivis d'autres caractères non valides (ex: '54?' ou '54toto') plutôt
     *  que de les parser silencieusement.
     */
    ParsePosition pos = new ParsePosition(0);
    BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse(value, pos);
    if (pos.getIndex() < value.length()) {
      String trailing = value.substring(pos.getIndex());
      if (!StringUtils.isBlank(trailing)) {
        ConversionException e =
            new ConversionException(
                String.format("Failed parsing of '%1$s' at position %2$d", value, pos.getIndex()));
        e.setConverter(this);
        e.setVariable("value", value);
        e.setResourceKey("common.validator.percentage.parsing.error");
        throw e;
      }
    }

    if (modelIsRatio) {
      bigDecimal = bigDecimal.divide(new BigDecimal("100"));
    }
    bigDecimal = bigDecimal.setScale(scale, roundingMode);

    if (bigDecimal.precision() > maxPrecision) {
      ConversionException e =
          new ConversionException(
              "The precision of the BigDecimal instance exceeds the maximum allowed precision.");
      e.setConverter(this);
      e.setVariable(
          "maxBeforePoint", modelIsRatio ? (maxPrecision - scale + 2) : (maxPrecision - scale));
      e.setResourceKey("common.validator.percentage.precision.error");
      throw e;
    } else {
      return bigDecimal;
    }
  }

  @Override
  public String convertToString(BigDecimal value, Locale locale) {
    if (value == null) {
      return "";
    }
    value = value.setScale(scale, roundingMode);

    if (modelIsRatio) {
      value = value.multiply(new BigDecimal("100"));
    }

    if (value.precision() > maxPrecision) {
      throw new IllegalStateException(
          String.format(
              "The precision of the BigDecimal instance (%1$s) exceeds the maximum allowed precision, %2$s.",
              value.precision(), maxPrecision));
    } else {

      String result = new DecimalFormat(PATTERN, getDecimalFormatSymbols()).format(value);

      if (displayPercentSymbol) {
        result = result + "%";
      }
      return result;
    }
  }

  private DecimalFormatSymbols getDecimalFormatSymbols() {
    return new DecimalFormatSymbols(CoreApplication.get().getNumberFormatLocale());
  }
}
