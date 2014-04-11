package fr.openwide.core.imports.excel.mapping.column;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;

import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.mapping.column.IExcelImportColumnDefinition.IMappedExcelImportColumnDefinition;

public class MappedExcelImportColumnDefinitionImpl<TSheet, TRow, TCell, TCellReference, TValue>
		implements IMappedExcelImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> {
	private final TSheet sheet;
	private final boolean bound;
	private final Function<? super TRow, ? extends TCellReference> rowToCellReferenceFunction;
	private final IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator;
	private final Function<? super TCell, ? extends TValue> cellToValueFunction;
	private final Predicate<? super TValue> mandatoryValuePredicate;

	public MappedExcelImportColumnDefinitionImpl(
			TSheet sheet,
			Function<? super TRow, ? extends TCellReference> rowToCellReferenceFunction,
			IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator,
			Function<? super TCell, ? extends TValue> cellToValueFunction,
			Predicate<? super TValue> mandatoryValuePredicate) {
		super();
		this.sheet = sheet;
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
		TCell cell = navigator.getCell(sheet, cellReference);
		return cellToValueFunction.apply(cell);
	}

	@Override
	public TValue getMandatoryValue(TRow row) {
		TCellReference cellReference = getCellReference(row);
		TCell cell = navigator.getCell(sheet, cellReference);
		TValue value = cellToValueFunction.apply(cell);
		if (!mandatoryValuePredicate.apply(value)) {
			return null;
		}
		return value;
	}
}