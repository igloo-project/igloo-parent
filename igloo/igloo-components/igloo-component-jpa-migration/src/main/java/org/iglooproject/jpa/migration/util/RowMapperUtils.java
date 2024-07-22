package org.iglooproject.jpa.migration.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public final class RowMapperUtils {

  public static String cleanLongText(String text) {
    if (text != null) {
      String trimmedText = text.trim();
      if (trimmedText.length() > 0) {
        return trimmedText;
      }
    }

    return null;
  }

  public static String nullIfEmpty(String text) {
    return cleanLongText(text);
  }

  public static Integer nullIfZeroOrMinValue(Integer number) {
    if (number == null || number == 0) {
      return null;
    } else if (number == Integer.MIN_VALUE || number == Short.MIN_VALUE) {
      return null;
    } else {
      return number;
    }
  }

  public static Long nullIfZero(Long number) {
    if (number == null || number == 0) {
      return null;
    } else {
      return number;
    }
  }

  public static Double nullIfZero(Double number) {
    if (number == null || number == 0) {
      return null;
    } else {
      return number;
    }
  }

  public static String getString(ResultSet rs, String columnLabel) throws SQLException {
    return rs.getString(columnLabel);
  }

  public static String getTrimmedString(ResultSet rs, String columnLabel) throws SQLException {
    return nullIfEmpty(getString(rs, columnLabel));
  }

  public static String getTrimmedStringToUpperCase(ResultSet rs, String columnLabel)
      throws SQLException {
    return getString(rs, columnLabel) != null
        ? nullIfEmpty(getString(rs, columnLabel).toUpperCase())
        : null;
  }

  public static Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
    int value = rs.getInt(columnLabel);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
    }
  }

  public static Integer getIntegerNotZeroNotMinValue(ResultSet rs, String columnLabel)
      throws SQLException {
    return nullIfZeroOrMinValue(getInteger(rs, columnLabel));
  }

  public static Double getDouble(ResultSet rs, String columnLabel) throws SQLException {
    double value = rs.getDouble(columnLabel);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
    }
  }

  public static Double getDoubleNotZero(ResultSet rs, String columnLabel) throws SQLException {
    return nullIfZero(getDouble(rs, columnLabel));
  }

  public static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
    long value = rs.getLong(columnLabel);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
    }
  }

  public static BigDecimal getBigDecimal(ResultSet rs, String columnLabel) throws SQLException {
    BigDecimal bigDecimal = rs.getBigDecimal(columnLabel);
    if (bigDecimal != null) {
      return bigDecimal;
    } else {
      return null;
    }
  }

  public static BigDecimal getBigDecimal(
      ResultSet rs, String columnLabel, int scale, RoundingMode roundingMode)
      throws SQLException, ArithmeticException {
    BigDecimal bigDecimal = rs.getBigDecimal(columnLabel);
    if (bigDecimal != null) {
      return bigDecimal.setScale(scale, roundingMode);
    } else {
      return null;
    }
  }

  /**
   * Récupére une valeur issue de la base ; permet de ne pas prendre en compte certaines valeurs
   * spéciales présentes dans la base. Si la valeur est zéro, on remplace par null.
   *
   * @param bigDecimal
   * @return
   * @throws SQLException
   */
  public static BigDecimal getBigDecimalNotZero(ResultSet rs, String columnLabel)
      throws SQLException {
    BigDecimal bigDecimal = rs.getBigDecimal(columnLabel);
    if (bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
      return null;
    } else {
      return bigDecimal;
    }
  }

  public static BigDecimal getBigDecimalNotZero(
      ResultSet rs, String columnLabel, int scale, RoundingMode roundingMode) throws SQLException {
    BigDecimal bigDecimal = rs.getBigDecimal(columnLabel);
    if (bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
      return null;
    } else {
      return bigDecimal.setScale(scale, roundingMode);
    }
  }

  public static BigDecimal getBigDecimalGreaterEqualsZero(ResultSet rs, String columnLabel)
      throws SQLException {
    BigDecimal bigDecimal = rs.getBigDecimal(columnLabel);

    if (bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
      return null;
    } else {
      return bigDecimal;
    }
  }

  public static BigDecimal getBigDecimalGreaterEqualsZero(
      ResultSet rs, String columnLabel, int scale, RoundingMode roundingMode)
      throws SQLException, ArithmeticException {
    BigDecimal bigDecimal = rs.getBigDecimal(columnLabel);

    if (bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
      return null;
    } else {
      return bigDecimal.setScale(scale, roundingMode);
    }
  }

  public static BigDecimal getBigDecimalPercentageAsRate(ResultSet rs, String columnLabel)
      throws SQLException {
    BigDecimal value = RowMapperUtils.getBigDecimalGreaterEqualsZero(rs, columnLabel);
    if (value != null) {
      return value.compareTo(BigDecimal.ZERO) == 0
          ? BigDecimal.ZERO
          : value.divide(new BigDecimal(100));
    } else {
      return null;
    }
  }

  public static Date getDate(ResultSet rs, String columnLabel) throws SQLException {
    return rs.getDate(columnLabel);
  }

  public static Date getDateTimestamp(ResultSet rs, String columnLabel) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnLabel);
    if (timestamp == null) {
      return null;
    } else {
      return new Date(timestamp.getTime());
    }
  }

  public static Boolean getBooleanNotNull(ResultSet rs, String columnLabel) throws SQLException {
    // le contrat de rs.getBoolean() correspond à ce qu'on veut true si 1 / true / ... sinon false
    return rs.getBoolean(columnLabel);
  }

  public static Boolean getBoolean(ResultSet rs, String columnLabel) throws SQLException {
    String booleanString = getTrimmedString(rs, columnLabel);
    if ("1".equals(booleanString)) {
      return Boolean.TRUE;
    } else if ("0".equals(booleanString)) {
      return Boolean.FALSE;
    } else {
      return null;
    }
  }
}
