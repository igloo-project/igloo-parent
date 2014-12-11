package fr.openwide.core.wicket.more.export.excel;

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

import com.google.common.base.Joiner;

import fr.openwide.core.export.excel.AbstractExcelTableExport;
import fr.openwide.core.wicket.markup.html.model.CountMessageModel;

/**
 * Apporte quelques configurations de style ainsi que le support de l'extraction de traduction via wicket.
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

	/**
	 * @deprecated Use {@link #localize(String)} instead.
	 */
	@Override
	@Deprecated
	protected String getLocalizedLabel(String key) {
		return localize(key);
	}
	
	protected Locale getLocale() {
		return component.getLocale();
	}
	
	protected String localize(String key) {
		return component.getString(key);
	}
	
	protected String localize(String key, IModel<?> model) {
		return component.getString(key, model);
	}
	
	protected String localize(String resourceKey, IModel<?> model, Object ... positionalParameters) {
		return new StringResourceModel(resourceKey, component, model, positionalParameters).getObject();
	}

	protected String localize(Enum<?> enumValue) {
		return enumValue == null ? null : localize(Classes.simpleName(enumValue.getDeclaringClass()) + "." + enumValue.name());
	}
	
	protected <E> String localize(Class<E> clazz, E value) {
		return Application.get().getConverterLocator().getConverter(clazz).convertToString(value, getLocale());
	}
	
	protected <N extends Number> String localizeCount(String resourceKey, N value) {
		return new CountMessageModel(resourceKey, Model.of(value)).getObject();
	}

	@Override
	protected void initColors() {
		setHeaderBackgroundColor("#009FE0");
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
	
	protected void addIterableTextCell(Row row, int columnIndex, Iterable<String> iterable) {
		addTextCell(row, columnIndex, DEFAULT_JOINER.join(iterable));
	}
}
