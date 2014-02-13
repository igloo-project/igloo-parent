package fr.openwide.core.imports.excel.poi.mapping.column.builder;

import java.text.NumberFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.AbstractColumnBuilder;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;
import fr.openwide.core.imports.excel.mapping.column.builder.state.DateState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.IntegerState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.StringState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.TypeState;

public class ApachePoiColumnBuilder extends AbstractColumnBuilder<Sheet, Row, Cell, CellReference> {
	
	@Override
	public ApachePoiTypeState withHeader(AbstractExcelImportColumnSet<Sheet, Row, Cell, CellReference> columnSet, String headerLabel,
			Predicate<? super String> predicate, int indexAmongMatchedColumns, boolean optional) {
		return new ApachePoiTypeState(columnSet, new ApachePoiHeaderLabelExcelImportColumnMapper(headerLabel, predicate, indexAmongMatchedColumns, optional));
	}

	@Override
	public ApachePoiTypeState withIndex(AbstractExcelImportColumnSet<Sheet, Row, Cell, CellReference> columnSet, int columnIndex) {
		return new ApachePoiTypeState(columnSet, new ApachePoiStaticIndexExcelImportColumnMapper(columnIndex));
	}

	@Override
	public ApachePoiTypeState unmapped(AbstractExcelImportColumnSet<Sheet, Row, Cell, CellReference> columnSet) {
		return new ApachePoiTypeState(columnSet, new ApachePoiUnmappableExcelImportColumnMapper());
	}
	
	private static class ApachePoiTypeState extends TypeState<Sheet, Row, Cell, CellReference> {

		public ApachePoiTypeState(AbstractExcelImportColumnSet<Sheet, Row, Cell, CellReference> columnSet, IExcelImportColumnMapper<Sheet, Row, Cell, CellReference> columnMapper) {
			super(columnSet, columnMapper);
		}
		
		@Override
		public IntegerState<Sheet, Row, Cell, CellReference> asInteger() {
			return new TypeStateSwitcher<Cell>(Functions.<Cell>identity()).toInteger(new Function<Cell, Integer>() {
				@Override
				public Integer apply(Cell cell) {
					if (cell == null) {
						return null;
					}
					
					switch(cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							return (int) cell.getNumericCellValue();
						default:
							return null;
					}
				}
			});
		}

		@Override
		public StringState<Sheet, Row, Cell, CellReference> asString(final Supplier<? extends NumberFormat> formatIfNumeric) {
			return new TypeStateSwitcher<Cell>(Functions.<Cell>identity()).toString(new Function<Cell, String>() {
				@Override
				public String apply(Cell cell) {
					if (cell == null) {
						return null;
					}
					
					switch(cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							return formatIfNumeric.get().format(cell.getNumericCellValue());
						case Cell.CELL_TYPE_STRING:
							return StringUtils.trimToNull(cell.getStringCellValue());
						default:
							return null;
					}
				}
			});
		}

		@Override
		public DateState<Sheet, Row, Cell, CellReference> asDate() {
			return new TypeStateSwitcher<Cell>(Functions.<Cell>identity()).toDate(new Function<Cell, Date>() {
				@Override
				public Date apply(Cell cell) {
					if (cell == null) {
						return null;
					}
					
					switch(cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							return null;
						default:
							return cell.getDateCellValue();
					}
				}
			});
		}
	}

}
