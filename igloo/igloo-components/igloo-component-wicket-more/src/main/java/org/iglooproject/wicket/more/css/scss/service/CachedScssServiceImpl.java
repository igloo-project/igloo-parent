package org.iglooproject.wicket.more.css.scss.service;

import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.sass.service.ScssServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Provides caching to {@link IScssService} with Spring AOP.
 */
// beware that bean name is used in @Cacheable annotations
@Service("scssService")
public class CachedScssServiceImpl extends ScssServiceImpl implements ICachedScssService {

	@Override
	// If checkCacheInvalidation is true and, before invocation, a cached value exists and is not up to date, we evict the cache entry. 
	@CacheEvict(value = "scssService.compiledStylesheets", 
			key = "@scssService.getCacheKey(#scope, #path)",
			beforeInvocation = true,
			condition= "#checkCacheEntryUpToDate && !(caches.?[name=='scssService.compiledStylesheets'][0]?.get(@scssService.getCacheKey(#scope, #path))?.get()?.isUpToDate() ?: false)"
			)
	// THEN, we check if a cached value exists. If it does, it is returned ; if not, the method is called. 
	@Cacheable(value = "scssService.compiledStylesheets",
			key = "@scssService.getCacheKey(#scope, #path)")
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

}
