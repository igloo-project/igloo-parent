package fr.openwide.core.imports.excel.mapping.column;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fr.openwide.core.imports.excel.mapping.column.IExcelImportColumnDefinition.IMappedExcelImportColumnDefinition;

public class MappedExcelImportColumnDefinitionImpl<TSheet, TRow, TCell, TValue>
		implements IMappedExcelImportColumnDefinition<TRow, TCell, TValue> {
	private final Function<? super TRow, ? extends TCell> rowToCellFunction;
	private final Function<? super TCell, ? extends TValue> cellToValueFunction;
	private final Predicate<? super TValue> mandatoryValuePredicate;

	public MappedExcelImportColumnDefinitionImpl(
			Function<? super TRow, ? extends TCell> rowToCellFunction,
			Function<? super TCell, ? extends TValue> cellToValueFunction,
			Predicate<? super TValue> mandatoryValuePredicate) {
		super();
		this.rowToCellFunction = rowToCellFunction;
		this.cellToValueFunction = cellToValueFunction;
		this.mandatoryValuePredicate = mandatoryValuePredicate;
	}
	
	@Override
	public TCell getCell(TRow row) {
		return rowToCellFunction.apply(row);
	}

	@Override
	public TValue getValue(TRow row) {
		return cellToValueFunction.apply(getCell(row));
	}

	@Override
	public TValue getMandatoryValue(TRow row) {
		TCell cell = getCell(row);
		TValue value = cellToValueFunction.apply(cell);
		if (!mandatoryValuePredicate.apply(value)) {
			return null;
		}
		return value;
	}
}