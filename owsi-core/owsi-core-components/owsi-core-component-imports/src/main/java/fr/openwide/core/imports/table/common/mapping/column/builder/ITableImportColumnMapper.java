package fr.openwide.core.imports.table.common.mapping.column.builder;

import com.google.common.base.Function;

import fr.openwide.core.imports.table.common.event.ITableImportEventHandler;
import fr.openwide.core.imports.table.common.event.exception.TableImportMappingException;
import fr.openwide.core.imports.table.common.location.ITableImportNavigator;

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
