package fr.openwide.core.imports.excel.poi.mapping.column.builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;

/*package*/ class ApachePoiStaticIndexExcelImportColumnMapper implements IExcelImportColumnMapper<Sheet, Row, Cell, CellReference> {
	
	private final int columnIndex;

	public ApachePoiStaticIndexExcelImportColumnMapper(int columnIndex) {
		super();
		
		this.columnIndex = columnIndex;
	}
	
	@Override
	public Function<? super Row, CellReference> tryMap(Sheet sheet, IExcelImportNavigator<Sheet, Row, Cell, CellReference> navigator, IExcelImportEventHandler eventHandler) {
		return new SerializableFunction<Row, CellReference>() {
			private static final long serialVersionUID = 1L;
			@Override
			public CellReference apply(Row row) {
				return row == null ? null : new CellReference(row.getRowNum(), columnIndex);
			}
		};
	}
}
