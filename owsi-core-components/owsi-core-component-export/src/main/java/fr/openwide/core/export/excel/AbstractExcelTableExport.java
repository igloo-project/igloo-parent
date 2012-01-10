/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.openwide.core.export.excel;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <p>Classe abstraite permettant de construire des tableaux Excel.</p>
 *
 * @author Open Wide
 */
public abstract class AbstractExcelTableExport extends AbstractExcelExport {
	
	/**
	 * Index de la couleur des bordures
	 */
	protected static final short BORDER_COLOR_INDEX = (short) 35;

	/**
	 * Index de la couleur de fond du header
	 */
	protected static final short HEADER_BACKGROUND_COLOR_INDEX = (short) 36;

	/**
	 * Index de la couleur du texte du header
	 */
	protected static final short HEADER_FONT_COLOR_INDEX = (short) 37;

	/**
	 * Index de la couleur des lignes paires
	 */
	protected static final short EVEN_ROW_BACKGROUND_COLOR_INDEX = (short) 38;

	protected static final String FONT_NORMAL_NAME = "fontNormal";
	protected static final String FONT_HEADER_NAME = "fontHeader";
	protected static final String ROW_ODD_NAME = "Odd";
	protected static final String ROW_EVEN_NAME = "Even";
	protected static final String STYLE_DEFAULT_NAME = "default";
	protected static final String STYLE_HEADER_NAME = "header";
	protected static final String STYLE_STANDARD_NAME = "standard";
	protected static final String STYLE_NUMERIC_NAME = "numeric";
	protected static final String STYLE_DATE_NAME = "date";
	protected static final String STYLE_DATE_TIME_NAME = "datetime";
	protected static final String STYLE_PERCENT_NAME = "percent";

	/**
	 * Police utilisée dans le document
	 */
	private String fontName = "Verdana";
	
	/**
	 * Taille de police utilisée dans le corps d'une table
	 */
	private short normalFontHeight = 9;
	
	/**
	 * Taille de police utilisée dans le header d'une table
	 */
	private short headerFontHeight = 9;

	/**
	 * Couleur des bordures
	 */
	private String borderColor = "#D1D1D1";

	/**
	 * Couleur de fond du header
	 */
	private String headerBackgroundColor = "#007CAF";

	/**
	 * Couleur du texte du header
	 */
	private String headerFontColor = "#000000";

	/**
	 * Couleur des lignes paires
	 */
	private String evenRowBackgroundColor = "#EEEEEE";
	
	/**
	 * Format des données numériques
	 */
	private String numericDataFormat = "# ### ### ### ###";
	
	/**
	 * Format des dates
	 */
	private String dateDataFormat = "DD/MM/YYYY";
	
	/**
	 * Format des dates avec heure
	 */
	private String dateTimeDataFormat = "DD/MM/YYYY HH:mm";
	
	/**
	 * Format des pourcentages
	 */
	private String percentDataFormat = "0.00%";
	
	/**
	 * Constructeur
	 */
	public AbstractExcelTableExport(Workbook workbook) {
		super(workbook);
	}

	/**
	 * Initilisation générale
	 */
	public void init() {
		initColors();
		initFonts();
		initStyles();
	}
	
	/**
	 * Initialisation des couleurs
	 */
	protected void initColors() {
		registerColor(BORDER_COLOR_INDEX, borderColor);
		registerColor(HEADER_BACKGROUND_COLOR_INDEX, headerBackgroundColor);
		registerColor(HEADER_FONT_COLOR_INDEX, headerFontColor);
		registerColor(EVEN_ROW_BACKGROUND_COLOR_INDEX, evenRowBackgroundColor);
	}

	/**
	 * Initialisation des polices
	 */
	protected void initFonts() {
		Font fontHeader = workbook.createFont();
		fontHeader.setFontHeightInPoints(getHeaderFontHeight());
		fontHeader.setFontName(getFontName());
		fontHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
		setFontColor(fontHeader, colorRegistry, HEADER_FONT_COLOR_INDEX);
		registerFont(FONT_HEADER_NAME, fontHeader);

		Font fontNormal = workbook.createFont();
		fontNormal.setFontHeightInPoints(getNormalFontHeight());
		fontNormal.setFontName(getFontName());
		registerFont(FONT_NORMAL_NAME, fontNormal);
	}
	
