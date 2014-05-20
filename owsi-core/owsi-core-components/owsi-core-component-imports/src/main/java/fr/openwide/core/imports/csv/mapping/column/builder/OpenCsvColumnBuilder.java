package fr.openwide.core.imports.csv.mapping.column.builder;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.primitives.Doubles;

import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.AbstractColumnBuilder;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;
import fr.openwide.core.imports.excel.mapping.column.builder.MappingConstraint;
import fr.openwide.core.imports.excel.mapping.column.builder.state.BigDecimalState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.DateState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.DoubleState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.IntegerState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.LongState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.StringState;
import fr.openwide.core.imports.excel.mapping.column.builder.state.TypeState;

public class OpenCsvColumnBuilder extends AbstractColumnBuilder<CsvSheet, CsvRow, CsvCell, CsvCellReference> {
	
	private static final Function<String, Double> DOUBLE_FORMAT_FUNCTION = Doubles.stringConverter();
	
	private static final Function<String, BigDecimal> BIG_DECIMAL_FORMAT_FUNCTION = new Function<String, BigDecimal>() {
		@Override
		public BigDecimal apply(String input) {
			return input == null ? null : new BigDecimal(input);
		}
	};
	
	private final Function<? super String, ? extends Date> dateFormatFunction;
	
	public OpenCsvColumnBuilder(Function<? super String, ? extends Date> dateFormatFunction) {
		super();
		this.dateFormatFunction = dateFormatFunction;
	}

	@Override
	public OpenCsvTypeState withHeader(AbstractExcelImportColumnSet<CsvSheet, CsvRow, CsvCell, CsvCellReference> columnSet, String headerLabel,
			Predicate<? super String> predicate, int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		return new OpenCsvTypeState(columnSet, new OpenCsvHeaderLabelExcelImportColumnMapper(headerLabel, predicate, indexAmongMatchedColumns, mappingConstraint));
	}

	@Override
	public OpenCsvTypeState withIndex(AbstractExcelImportColumnSet<CsvSheet, CsvRow, CsvCell, CsvCellReference> columnSet, int columnIndex) {
		return new OpenCsvTypeState(columnSet, new OpenCsvStaticIndexExcelImportColumnMapper(columnIndex));
	}

	@Override
	public OpenCsvTypeState unmapped(AbstractExcelImportColumnSet<CsvSheet, CsvRow, CsvCell, CsvCellReference> columnSet) {
		return new OpenCsvTypeState(columnSet, new OpenCsvUnmappableExcelImportColumnMapper());
	}
	
	private class OpenCsvTypeState extends TypeState<CsvSheet, CsvRow, CsvCell, CsvCellReference> {

		public OpenCsvTypeState(AbstractExcelImportColumnSet<CsvSheet, CsvRow, CsvCell, CsvCellReference> columnSet,
				IExcelImportColumnMapper<CsvSheet, CsvRow, CsvCell, CsvCellReference> columnMapper) {
			super(columnSet, columnMapper);
		}
		
		@Override
		public IntegerState<CsvSheet, CsvRow, CsvCell, CsvCellReference> asInteger() {
			return asDouble().toInteger();
		}
		
		@Override
		public LongState<CsvSheet, CsvRow, CsvCell, CsvCellReference> asLong() {
			return asDouble().toLong();
		}
		
		@Override
		public DoubleState<CsvSheet, CsvRow, CsvCell, CsvCellReference> asDouble() {
			return asString().cleaned().toDouble(DOUBLE_FORMAT_FUNCTION);
		}
		
		@Override
		public BigDecimalState<CsvSheet, CsvRow, CsvCell, CsvCellReference> asBigDecimal() {
			return asString().cleaned().toBigDecimal(BIG_DECIMAL_FORMAT_FUNCTION);
		}

		@Override
		public StringState<CsvSheet, CsvRow, CsvCell, CsvCellReference> asString(final Supplier<? extends NumberFormat> formatIfNumeric) {
			return new TypeStateSwitcher<CsvCell>(Functions.<CsvCell>identity()).toString(new Function<CsvCell, String>() {
				@Override
				public String apply(CsvCell cell) {
					if (cell == null) {
						return null;
					}
					
					return cell.getContent();
				}
			});
		}

		@Override
		public DateState<CsvSheet, CsvRow, CsvCell, CsvCellReference> asDate() {
			return asString().toDate(dateFormatFunction);
		}
	}

}
