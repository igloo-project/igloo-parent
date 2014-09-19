package fr.openwide.core.imports.table.common.mapping.column;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;

import fr.openwide.core.imports.table.common.location.ITableImportNavigator;
import fr.openwide.core.imports.table.common.mapping.column.ITableImportColumnDefinition.IMappedExcelImportColumnDefinition;

public class MappedTableImportColumnDefinitionImpl<TTable, TRow, TCell, TCellReference, TValue>
		implements IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> {
	private final TTable table;
	private final boolean bound;
	private final Function<? super TRow, ? extends TCellReference> rowToCellReferenceFunction;
	private final ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator;
	private final Function<? super TCell, ? extends TValue> cellToValueFunction;
	private final Predicate<? super TValue> mandatoryValuePredicate;

	public MappedTableImportColumnDefinitionImpl(
			TTable table,
			Function<? super TRow, ? extends TCellReference> rowToCellReferenceFunction,
			ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator,
			Function<? super TCell, ? extends TValue> cellToValueFunction,
			Predicate<? super TValue> mandatoryValuePredicate) {
		super();
		this.table = table;
		if (rowToCellReferenceFunction != null) {
			this.bound = true;
			this.rowToCellReferenceFunction = rowToCellReferenceFunction;
		} else {
			this.bound = false;
			this.rowToCellReferenceFunction = Functions.constant(null);
		}
		this.navigator = navigator;
		this.cellToValueFunction = cellToValueFunction;
		this.mandatoryValuePredicate = mandatoryValuePredicate;
	}
	
	@Override
	public boolean isBound() {
		return bound;
	}
	
	@Override
	public TCellReference getCellReference(TRow row) {
		return rowToCellReferenceFunction.apply(row);
	}

	@Override
	public TValue getValue(TRow row) {
		TCellReference cellReference = getCellReference(row);
		TCell cell = navigator.getCell(table, cellReference);
		return cellToValueFunction.apply(cell);
	}

	@Override
	public TValue getMandatoryValue(TRow row) {
		TValue value = getValue(row);
		if (!mandatoryValuePredicate.apply(value)) {
			return null;
		}
		return value;
	}
	
	@Override
	public boolean hasContent(TRow row) {
		TValue value = getValue(row);
		return mandatoryValuePredicate.apply(value);
	}
}