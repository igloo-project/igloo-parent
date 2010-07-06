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
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <p>Classe abstraite permettant des tableaux Excel.</p>
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

	/**
	 * Police utilisée dans le document
	 */
	protected String fontName = "Verdana";
	
	/**
	 * Taille de police utilisée dans le corps d'une table
	 */
	protected short normalFontHeight = 9;
	
	/**
	 * Taille de police utilisée dans le header d'une table
	 */
	protected short headerFontHeight = 9;

	/**
	 * Couleur des bordures
	 */
	protected String borderColor = "#D1D1D1";

	/**
	 * Couleur de fond du header
	 */
	protected String headerBackgroundColor = "#007CAF";

	/**
	 * Couleur du texte du header
	 */
	protected String headerFontColor = "#000000";

	/**
	 * Couleur des lignes paires
	 */
	protected String evenRowBackgroundColor = "#EEEEEE";
	
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
		registerFont("fontHeader", fontHeader);

		Font fontNormal = workbook.createFont();
		fontNormal.setFontHeightInPoints(getNormalFontHeight());
		fontNormal.setFontName(getFontName());
		registerFont("fontNormal", fontNormal);
	}
	
	/**
	 * Initialisation des styles de cellule
	 */
	protected void initStyles() {
		CellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setFont(getFont("fontNormal"));
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
		registerStyle("default", defaultStyle);

		CellStyle styleHeader = workbook.createCellStyle();
		styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setFont(getFont("fontHeader"));
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
		registerStyle("header", styleHeader);

		CellStyle styleOdd = cloneStyle(defaultStyle);
		registerStyle("standardOdd", styleOdd);

		CellStyle styleEven = cloneStyle(styleOdd);
		setStyleFillForegroundColor(styleEven, colorRegistry, EVEN_ROW_BACKGROUND_COLOR_INDEX);
		registerStyle("standardEven", styleEven);

		// Styles pour les nombres
		DataFormat numericFormat = creationHelper.createDataFormat();
		short numericFormatIndex = numericFormat.getFormat("# ### ### ### ###");

		CellStyle styleOddNumeric = cloneStyle(styleOdd);
		styleOddNumeric.setAlignment(CellStyle.ALIGN_RIGHT);
		styleOddNumeric.setDataFormat(numericFormatIndex);
		registerStyle("numericOdd", styleOddNumeric);

		CellStyle styleEvenNumeric = cloneStyle(styleEven);
		styleEvenNumeric.setAlignment(CellStyle.ALIGN_RIGHT);
		styleEvenNumeric.setDataFormat(numericFormatIndex);
		registerStyle("numericEven", styleEvenNumeric);

		// styles pour les dates
		DataFormat dateFormat = creationHelper.createDataFormat();
		short dateFormatIndex = dateFormat.getFormat("DD/MM/YYYY");

		CellStyle styleOddDate = cloneStyle(styleOdd);
		styleOddDate.setDataFormat(dateFormatIndex);
		registerStyle("dateOdd", styleOddDate);

		CellStyle styleEvenDate = cloneStyle(styleEven);
		styleEvenDate.setDataFormat(dateFormatIndex);
		registerStyle("dateEven", styleEvenDate);

		// styles pour les pourcentages
		DataFormat percentFormat = creationHelper.createDataFormat();
		short percentFormatIndex = percentFormat.getFormat("0.00%");

		CellStyle styleOddPercent = cloneStyle(styleOdd);
		styleOddPercent.setDataFormat(percentFormatIndex);
		registerStyle("percentOdd", styleOddPercent);

		CellStyle styleEvenPercent = cloneStyle(styleEven);
		styleEvenPercent.setDataFormat(percentFormatIndex);
		registerStyle("percentEven", styleEvenPercent);
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
		cell.setCellStyle(getRowStyle("standard", row.getRowNum()));
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
		cell.setCellStyle(getStyle("header"));
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
		cell.setCellStyle(getRowStyle("date", row.getRowNum()));
		cell.setCellValue(date);

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
		cell.setCellStyle(getRowStyle("numeric", row.getRowNum()));

		cell.setCellValue(number.doubleValue());

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
		cell.setCellStyle(getRowStyle("percent", row.getRowNum()));
		
		cell.setCellValue(number.doubleValue());

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
			styleName.append("Even");
		} else {
			styleName.append("Odd");
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
			header = getLocalizedLabel(header);
			addHeaderCell(rowHeader, columnIndex, header);
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

	/**
	 * Retourne le message correspondant à la clé en fonction du Locale
	 * 
	 * @param key clé
	 * @return message
	 */
	protected abstract String getLocalizedLabel(String key);
}
