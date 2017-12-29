package org.iglooproject.wicket.more.css.scss.service;

import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.css.scss.model.ScssStylesheetInformation;

public interface IScssService {

	ScssStylesheetInformation getCompiledStylesheet(Class<?> scope, String path, boolean checkCacheEntryUpToDate) throws ServiceException;

	void registerImportScope(String scopeName, Class<?> scope);
	
}
