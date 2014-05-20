package fr.openwide.core.imports.csv.mapping.column.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.exception.ExcelImportHeaderLabelMappingException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;
import fr.openwide.core.imports.excel.mapping.column.builder.MappingConstraint;

/*package*/ class OpenCsvHeaderLabelExcelImportColumnMapper implements IExcelImportColumnMapper<CsvSheet, CsvRow, CsvCell, CsvCellReference> {
	
	private final String expectedHeaderLabel;
	
	private final Predicate<? super String> predicate;

	private final int indexAmongMatchedColumns;
	
	private final MappingConstraint mappingConstraint;

	/**
	 * @param indexAmongMatchedColumns The 0-based index of this column among the columns matching the given <code>predicate</code>.
	 */
	public OpenCsvHeaderLabelExcelImportColumnMapper(String expectedHeaderLabel, Predicate<? super String> predicate,
			int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		super();
		Validate.notNull(predicate, "predicate must not be null");
		
		this.expectedHeaderLabel = expectedHeaderLabel;
		this.predicate = predicate;
		this.indexAmongMatchedColumns = indexAmongMatchedColumns;
		this.mappingConstraint = mappingConstraint;
	}
	
	@Override
	public Function<? super CsvRow, CsvCellReference> tryMap(CsvSheet sheet, IExcelImportNavigator<CsvSheet, CsvRow, CsvCell, CsvCellReference> navigator,
			IExcelImportEventHandler eventHandler) throws ExcelImportHeaderLabelMappingException {
		int matchedColumnsCount = 0;
		CsvRow headersRow = sheet.getRow(0);
		
		if (headersRow != null) {
			for (CsvCell cell : headersRow) {
				String cellValue = StringUtils.trimToNull(cell.getContent());
				if (predicate.apply(cellValue)) {
					if (matchedColumnsCount == indexAmongMatchedColumns) {
						final int index = cell.getIndex();
						return new SerializableFunction<CsvRow, CsvCellReference>() {
							private static final long serialVersionUID = 1L;
							@Override
							public CsvCellReference apply(CsvRow row) {
								return row == null ? null : new CsvCellReference(row.getIndex(), index);
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
