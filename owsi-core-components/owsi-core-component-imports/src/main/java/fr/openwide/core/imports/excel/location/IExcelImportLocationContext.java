package fr.openwide.core.imports.excel.location;

import fr.openwide.core.imports.excel.event.exception.ExcelImportContentException;

public interface IExcelImportLocationContext {
	
	void error(String message) throws ExcelImportContentException;

}
