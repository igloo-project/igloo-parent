package fr.openwide.core.imports.excel.mapping.column;

import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.exception.ExcelImportMappingException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public interface IExcelImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> {
	
	IMappedExcelImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> map(
			TSheet sheet,
			IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator,
			IExcelImportEventHandler eventHandler
			) throws ExcelImportMappingException;
	
	interface IMappedExcelImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> {
		
		/**
		 * @return True if this "mapped" column is actually bound to a column in the sheet, false otherwise.
		 */
		boolean isBound();
		
		TCellReference getCellReference(TRow row);
		
		TValue getValue(TRow row);
		
		/**
		 * @return The cell value, or null if the value does not satisfy "mandatory requirements" for the value type.
		 * <p>In general, a value is assumed "present" if it is not {@code null}, so this method returns exactly the same as {@link #getValue(Object)}.
		 * In some cases though, the result may vary. For instance, when dealing whith strings, an empty string would be considered as "absent", so that
		 * {@link #getValue(Object)} would return {@code ""} and {@link #getMandatoryValue(Object)} would return {@code null}.
		 */
		TValue getMandatoryValue(TRow row);
		
	}

}
