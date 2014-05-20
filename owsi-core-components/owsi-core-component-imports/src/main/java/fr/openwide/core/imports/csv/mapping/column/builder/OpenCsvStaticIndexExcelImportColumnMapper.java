package fr.openwide.core.imports.csv.mapping.column.builder;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;

/*package*/ class OpenCsvStaticIndexExcelImportColumnMapper implements IExcelImportColumnMapper<CsvSheet, CsvRow, CsvCell, CsvCellReference> {
	
	private final int columnIndex;

	public OpenCsvStaticIndexExcelImportColumnMapper(int columnIndex) {
		super();
		
		this.columnIndex = columnIndex;
	}
	
	@Override
	public Function<? super CsvRow, CsvCellReference> tryMap(CsvSheet sheet, IExcelImportNavigator<CsvSheet, CsvRow, CsvCell, CsvCellReference> navigator,
			IExcelImportEventHandler eventHandler) {
		return new SerializableFunction<CsvRow, CsvCellReference>() {
			private static final long serialVersionUID = 1L;
			@Override
			public CsvCellReference apply(CsvRow row) {
				return row == null ? null : new CsvCellReference(row.getIndex(), columnIndex);
			}
		};
	}
}
