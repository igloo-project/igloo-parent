package org.iglooproject.imports.table.common.mapping.column.builder;

import com.google.common.base.Function;

import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.exception.TableImportMappingException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;

public interface ITableImportColumnMapper<TTable, TRow, TCell, TCellReference> {
	
	/**
	 * @return The row to cellReference mapping function, or null if the mapper was unable to determine a mapping.
	 */
	Function<? super TRow, ? extends TCellReference> tryMap(
			TTable sheet,
			ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator,
			ITableImportEventHandler eventHandler
			) throws TableImportMappingException;

}
