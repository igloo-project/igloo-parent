package org.iglooproject.jpa.migration.rowmapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.iglooproject.jpa.migration.util.RowMapperUtils;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractRowMapper<T> implements RowMapper<T> {

  @Override
  public abstract T mapRow(ResultSet rs, int rowNum) throws SQLException;

  protected String nullIfEmpty(String text) {
    return RowMapperUtils.cleanLongText(text);
  }

  protected Integer nullIfZeroOrMinValue(Integer number) {
    return RowMapperUtils.nullIfZeroOrMinValue(number);
  }

  protected Long nullIfZero(Long number) {
    return RowMapperUtils.nullIfZero(number);
  }

  protected Double nullIfZero(Double number) {
    return RowMapperUtils.nullIfZero(number);
  }

  protected String getString(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getString(rs, columnLabel);
  }

  protected String getTrimmedString(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getTrimmedString(rs, columnLabel);
  }

  protected String getTrimmedStringToUpperCase(ResultSet rs, String columnLabel)
      throws SQLException {
    return RowMapperUtils.getTrimmedStringToUpperCase(rs, columnLabel);
  }

  protected Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getInteger(rs, columnLabel);
  }

  protected Integer getIntegerNotZeroNotMinValue(ResultSet rs, String columnLabel)
      throws SQLException {
    return RowMapperUtils.getIntegerNotZeroNotMinValue(rs, columnLabel);
  }

  protected Double getDouble(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getDouble(rs, columnLabel);
  }

  protected Double getDoubleNotZero(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getDoubleNotZero(rs, columnLabel);
  }

  protected Long getLong(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getLong(rs, columnLabel);
  }

  protected BigDecimal getBigDecimal(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getBigDecimal(rs, columnLabel);
  }

  protected BigDecimal getBigDecimal(
      ResultSet rs, String columnLabel, int scale, RoundingMode roundingMode) throws SQLException {
    return RowMapperUtils.getBigDecimal(rs, columnLabel, scale, roundingMode);
  }

  protected BigDecimal getBigDecimalNotZero(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getBigDecimalNotZero(rs, columnLabel);
  }

  protected BigDecimal getBigDecimalNotZero(
      ResultSet rs, String columnLabel, int scale, RoundingMode roundingMode) throws SQLException {
    return RowMapperUtils.getBigDecimalNotZero(rs, columnLabel, scale, roundingMode);
  }

  protected BigDecimal getBigDecimalGreaterEqualsZero(ResultSet rs, String columnLabel)
      throws SQLException {
    return RowMapperUtils.getBigDecimalGreaterEqualsZero(rs, columnLabel);
  }

  protected BigDecimal getBigDecimalGreaterEqualsZero(
      ResultSet rs, String columnLabel, int scale, RoundingMode roundingMode)
      throws SQLException, ArithmeticException {
    return RowMapperUtils.getBigDecimalGreaterEqualsZero(rs, columnLabel, scale, roundingMode);
  }

  protected BigDecimal getBigDecimalPercentageAsRate(ResultSet rs, String columnLabel)
      throws SQLException {
    return RowMapperUtils.getBigDecimalPercentageAsRate(rs, columnLabel);
  }

  protected Date getDate(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getDate(rs, columnLabel);
  }

  protected Date getDateTimestamp(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getDateTimestamp(rs, columnLabel);
  }

  protected Boolean getBooleanNotNull(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getBooleanNotNull(rs, columnLabel);
  }

  protected Boolean getBoolean(ResultSet rs, String columnLabel) throws SQLException {
    return RowMapperUtils.getBoolean(rs, columnLabel);
  }
}
