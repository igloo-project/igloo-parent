package fr.openwide.core.imports.excel.poi.mapping;

import java.util.Comparator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.AbstractColumnBuilder;
import fr.openwide.core.imports.excel.poi.mapping.column.builder.ApachePoiColumnBuilder;

public class ApachePoiImportColumnSet extends AbstractExcelImportColumnSet<Sheet, Row, Cell, CellReference> {

	public ApachePoiImportColumnSet() {
		super(new ApachePoiColumnBuilder());
	}
	
	public ApachePoiImportColumnSet(AbstractColumnBuilder<Sheet, Row, Cell, CellReference> builder) {
		super(builder);
	}
	
	public ApachePoiImportColumnSet(Comparator<? super String> defaultHeaderLabelCollator) {
		super(new ApachePoiColumnBuilder(), defaultHeaderLabelCollator);
	}
	
	public ApachePoiImportColumnSet(AbstractColumnBuilder<Sheet, Row, Cell, CellReference> builder, Comparator<? super String> defaultHeaderLabelCollator) {
		super(builder, defaultHeaderLabelCollator);
	}

}
