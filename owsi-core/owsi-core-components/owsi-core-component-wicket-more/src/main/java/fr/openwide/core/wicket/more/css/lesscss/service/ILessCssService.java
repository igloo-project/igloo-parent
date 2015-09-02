package fr.openwide.core.wicket.more.css.lesscss.service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.css.lesscss.model.LessCssStylesheetInformation;

public interface ILessCssService {

	LessCssStylesheetInformation getCompiledStylesheet(LessCssStylesheetInformation lessInformation, boolean checkCacheEntryUpToDate) throws ServiceException;

	void registerImportScope(String scopeName, Class<?> scope);
	
}
