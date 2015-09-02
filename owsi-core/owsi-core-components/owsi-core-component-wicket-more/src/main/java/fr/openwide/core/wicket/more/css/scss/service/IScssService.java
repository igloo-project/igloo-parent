package fr.openwide.core.wicket.more.css.scss.service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.css.scss.model.ScssStylesheetInformation;

public interface IScssService {

	ScssStylesheetInformation getCompiledStylesheet(Class<?> scope, String path, boolean checkCacheEntryUpToDate) throws ServiceException;

	void registerImportScope(String scopeName, Class<?> scope);
	
}
