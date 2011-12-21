package fr.openwide.core.wicket.more.lesscss.service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;

public interface ILessCssService {

	CssStylesheetInformation getCompiledStylesheet(Class<?> scope, String name, CssStylesheetInformation lessSource,
			boolean enableCache) throws ServiceException;
	
}
