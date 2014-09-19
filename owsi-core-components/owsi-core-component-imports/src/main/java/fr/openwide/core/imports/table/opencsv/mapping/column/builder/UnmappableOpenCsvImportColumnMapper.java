package fr.openwide.core.imports.table.opencsv.mapping.column.builder;

import com.google.common.base.Function;

import fr.openwide.core.imports.table.common.event.ITableImportEventHandler;
import fr.openwide.core.imports.table.common.location.ITableImportNavigator;
import fr.openwide.core.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import fr.openwide.core.imports.table.opencsv.model.CsvCell;
import fr.openwide.core.imports.table.opencsv.model.CsvCellReference;
import fr.openwide.core.imports.table.opencsv.model.CsvRow;
import fr.openwide.core.imports.table.opencsv.model.CsvTable;

/*package*/ final class UnmappableOpenCsvImportColumnMapper implements ITableImportColumnMapper<CsvTable, CsvRow, CsvCell, CsvCellReference> {

	@Override
	public Function<? super CsvRow, CsvCellReference> tryMap(CsvTable sheet, ITableImportNavigator<CsvTable, CsvRow, CsvCell, CsvCellReference> navigator,
			ITableImportEventHandler eventHandler) {
		return null;
	}

}
