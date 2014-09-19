package fr.openwide.core.imports.table.opencsv.mapping.column.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.imports.table.common.event.ITableImportEventHandler;
import fr.openwide.core.imports.table.common.event.exception.TableImportHeaderLabelMappingException;
import fr.openwide.core.imports.table.common.location.ITableImportNavigator;
import fr.openwide.core.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import fr.openwide.core.imports.table.common.mapping.column.builder.MappingConstraint;
import fr.openwide.core.imports.table.opencsv.model.CsvCell;
import fr.openwide.core.imports.table.opencsv.model.CsvCellReference;
import fr.openwide.core.imports.table.opencsv.model.CsvRow;
import fr.openwide.core.imports.table.opencsv.model.CsvTable;

/*package*/ class HeaderLabelOpenCsvImportColumnMapper implements ITableImportColumnMapper<CsvTable, CsvRow, CsvCell, CsvCellReference> {
	
	private final String expectedHeaderLabel;
	
	private final Predicate<? super String> predicate;

	private final int indexAmongMatchedColumns;
	
	private final MappingConstraint mappingConstraint;

	/**
	 * @param indexAmongMatchedColumns The 0-based index of this column among the columns matching the given <code>predicate</code>.
	 */
	public HeaderLabelOpenCsvImportColumnMapper(String expectedHeaderLabel, Predicate<? super String> predicate,
			int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		super();
		Validate.notNull(predicate, "predicate must not be null");
		
		this.expectedHeaderLabel = expectedHeaderLabel;
		this.predicate = predicate;
		this.indexAmongMatchedColumns = indexAmongMatchedColumns;
		this.mappingConstraint = mappingConstraint;
	}
	
	@Override
	public Function<? super CsvRow, CsvCellReference> tryMap(CsvTable sheet, ITableImportNavigator<CsvTable, CsvRow, CsvCell, CsvCellReference> navigator,
			ITableImportEventHandler eventHandler) throws TableImportHeaderLabelMappingException {
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
