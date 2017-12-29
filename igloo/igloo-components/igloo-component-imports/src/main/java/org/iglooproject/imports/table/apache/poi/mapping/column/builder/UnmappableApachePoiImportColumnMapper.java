package org.iglooproject.imports.table.apache.poi.mapping.column.builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.base.Function;

import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;

/*package*/ final class UnmappableApachePoiImportColumnMapper  implements ITableImportColumnMapper<Sheet, Row, Cell, CellReference> {

	@Override
	public Function<? super Row, CellReference> tryMap(Sheet sheet, ITableImportNavigator<Sheet, Row, Cell, CellReference> navigator, ITableImportEventHandler eventHandler) {
		return null;
	}

}
