package fr.openwide.core.wicket.more.lesscss.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;

@Service("lessCssService")
public class LessCssServiceImpl implements ILessCssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LessCssServiceImpl.class);
	
	private static final LessEngine LESS_ENGINE = new LessEngine();
	
	private static final Pattern LESSCSS_IMPORT_PATTERN =
			Pattern.compile("^\\p{Blank}*@import\\p{Blank}+\"([^\"]+)\"\\p{Blank}*;", Pattern.MULTILINE);
	
	@Override
	@Cacheable(value = "lessCssService.compiledStylesheets",
			key = "T(fr.openwide.core.wicket.more.lesscss.service.LessCssServiceImpl).getCacheKey(#scope, #name)",
			condition = "#enableCache")
	public CssStylesheetInformation getCompiledStylesheet(Class<?> scope, String name,
			CssStylesheetInformation lessSource, boolean enableCache) throws ServiceException {
		
		CssStylesheetInformation rawStylesheet = prepareRawStylesheet(scope, name, lessSource);
		try {
			CssStylesheetInformation compiledStylesheet = new CssStylesheetInformation(
					LESS_ENGINE.compile(rawStylesheet.getSource()),
					rawStylesheet.getLastModifiedTime());
			
			return compiledStylesheet;
		} catch (LessException e) {
			throw new ServiceException(String.format("Error compiling %1$s (scope: %2$s)",
					name, scope), e);
		}
	}
	
	private CssStylesheetInformation prepareRawStylesheet(Class<?> scope, String name, CssStylesheetInformation lessSource)
			throws ServiceException {
		Matcher matcher = LESSCSS_IMPORT_PATTERN.matcher(lessSource.getSource());
		
		ClassPathResource importedResource;
		
		while (matcher.find()) {
			String importedResourceFilename = getRelativeToScopePath(name, matcher.group(1));
			InputStream inputStream = null;
			
			try {
				importedResource = new ClassPathResource(importedResourceFilename, scope);
				inputStream = importedResource.getURL().openStream();
				
				CssStylesheetInformation importedStylesheet =
						prepareRawStylesheet(scope, importedResourceFilename,
								new CssStylesheetInformation(IOUtils.toString(inputStream), importedResource.lastModified()));
			
				if (importedStylesheet.getLastModifiedTime() > lessSource.getLastModifiedTime()) {
					lessSource.setLastModifiedTime(importedStylesheet.getLastModifiedTime());
				}
				
				lessSource.setSource(lessSource.getSource().replaceFirst(
						Matcher.quoteReplacement(matcher.group()), importedStylesheet.getSource()));
			} catch (Exception e) {
				throw new ServiceException(String.format("Error reading lesscss source for %1$s in %2$s (scope: %3$s)",
					importedResourceFilename, name, scope), e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						LOGGER.error(String.format("Error closing the resource stream for: %1$s", importedResourceFilename));
					}
				}
			}
		}
		
		return lessSource;
	}
	
	private String getRelativeToScopePath(String sourceFile, String importFilename) {
		String contextPath = FilenameUtils.getFullPath(sourceFile);
		String relativeToScopeFilename;
		if (StringUtils.hasLength(contextPath)) {
			relativeToScopeFilename = FilenameUtils.concat(contextPath, importFilename);
		} else {
			relativeToScopeFilename = importFilename;
		}
		
		return relativeToScopeFilename;
	}
	
	public static String getCacheKey(Class<?> scope, String name) {
		StringBuilder cacheKeyBuilder = new StringBuilder();
		cacheKeyBuilder.append(scope.getName());
		cacheKeyBuilder.append("-");
		cacheKeyBuilder.append(name);
		
		return cacheKeyBuilder.toString();
	}

}
