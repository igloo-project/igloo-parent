package fr.openwide.core.wicket.more.lesscss.service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;

public interface ILessCssService {

	CssStylesheetInformation getCompiledStylesheet(CssStylesheetInformation lessInformation, boolean checkCacheEntryUpToDate) throws ServiceException;

	void registerImportScope(String scopeName, Class<?> scope);
	
}
