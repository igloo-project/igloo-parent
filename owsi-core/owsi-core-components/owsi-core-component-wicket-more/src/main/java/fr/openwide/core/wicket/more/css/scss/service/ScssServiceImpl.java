package fr.openwide.core.wicket.more.css.scss.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.vaadin.sass.internal.ScssStylesheet;
import com.vaadin.sass.internal.handler.SCSSDocumentHandlerImpl;
import com.vaadin.sass.internal.handler.SCSSErrorHandler;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.config.spring.WicketMoreServiceConfig;
import fr.openwide.core.wicket.more.css.scss.model.ScssStylesheetInformation;

/**
 * @see WicketMoreServiceConfig
 */
@Service("scssService")
public class ScssServiceImpl implements IScssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScssServiceImpl.class);
	
	private static final Pattern SCOPE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
	
	private static final Map<String, Class<?>> SCOPES = Maps.newHashMapWithExpectedSize(3);
	
	/**
	 * required = false pour les tests unitaires
	 */
	@Autowired(required = false)
	private CoreConfigurer configurer;
	
	@Override
	// If checkCacheInvalidation is true and, before invocation, a cached value exists and is not up to date, we evict the cache entry. 
	@CacheEvict(value = "scssService.compiledStylesheets", 
			key = "T(fr.openwide.core.wicket.more.css.scss.service.ScssServiceImpl).getCacheKey(#scope, #path)",
			beforeInvocation = true,
			condition= "#checkCacheEntryUpToDate && !(caches.?[name=='scssService.compiledStylesheets'][0]?.get(T(fr.openwide.core.wicket.more.css.scss.service.ScssServiceImpl).getCacheKey(#scope, #path))?.get()?.isUpToDate() ?: false)"
			)
	// THEN, we check if a cached value exists. If it does, it is returned ; if not, the method is called. 
	@Cacheable(value = "scssService.compiledStylesheets",
			key = "T(fr.openwide.core.wicket.more.css.scss.service.ScssServiceImpl).getCacheKey(#scope, #path)")
	public ScssStylesheetInformation getCompiledStylesheet(Class<?> scope, String path, boolean checkCacheEntryUpToDate)
			throws ServiceException {
		String scssPath = getFullPath(scope, path);
		
		SCSSErrorHandler errorHandler = new SCSSErrorHandler();
		errorHandler.setWarningsAreErrors(false);
		try {
			// Parse stylesheet
			ScssStylesheet scss = ScssStylesheet.get(scssPath, null, new SCSSDocumentHandlerImpl(),
					errorHandler);
			if (scss == null) {
				throw new ServiceException(String.format("The SCSS file %1$s could not be found", scssPath));
			}
			
			scss.addResolver(new ScopeAwareScssStylesheetResolver(SCOPES));

			// Compile scss -> css
			scss.compile();
			
			if (errorHandler.isErrorsDetected()) {
				throw new ServiceException(String.format("Error compiling %1$s: see errors above", scssPath));
			}

			// Write result
			ScssStylesheetInformation compiledStylesheet = new ScssStylesheetInformation(scssPath, scss.printState());
			
			for (String sourceUri : scss.getSourceUris()) {
				ClassPathResource cpr = new ClassPathResource(sourceUri);
				compiledStylesheet.addImportedStylesheet(new ScssStylesheetInformation(sourceUri, cpr.lastModified()));
			}
			
			return compiledStylesheet;
		} catch (Exception e) {
			throw new ServiceException(String.format("Error compiling %1$s", scssPath), e);
		}
	}
	
	@Override
	public void registerImportScope(String scopeName, Class<?> scope) {
		if (SCOPES.containsKey(scopeName)) {
			LOGGER.warn(String.format("Scope %1$s already registered: ignored", scopeName));
			return;
		}
		Matcher matcher = SCOPE_NAME_PATTERN.matcher(scopeName);
		if (!matcher.matches()) {
			LOGGER.error(String.format("Scope name %1$s invalid (%2$s): ignored", scopeName, SCOPE_NAME_PATTERN.toString()));
			return;
		}
		
		SCOPES.put(scopeName, scope);
	}
	
	public static String getCacheKey(Class<?> scope, String path) {
		StringBuilder cacheKeyBuilder = new StringBuilder();
		cacheKeyBuilder.append(getFullPath(scope, path));
		
		return cacheKeyBuilder.toString();
	}
	
	public static String getFullPath(Class<?> scope, String path) {
		StringBuilder fullPath = new StringBuilder();
		if (scope != null) {
			fullPath.append(scope.getPackage().getName().replace(".", "/")).append("/");
		}
		fullPath.append(path);
		return fullPath.toString();
	}

}
