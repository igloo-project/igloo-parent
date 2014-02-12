package fr.openwide.core.imports.excel.poi.mapping.column.builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;

/*package*/ final class ApachePoiUnmappableExcelImportColumnMapper  implements IExcelImportColumnMapper<Sheet, Row, Cell> {

	@Override
	public Function<? super Row, Cell> map(Sheet sheet, IExcelImportNavigator<Sheet, Row, Cell> navigator, IExcelImportEventHandler eventHandler) {
		return Functions.constant(null);
	}

}
