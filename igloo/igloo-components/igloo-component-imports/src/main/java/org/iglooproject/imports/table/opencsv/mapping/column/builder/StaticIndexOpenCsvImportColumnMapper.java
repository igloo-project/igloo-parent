package org.iglooproject.imports.table.opencsv.mapping.column.builder;

import com.google.common.base.Function;

import org.iglooproject.commons.util.functional.SerializableFunction;
import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import org.iglooproject.imports.table.opencsv.model.CsvCell;
import org.iglooproject.imports.table.opencsv.model.CsvCellReference;
import org.iglooproject.imports.table.opencsv.model.CsvRow;
import org.iglooproject.imports.table.opencsv.model.CsvTable;

/*package*/ class StaticIndexOpenCsvImportColumnMapper implements ITableImportColumnMapper<CsvTable, CsvRow, CsvCell, CsvCellReference> {
	
	private final int columnIndex;

	public StaticIndexOpenCsvImportColumnMapper(int columnIndex) {
		super();
		
		this.columnIndex = columnIndex;
	}
	
	@Override
	public Function<? super CsvRow, CsvCellReference> tryMap(CsvTable sheet, ITableImportNavigator<CsvTable, CsvRow, CsvCell, CsvCellReference> navigator,
			ITableImportEventHandler eventHandler) {
		return new SerializableFunction<CsvRow, CsvCellReference>() {
			private static final long serialVersionUID = 1L;
			@Override
			public CsvCellReference apply(CsvRow row) {
				return row == null ? null : new CsvCellReference(row.getIndex(), columnIndex);
			}
		};
	}
}
