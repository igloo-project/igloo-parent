package fr.openwide.core.wicket.more.css.scss.service;

import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.OutputStyle;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.StringContext;

import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Maps;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.config.spring.WicketMoreServiceConfig;
import fr.openwide.core.wicket.more.css.scss.model.ScssStylesheetInformation;

/**
 * @see WicketMoreServiceConfig
 */
// TODO SCSS : @Service("scssService")
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
		
		try {
			JSassScopeAwareImporter importer = new JSassScopeAwareImporter(SCOPES);
			importer.addSourceUri(scssPath);
			
			Compiler compiler = new Compiler();
			Options options = new Options();
			options.setOutputStyle(OutputStyle.EXPANDED);
			options.setIndent("\t");
			options.getImporters().add(importer);
			
			ClassPathResource scssCpr = new ClassPathResource(scssPath);
			
			Context fileContext = new StringContext(IOUtils.toString(scssCpr.getInputStream()), new URI("classpath", "/" + scssPath, null), null, options);
			Output output = compiler.compile(fileContext);
			// Write result
			ScssStylesheetInformation compiledStylesheet = new ScssStylesheetInformation(scssPath, output.getCss());
			
			for (String sourceUri : importer.getSourceUris()) {
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