	/**
	 * Initialisation des styles de cellule
	 */
	protected void initStyles() {
		CellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setFont(getFont(FONT_NORMAL_NAME));
		setStyleFillForegroundColor(defaultStyle, colorRegistry, HSSFColor.WHITE.index);
		defaultStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		defaultStyle.setBorderBottom(CellStyle.BORDER_THIN);
		setStyleBottomBorderColor(defaultStyle, colorRegistry, BORDER_COLOR_INDEX);
		defaultStyle.setBorderLeft(CellStyle.BORDER_THIN);
		setStyleLeftBorderColor(defaultStyle, colorRegistry, BORDER_COLOR_INDEX);
		defaultStyle.setBorderRight(CellStyle.BORDER_THIN);
		setStyleRightBorderColor(defaultStyle, colorRegistry, BORDER_COLOR_INDEX);
		defaultStyle.setBorderTop(CellStyle.BORDER_THIN);
		setStyleTopBorderColor(defaultStyle, colorRegistry, BORDER_COLOR_INDEX);
		defaultStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		registerStyle(STYLE_DEFAULT_NAME, defaultStyle);

		CellStyle styleHeader = workbook.createCellStyle();
		styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setFont(getFont(FONT_HEADER_NAME));
		setStyleFillForegroundColor(styleHeader, colorRegistry, HEADER_BACKGROUND_COLOR_INDEX);
		styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		setStyleBottomBorderColor(styleHeader, colorRegistry, BORDER_COLOR_INDEX);
		styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		setStyleLeftBorderColor(styleHeader, colorRegistry, BORDER_COLOR_INDEX);
		styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		setStyleRightBorderColor(styleHeader, colorRegistry, BORDER_COLOR_INDEX);
		styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		setStyleTopBorderColor(styleHeader, colorRegistry, BORDER_COLOR_INDEX);
		styleHeader.setDataFormat((short) 0);
		registerStyle(STYLE_HEADER_NAME, styleHeader);

		CellStyle styleOdd = cloneStyle(defaultStyle);
		registerStyle(STYLE_STANDARD_NAME + ROW_ODD_NAME, styleOdd);

		CellStyle styleEven = cloneStyle(styleOdd);
		setStyleFillForegroundColor(styleEven, colorRegistry, EVEN_ROW_BACKGROUND_COLOR_INDEX);
		registerStyle(STYLE_STANDARD_NAME + ROW_EVEN_NAME, styleEven);

		// Styles pour les nombres
		short numericFormatIndex = dataFormat.getFormat(numericDataFormat);

		CellStyle styleOddNumeric = cloneStyle(styleOdd);
		styleOddNumeric.setAlignment(CellStyle.ALIGN_RIGHT);
		styleOddNumeric.setDataFormat(numericFormatIndex);
		registerStyle(STYLE_NUMERIC_NAME + ROW_ODD_NAME, styleOddNumeric);

		CellStyle styleEvenNumeric = cloneStyle(styleEven);
		styleEvenNumeric.setAlignment(CellStyle.ALIGN_RIGHT);
		styleEvenNumeric.setDataFormat(numericFormatIndex);
		registerStyle(STYLE_NUMERIC_NAME + ROW_EVEN_NAME, styleEvenNumeric);

		// styles pour les dates
		short dateFormatIndex = dataFormat.getFormat(dateDataFormat);

		CellStyle styleOddDate = cloneStyle(styleOdd);
		styleOddDate.setDataFormat(dateFormatIndex);
		registerStyle(STYLE_DATE_NAME + ROW_ODD_NAME, styleOddDate);

		CellStyle styleEvenDate = cloneStyle(styleEven);
		styleEvenDate.setDataFormat(dateFormatIndex);
		registerStyle(STYLE_DATE_NAME + ROW_EVEN_NAME, styleEvenDate);

		// styles pour les dates avec heure
		short dateTimeFormatIndex = dataFormat.getFormat(dateTimeDataFormat);
		
		CellStyle styleOddDateTime = cloneStyle(styleOdd);
		styleOddDateTime.setDataFormat(dateTimeFormatIndex);
		registerStyle(STYLE_DATE_TIME_NAME + ROW_ODD_NAME, styleOddDateTime);
		
		CellStyle styleEvenDateTime = cloneStyle(styleEven);
		styleEvenDateTime.setDataFormat(dateTimeFormatIndex);
		registerStyle(STYLE_DATE_TIME_NAME + ROW_EVEN_NAME, styleEvenDateTime);
		
		// styles pour les pourcentages
		short percentFormatIndex = dataFormat.getFormat(percentDataFormat);

		CellStyle styleOddPercent = cloneStyle(styleOdd);
		styleOddPercent.setDataFormat(percentFormatIndex);
		registerStyle(STYLE_PERCENT_NAME + ROW_ODD_NAME, styleOddPercent);

		CellStyle styleEvenPercent = cloneStyle(styleEven);
		styleEvenPercent.setDataFormat(percentFormatIndex);
		registerStyle(STYLE_PERCENT_NAME + ROW_EVEN_NAME, styleEvenPercent);
	}
	
