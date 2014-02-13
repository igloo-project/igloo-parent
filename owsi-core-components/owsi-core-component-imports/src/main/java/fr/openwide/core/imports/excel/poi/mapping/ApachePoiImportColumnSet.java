package fr.openwide.core.imports.excel.poi.mapping;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.AbstractColumnBuilder;
import fr.openwide.core.imports.excel.poi.mapping.column.builder.ApachePoiColumnBuilder;

public class ApachePoiImportColumnSet extends AbstractExcelImportColumnSet<Sheet, Row, Cell, CellReference> {

	public ApachePoiImportColumnSet() {
		this(new ApachePoiColumnBuilder());
	}
	
	public ApachePoiImportColumnSet(AbstractColumnBuilder<Sheet, Row, Cell, CellReference> builder) {
		super(builder);
	}

}
