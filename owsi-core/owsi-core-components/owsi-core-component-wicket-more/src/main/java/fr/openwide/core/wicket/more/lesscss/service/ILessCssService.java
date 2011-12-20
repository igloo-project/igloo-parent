package fr.openwide.core.wicket.more.lesscss.service;

import java.util.Locale;

import org.apache.wicket.util.resource.IResourceStream;

import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;

public interface ILessCssService {
	
	CssStylesheetInformation getCss(IResourceStream resourceStream,
			Class<?> scope, String name, Locale locale, String style, String variation,
			boolean processLess);
	
}
