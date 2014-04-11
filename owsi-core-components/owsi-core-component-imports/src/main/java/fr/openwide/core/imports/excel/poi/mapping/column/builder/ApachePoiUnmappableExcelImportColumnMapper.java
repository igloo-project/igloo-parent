package fr.openwide.core.imports.excel.poi.mapping.column.builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.base.Function;

import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;

/*package*/ final class ApachePoiUnmappableExcelImportColumnMapper  implements IExcelImportColumnMapper<Sheet, Row, Cell, CellReference> {

	@Override
	public Function<? super Row, CellReference> tryMap(Sheet sheet, IExcelImportNavigator<Sheet, Row, Cell, CellReference> navigator, IExcelImportEventHandler eventHandler) {
		return null;
	}

}