	/**
	 * Ajoute une cellule texte.
	 * 
	 * @param row ligne
	 * @param columnIndex numéro de la colonne
	 * @param text texte à insérer dans la cellule
	 * @return cellule
	 */
	protected Cell addTextCell(Row row, int columnIndex, String text) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellStyle(getRowStyle(STYLE_STANDARD_NAME, row.getRowNum()));
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(creationHelper.createRichTextString(text));

		return cell;
	}

	/**
	 * Ajoute une cellule d'en-tête.
	 * 
	 * @param row ligne
	 * @param columnIndex numéro de la colonne
	 * @param text texte à insérer dans la cellule
	 * @return cellule
	 */
	protected Cell addHeaderCell(Row row, int columnIndex, String text) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellStyle(getStyle(STYLE_HEADER_NAME));
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(creationHelper.createRichTextString(text));

		return cell;
	}

	/**
	 * Ajoute une cellule au format date.
	 * 
	 * @param row ligne
	 * @param columnIndex numéro de la colonne
	 * @param date date à insérer dans la cellule
	 * @return cellule
	 */
	protected Cell addDateCell(Row row, int columnIndex, Date date) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellStyle(getRowStyle(STYLE_DATE_NAME, row.getRowNum()));
		if (date != null) {
			cell.setCellValue(date);
		}

		return cell;
	}
	
	/**
	 * Ajoute une cellule au format date + heure.
	 * 
	 * @param row ligne
	 * @param columnIndex numéro de la colonne
	 * @param date date à insérer dans la cellule
	 * @return cellule
	 */
	protected Cell addDateTimeCell(Row row, int columnIndex, Date date) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellStyle(getRowStyle(STYLE_DATE_TIME_NAME, row.getRowNum()));
		if (date != null) {
			cell.setCellValue(date);
		}

		return cell;
	}

	/**
	 * Ajoute une cellule numérique.
	 * 
	 * @param row ligne
	 * @param columnIndex numéro de la colonne
	 * @param number nombre à insérer dans la cellule
	 * @return cellule
	 */
	protected Cell addNumericCell(Row row, int columnIndex, Number number) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(getRowStyle(STYLE_NUMERIC_NAME, row.getRowNum()));

		if (number != null) {
			cell.setCellValue(number.doubleValue());
		}

		return cell;
	}

	/**
	 * Ajoute une cellule avec des pourcentages.
	 * 
	 * @param row ligne
	 * @param columnIndex numéro de la colonne
	 * @param number nombre à insérer dans la cellule
	 * @return cellule
	 */
	protected Cell addPercentCell(Row row, int columnIndex, Number number) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(getRowStyle(STYLE_PERCENT_NAME, row.getRowNum()));
		
		if (number != null) {
			cell.setCellValue(number.doubleValue());
		}

		return cell;
	}
	
	/**
	 * Retourne le style de la ligne en tenant compte des alternances de
	 * couleurs pour les lignes paires/impaires.
	 * 
	 * @param prefix prefixe du nom du style auquel est ajouté Even ou Odd
	 * @param rowIndex numéro de la ligne
	 * @return style à utiliser pour la ligne
	 */
	protected CellStyle getRowStyle(String prefix, int rowIndex) {
		StringBuilder styleName = new StringBuilder(prefix);
		if (rowIndex % 2 == 0) {
			styleName.append(ROW_EVEN_NAME);
		} else {
			styleName.append(ROW_ODD_NAME);
		}
		return getStyle(styleName.toString());
	}
	
	/**
	 * Ajoute les en-têtes dans la feuille de calcul.
	 * 
	 * @param sheet feuille de calcul
	 * @param rowIndex numéro de la ligne
	 * @param headers en-têtes
	 */
	protected void addHeadersToSheet(Sheet sheet, int rowIndex, List<String> headers) {
		int columnIndex = 0;

		Row rowHeader = sheet.createRow(rowIndex);
		for (String header : headers) {
			addHeaderCell(rowHeader, columnIndex, getLocalizedLabel(header));
			columnIndex++;
		}
	}
	
	/**
	 * Finalise la création de la feuille de calcul, notamment en demandant le
	 * redimensionnement automatique des colonnes.
	 * 
	 * @param sheet feuilles de calcul
	 * @param headers en-têtes
	 */
	protected void finalizeSheet(Sheet sheet, List<String> headers) {
		int headersSize = headers.size();
		for (int i = 0; i < headersSize; i++) {
			sheet.autoSizeColumn((short) i);
		}
	}
	
	/**
	 * Retourne le message correspondant à la clé en fonction du Locale
	 * 
	 * @param key clé
	 * @return message
	 */
	protected abstract String getLocalizedLabel(String key);

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public short getNormalFontHeight() {
		return normalFontHeight;
	}

	public void setNormalFontHeight(short normalFontHeight) {
		this.normalFontHeight = normalFontHeight;
	}

	public short getHeaderFontHeight() {
		return headerFontHeight;
	}

	public void setHeaderFontHeight(short headerFontHeight) {
		this.headerFontHeight = headerFontHeight;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getHeaderBackgroundColor() {
		return headerBackgroundColor;
	}

	public void setHeaderBackgroundColor(String headerBackgroundColor) {
		this.headerBackgroundColor = headerBackgroundColor;
	}

	public String getHeaderFontColor() {
		return headerFontColor;
	}

	public void setHeaderFontColor(String headerFontColor) {
		this.headerFontColor = headerFontColor;
	}

	public String getEvenRowBackgroundColor() {
		return evenRowBackgroundColor;
	}

	public void setEvenRowBackgroundColor(String evenRoxBackgroundColor) {
		this.evenRowBackgroundColor = evenRoxBackgroundColor;
	}

	public String getNumericDataFormat() {
		return numericDataFormat;
	}

	public void setNumericDataFormat(String numericDataFormat) {
		this.numericDataFormat = numericDataFormat;
	}

	public String getDateDataFormat() {
		return dateDataFormat;
	}

	public void setDateDataFormat(String dateDataFormat) {
		this.dateDataFormat = dateDataFormat;
	}

	public String getDateTimeDataFormat() {
		return dateTimeDataFormat;
	}

	public void setDateTimeDataFormat(String dateTimeDataFormat) {
		this.dateTimeDataFormat = dateTimeDataFormat;
	}

	public String getPercentDataFormat() {
		return percentDataFormat;
	}

	public void setPercentDataFormat(String percentDataFormat) {
		this.percentDataFormat = percentDataFormat;
	}
}
