package fr.openwide.core.imports.excel.poi.mapping.column.builder;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.exception.ExcelImportHeaderLabelMappingException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;
import fr.openwide.core.imports.excel.mapping.column.builder.MappingConstraint;

/*package*/ class ApachePoiHeaderLabelExcelImportColumnMapper implements IExcelImportColumnMapper<Sheet, Row, Cell, CellReference> {
	
	private final String expectedHeaderLabel;
	
	private final Predicate<? super String> predicate;

	private final int indexAmongMatchedColumns;
	
	private final MappingConstraint mappingConstraint;

	/**
	 * @param indexAmongMatchedColumns The 0-based index of this column among the columns matching the given <code>predicate</code>.
	 */
	public ApachePoiHeaderLabelExcelImportColumnMapper(String expectedHeaderLabel, Predicate<? super String> predicate,
			int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		super();
		Validate.notNull(predicate, "predicate must not be null");
		
		this.expectedHeaderLabel = expectedHeaderLabel;
		this.predicate = predicate;
		this.indexAmongMatchedColumns = indexAmongMatchedColumns;
		this.mappingConstraint = mappingConstraint;
	}
	
	@Override
	public Function<? super Row, CellReference> tryMap(Sheet sheet, IExcelImportNavigator<Sheet, Row, Cell, CellReference> navigator, IExcelImportEventHandler eventHandler) throws ExcelImportHeaderLabelMappingException {
		int matchedColumnsCount = 0;
		Row headersRow = sheet.getRow(sheet.getFirstRowNum());
		
		if (headersRow != null) {
			Iterator<Cell> iterator = headersRow.cellIterator();
			
			while (iterator.hasNext()) {
				Cell cell = iterator.next();
				String cellValue = StringUtils.trimToNull(cell.getStringCellValue());
				if (predicate.apply(cellValue)) {
					if (matchedColumnsCount == indexAmongMatchedColumns) {
						final int index = cell.getColumnIndex();
						return new SerializableFunction<Row, CellReference>() {
							private static final long serialVersionUID = 1L;
							@Override
							public CellReference apply(Row row) {
								return row == null ? null : new CellReference(row.getRowNum(), index);
							}
						};
					} else {
						++matchedColumnsCount;
					}
				}
			}
		}
		
		// Could not map the header to a column index
		if (MappingConstraint.REQUIRED.equals(mappingConstraint)) {
			eventHandler.headerLabelMappingError(expectedHeaderLabel, indexAmongMatchedColumns, navigator.getLocation(sheet, headersRow, null));
		}
		
		return null;
	}
}
