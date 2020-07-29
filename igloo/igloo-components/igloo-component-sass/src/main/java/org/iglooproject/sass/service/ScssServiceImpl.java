package org.iglooproject.sass.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassPathUtils;
import org.iglooproject.autoprefixer.Autoprefixer;
import org.iglooproject.autoprefixer.AutoprefixerException;
import org.iglooproject.commons.io.ClassPathResourceUtil;
import org.iglooproject.sass.config.ISassConfigurationProvider;
import org.iglooproject.sass.jsass.JSassClassPathImporter;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

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

	private static final Map<String, Class<?>> SCOPES = new HashMap<>();

	private final ISassConfigurationProvider configurationProvider;

	private final ClassPathResourceUtil classPathResourceUtil = new ClassPathResourceUtil();

	public ScssServiceImpl(final ISassConfigurationProvider configurationProvider) {
		super();
		this.configurationProvider = configurationProvider;
	}

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
			
			final String scss = classPathResourceUtil.asUtf8String(scssPath);
			Context fileContext = new StringContext(scss, new URI(scssPath), null, options);
			Output output = compiler.compile(fileContext);
			String compiledOutput = output.getCss();
			
			if (configurationProvider.isAutoprefixerEnabled()) {
				LOGGER.debug("Autoprefixer start for {} ({})", scope, path);
				Stopwatch stopwatch = Stopwatch.createStarted();
				compiledOutput = Autoprefixer.simple().process(compiledOutput);
				stopwatch.stop();
				LOGGER.debug("Autoprefixer end for {} ({}) : {} ms", scope, path, stopwatch.elapsed(TimeUnit.MILLISECONDS));
			}
			
			// Write result
			ScssStylesheetInformation compiledStylesheet = new ScssStylesheetInformation(scssPath, compiledOutput);
			
			for (String sourceUri : importer.getSourceUris()) {
				long lastModified = classPathResourceUtil.lastModified(sourceUri);
				compiledStylesheet.addImportedStylesheet(new ScssStylesheetInformation(sourceUri, lastModified));
			}
			
			return compiledStylesheet;
		} catch (IOException | URISyntaxException | CompilationException | AutoprefixerException e) {
			throw new RuntimeException(String.format("Error compiling %s", scssPath), e);
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
		return "classpath:/" + ClassPathUtils.toFullyQualifiedPath(scope, path);
	}

}
