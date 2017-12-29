package org.iglooproject.wicket.more.css.lesscss.service;

import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.css.lesscss.model.LessCssStylesheetInformation;

public interface ILessCssService {

	LessCssStylesheetInformation getCompiledStylesheet(LessCssStylesheetInformation lessInformation, boolean checkCacheEntryUpToDate) throws ServiceException;

	void registerImportScope(String scopeName, Class<?> scope);
	
}
