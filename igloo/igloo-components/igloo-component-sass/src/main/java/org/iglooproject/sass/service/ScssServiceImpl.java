package org.iglooproject.sass.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.iglooproject.sass.jsass.JSassClassPathImporter;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Maps;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.OutputStyle;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.StringContext;

public class ScssServiceImpl implements IScssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScssServiceImpl.class);
	
	private static final Pattern SCOPE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
	
	private static final Map<String, Class<?>> SCOPES = Maps.newHashMapWithExpectedSize(3);
	
	@Override
	public ScssStylesheetInformation getCompiledStylesheet(Class<?> scope, String path) {
		String scssPath = getFullPath(scope, path);
		
		try {
			JSassClassPathImporter importer = new JSassClassPathImporter(SCOPES);
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
		} catch (IOException | URISyntaxException | CompilationException e) {
			throw new RuntimeException(String.format("Error compiling %1$s", scssPath), e);
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

	protected String getFullPath(Class<?> scope, String path) {
		StringBuilder fullPath = new StringBuilder();
		if (scope != null) {
			fullPath.append(scope.getPackage().getName().replace(".", "/")).append("/");
		}
		fullPath.append(path);
		return fullPath.toString();
	}

}
