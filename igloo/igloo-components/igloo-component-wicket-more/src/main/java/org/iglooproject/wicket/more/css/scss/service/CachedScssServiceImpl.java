package org.iglooproject.wicket.more.css.scss.service;

import java.io.IOException;

import org.iglooproject.commons.io.ClassPathResourceUtil;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.sass.service.ScssServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Provides caching to {@link IScssService} with Spring AOP.
 */
// beware that bean name is used in @Cacheable annotations
@Service("scssService")
public class CachedScssServiceImpl extends ScssServiceImpl implements ICachedScssService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachedScssServiceImpl.class);

	private static final String SCSS_COMPILED_STYLESHEETS_CACHE_NAME = "scssService.compiledStylesheets";

	private final ClassPathResourceUtil classpathResourceUtil = new ClassPathResourceUtil();

	@Autowired
	private CacheManager cacheManager;

	@Override
	// If checkCacheInvalidation is true and, before invocation, a cached value exists and is not up to date, we evict the cache entry. 
	@CacheEvict(
			value = SCSS_COMPILED_STYLESHEETS_CACHE_NAME, 
			key = "#root.target.getCacheKey(#scope, #path)",
			beforeInvocation = true,
			condition = "#checkCacheEntryUpToDate && !#root.target.isUpToDate(#scope, #path)"
	)
	// THEN, we check if a cached value exists. If it does, it is returned ; if not, the method is called. 
	@Cacheable(value = SCSS_COMPILED_STYLESHEETS_CACHE_NAME, key = "#root.target.getCacheKey(#scope, #path)")
	public ScssStylesheetInformation getCachedCompiledStylesheet(Class<?> scope, String path, boolean checkCacheEntryUpToDate) {
		return super.getCompiledStylesheet(scope, path);
	}

	@Override
	public void registerImportScope(String scopeName, Class<?> scope) {
		super.registerImportScope(scopeName, scope);
	}

	@Override
	public String getCacheKey(Class<?> scope, String path) {
		StringBuilder cacheKeyBuilder = new StringBuilder();
		cacheKeyBuilder.append(getFullPath(scope, path));
		
		return cacheKeyBuilder.toString();
	}

	public boolean isUpToDate(Class<?> scope, String path) {
		String cacheKey = getCacheKey(scope, path);
		ScssStylesheetInformation stylesheet = cacheManager.getCache(SCSS_COMPILED_STYLESHEETS_CACHE_NAME)
				.get(cacheKey, ScssStylesheetInformation.class);
		if (stylesheet == null) {
			return false;
		}
		for (String referencedResource : stylesheet.getReferencedResources()) {
			if (!isUpToDate(stylesheet, referencedResource)) {
				return false;
			}
		}
		return true;
	}
		
	private boolean isUpToDate(ScssStylesheetInformation stylesheet, String path) {
		try {
			return classpathResourceUtil.lastModified(path) <= stylesheet.getLastModifiedTime();
		} catch (RuntimeException | IOException e) {
			LOGGER.warn("Error determining lastModifiedDate on {}; cached invalidated", path, e);
			return false;
		}
	}

}
