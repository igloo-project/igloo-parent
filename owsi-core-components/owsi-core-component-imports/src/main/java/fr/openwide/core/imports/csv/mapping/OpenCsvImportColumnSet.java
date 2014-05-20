package fr.openwide.core.imports.csv.mapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.imports.csv.mapping.column.builder.OpenCsvColumnBuilder;
import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.AbstractColumnBuilder;

public class OpenCsvImportColumnSet extends AbstractExcelImportColumnSet<CsvSheet, CsvRow, CsvCell, CsvCellReference> {
	private static final Function<String, Date> DEFAULT_DATE_FORMAT_FUNCTION = new SerializableFunction<String, Date>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Date apply(String input) {
			if (input == null) {
				return null;
			}
			DateFormat format = DateFormat.getDateTimeInstance();
			try {
				return format.parse(input);
			} catch (ParseException e) {
				return null;
			}
		}
	};

	public OpenCsvImportColumnSet() {
		super(new OpenCsvColumnBuilder(DEFAULT_DATE_FORMAT_FUNCTION));
	}
	
	public OpenCsvImportColumnSet(AbstractColumnBuilder<CsvSheet, CsvRow, CsvCell, CsvCellReference> builder) {
		super(builder);
	}
	
	public OpenCsvImportColumnSet(Function<? super String, ? extends Date> dateFormatFunction) {
		super(new OpenCsvColumnBuilder(dateFormatFunction));
	}
	
	public OpenCsvImportColumnSet(Comparator<? super String> defaultHeaderLabelCollator) {
		super(new OpenCsvColumnBuilder(DEFAULT_DATE_FORMAT_FUNCTION), defaultHeaderLabelCollator);
	}
	
	public OpenCsvImportColumnSet(Comparator<? super String> defaultHeaderLabelCollator, Function<? super String, ? extends Date> dateFormatFunction) {
		super(new OpenCsvColumnBuilder(dateFormatFunction), defaultHeaderLabelCollator);
	}
	
	public OpenCsvImportColumnSet(AbstractColumnBuilder<CsvSheet, CsvRow, CsvCell, CsvCellReference> builder, Comparator<? super String> defaultHeaderLabelCollator) {
		super(builder, defaultHeaderLabelCollator);
	}

}
