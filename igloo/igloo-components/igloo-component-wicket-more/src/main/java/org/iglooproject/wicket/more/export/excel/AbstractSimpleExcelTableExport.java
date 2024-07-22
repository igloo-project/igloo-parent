package org.iglooproject.wicket.more.export.excel;

import com.google.common.base.Joiner;
import igloo.wicket.model.CountMessageModel;
import java.util.Locale;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Classes;
import org.iglooproject.export.excel.AbstractExcelTableExport;
import org.iglooproject.spring.util.StringUtils;

/**
 * Apporte quelques configurations de style ainsi que le support de l'extraction de traduction via
 * wicket.
 */
public abstract class AbstractSimpleExcelTableExport extends AbstractExcelTableExport {

  protected static final Joiner DEFAULT_JOINER = Joiner.on("\n").skipNulls();

  private final Component component;

  public AbstractSimpleExcelTableExport(Component component) {
    this(new XSSFWorkbook(), component);
  }

  public AbstractSimpleExcelTableExport(Workbook workbook, Component component) {
    super(workbook);
    this.component = component;
    init();
  }

  @Override
  protected Sheet createSheet(String title) {
    Sheet sheet = super.createSheet(title);
    sheet.setDefaultRowHeight((short) 450);
    return sheet;
  }

  protected Locale getLocale() {
    return component.getLocale();
  }

  @Override
  protected String localize(String key) {
    return component.getString(key);
  }

  protected String localize(String key, IModel<?> model) {
    return component.getString(key, model);
  }

  protected String localize(String resourceKey, IModel<?> model, Object... positionalParameters) {
    return new StringResourceModel(resourceKey, component, model)
        .setParameters(positionalParameters)
        .getObject();
  }

  protected String localize(Enum<?> enumValue) {
    return localize(enumValue, null, null);
  }

  protected <E> String localize(Enum<?> enumValue, String prefix, String suffix) {
    if (enumValue == null) {
      return null;
    }

    StringBuilder key = new StringBuilder();

    if (StringUtils.hasText(prefix)) {
      key.append(prefix).append(".");
    }

    key.append(Classes.simpleName(enumValue.getDeclaringClass()))
        .append(".")
        .append(enumValue.name());

    if (StringUtils.hasText(suffix)) {
      key.append(".").append(suffix);
    }

    return localize(key.toString());
  }

  protected <E> String localize(Class<E> clazz, E value) {
    return Application.get()
        .getConverterLocator()
        .getConverter(clazz)
        .convertToString(value, getLocale());
  }

  protected <N extends Number> String localizeCount(String resourceKey, N value) {
    return new CountMessageModel(resourceKey, Model.of(value)).getObject();
  }

  @Override
  protected void initColors() {
    setHeaderBackgroundColor("#0062CC");
    setHeaderFontColor("#FFFFFF");
    super.initColors();
  }

  @Override
  protected void initFonts() {
    setFontName("Calibri");
    super.initFonts();
  }

  @Override
  protected void initStyles() {
    super.initStyles();

    CellStyle header = getStyle(STYLE_HEADER_NAME);
    header.setWrapText(true);
    CellStyle standardOdd = getStyle(STYLE_STANDARD_NAME + ROW_ODD_NAME);
    standardOdd.setWrapText(true);
    CellStyle standardEven = getStyle(STYLE_STANDARD_NAME + ROW_EVEN_NAME);
    standardEven.setWrapText(true);
  }

  @Override
  protected void finalizeSheet(Sheet sheet, boolean landscapePrintSetup) {
    super.finalizeSheet(sheet, landscapePrintSetup);
    sheet.setAutobreaks(true);
    sheet.getPrintSetup().setFitWidth((short) 1);
    sheet.getPrintSetup().setFitHeight((short) 10000);
  }

  @Override
  protected void addIterableTextCell(Row row, int columnIndex, Iterable<String> iterable) {
    addTextCell(row, columnIndex, DEFAULT_JOINER.join(iterable));
  }
}
