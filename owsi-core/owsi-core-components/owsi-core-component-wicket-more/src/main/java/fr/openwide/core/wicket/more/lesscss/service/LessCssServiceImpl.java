package fr.openwide.core.wicket.more.lesscss.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.config.spring.WicketMoreServiceConfig;
import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;

/**
 * @see WicketMoreServiceConfig
 */
@Service("lessCssService")
public class LessCssServiceImpl implements ILessCssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LessCssServiceImpl.class);
	
	private static final LessEngine LESS_ENGINE = new LessEngine();
	
	private static final Pattern LESSCSS_IMPORT_PATTERN =
			Pattern.compile("^\\p{Blank}*@import\\p{Blank}+\"([^\"]+)\"\\p{Blank}*;", Pattern.MULTILINE);
	
	private static final Pattern LESSCSS_IMPORT_SCOPE_PATTERN =
			Pattern.compile("^@\\{scope-([a-zA-Z0-9_-]*)\\}(.*)$");
	
	private static final Pattern SCOPE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
	
	private static final Map<String, Class<?>> SCOPES = Maps.newHashMapWithExpectedSize(3);
	
	@Override
	// If checkCacheInvalidation is true and, before invocation, a cached value exists and is not up to date, we evict the cache entry. 
	@CacheEvict(value = "lessCssService.compiledStylesheets", 
			key = "T(fr.openwide.core.wicket.more.lesscss.service.LessCssServiceImpl).getCacheKey(#lessInformation)",
			beforeInvocation = true,
			condition= "#checkCacheEntryUpToDate && !(caches.?[name=='lessCssService.compiledStylesheets'][0]?.get(T(fr.openwide.core.wicket.more.lesscss.service.LessCssServiceImpl).getCacheKey(#lessInformation))?.get()?.isUpToDate() ?: false)"
			)
	// THEN, we check if a cached value exists. If it does, it is returned ; if not, the method is called. 
	@Cacheable(value = "lessCssService.compiledStylesheets",
			key = "T(fr.openwide.core.wicket.more.lesscss.service.LessCssServiceImpl).getCacheKey(#lessInformation)")
	public CssStylesheetInformation getCompiledStylesheet(CssStylesheetInformation lessInformation, boolean checkCacheEntryUpToDate)
			throws ServiceException {
		prepareRawStylesheet(lessInformation);
		try {
			CssStylesheetInformation compiledStylesheet = new CssStylesheetInformation(
					lessInformation,
					LESS_ENGINE.compile(lessInformation.getSource())
			);
			
			return compiledStylesheet;
		} catch (LessException e) {
			throw new ServiceException(String.format("Error compiling %1$s (scope: %2$s)",
					lessInformation.getName(), lessInformation.getScope()), e);
		}
	}
	
	private void prepareRawStylesheet(CssStylesheetInformation lessSource) throws ServiceException {
		Matcher matcher = LESSCSS_IMPORT_PATTERN.matcher(lessSource.getSource());
		
		ClassPathResource importedResource;
		
		while (matcher.find()) {
			Class<?> scope;
			String importedResourceFilename;
			
			String importUrl = matcher.group(1);
			Matcher scopeMatcher = LESSCSS_IMPORT_SCOPE_PATTERN.matcher(importUrl);
			if (scopeMatcher.matches()) {
				Class<?> referencedScope = SCOPES.get(scopeMatcher.group(1));
				if (referencedScope != null) {
					scope = referencedScope;
				} else {
					throw new IllegalStateException(String.format("Scope %1$s is not supported", scopeMatcher.group(1)));
				}
				importedResourceFilename = scopeMatcher.group(2);
			} else {
				// Defaults to importing file's scope
				scope = lessSource.getScope();
				importedResourceFilename = getRelativeToScopePath(lessSource.getName(), matcher.group(1));
			}
			
			InputStream inputStream = null;
			try {
				importedResource = new ClassPathResource(importedResourceFilename, scope);
				inputStream = importedResource.getURL().openStream();
				
				CssStylesheetInformation importedStylesheet = new CssStylesheetInformation(scope, importedResourceFilename, IOUtils.toString(inputStream), importedResource.lastModified());
				prepareRawStylesheet(importedStylesheet);
				
				lessSource.addImportedStylesheet(importedStylesheet);
				
				lessSource.setSource(StringUtils.replace(lessSource.getSource(), matcher.group(), importedStylesheet.getSource()));
			} catch (Exception e) {
				throw new ServiceException(String.format("Error reading lesscss source for %1$s in %2$s (scope: %3$s)",
					importedResourceFilename, lessSource.getName(), scope), e);
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
	
	@Override
	public void registerImportScope(String scopeName, Class<?> scope) {
		if (SCOPES.containsKey(scopeName)) {
			LOGGER.error(String.format("Scope %1$s already registered: ignored", scopeName));
			return;
		}
		Matcher matcher = SCOPE_NAME_PATTERN.matcher(scopeName);
		if (!matcher.matches()) {
			LOGGER.error(String.format("Scope name %1$s invalid (%2$s): ignored", scopeName, SCOPE_NAME_PATTERN.toString()));
			return;
		}
		
		SCOPES.put(scopeName, scope);
	}
	
	public static String getCacheKey(CssStylesheetInformation resourceInformation) {
		StringBuilder cacheKeyBuilder = new StringBuilder();
		cacheKeyBuilder.append(resourceInformation.getScope().getName());
		cacheKeyBuilder.append("-");
		cacheKeyBuilder.append(resourceInformation.getName());
		
		return cacheKeyBuilder.toString();
	}

}
