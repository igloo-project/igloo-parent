package org.iglooproject.imports.table.opencsv.mapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import com.google.common.base.Function;

import org.iglooproject.commons.util.functional.SerializableFunction;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;
import org.iglooproject.imports.table.common.mapping.column.builder.AbstractTableImportColumnBuilder;
import org.iglooproject.imports.table.opencsv.mapping.column.builder.OpenCsvImportColumnBuilder;
import org.iglooproject.imports.table.opencsv.model.CsvCell;
import org.iglooproject.imports.table.opencsv.model.CsvCellReference;
import org.iglooproject.imports.table.opencsv.model.CsvRow;
import org.iglooproject.imports.table.opencsv.model.CsvTable;

public class OpenCsvImportColumnSet extends AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> {
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
		super(new OpenCsvImportColumnBuilder(DEFAULT_DATE_FORMAT_FUNCTION));
	}
	
	public OpenCsvImportColumnSet(AbstractTableImportColumnBuilder<CsvTable, CsvRow, CsvCell, CsvCellReference> builder) {
		super(builder);
	}
	
	public OpenCsvImportColumnSet(Function<? super String, ? extends Date> dateFormatFunction) {
		super(new OpenCsvImportColumnBuilder(dateFormatFunction));
	}
	
	public OpenCsvImportColumnSet(Comparator<? super String> defaultHeaderLabelCollator) {
		super(new OpenCsvImportColumnBuilder(DEFAULT_DATE_FORMAT_FUNCTION), defaultHeaderLabelCollator);
	}
	
	public OpenCsvImportColumnSet(Comparator<? super String> defaultHeaderLabelCollator, Function<? super String, ? extends Date> dateFormatFunction) {
		super(new OpenCsvImportColumnBuilder(dateFormatFunction), defaultHeaderLabelCollator);
	}
	
	public OpenCsvImportColumnSet(AbstractTableImportColumnBuilder<CsvTable, CsvRow, CsvCell, CsvCellReference> builder, Comparator<? super String> defaultHeaderLabelCollator) {
		super(builder, defaultHeaderLabelCollator);
	}

}
