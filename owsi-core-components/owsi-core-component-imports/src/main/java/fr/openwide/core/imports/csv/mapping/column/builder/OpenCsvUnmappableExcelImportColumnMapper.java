package fr.openwide.core.imports.csv.mapping.column.builder;

import com.google.common.base.Function;

import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;

/*package*/ final class OpenCsvUnmappableExcelImportColumnMapper implements IExcelImportColumnMapper<CsvSheet, CsvRow, CsvCell, CsvCellReference> {

	@Override
	public Function<? super CsvRow, CsvCellReference> tryMap(CsvSheet sheet, IExcelImportNavigator<CsvSheet, CsvRow, CsvCell, CsvCellReference> navigator,
			IExcelImportEventHandler eventHandler) {
		return null;
	}

}
