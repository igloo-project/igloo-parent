package fr.openwide.core.wicket.more.lesscss.service;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.lesscss.LessCssResource;
import fr.openwide.core.wicket.more.lesscss.WicketLessCssException;
import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;

@Service("lessCssService")
public class LessCssServiceImpl implements ILessCssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LessCssServiceImpl.class);
	
	private static final LessEngine LESS_ENGINE = new LessEngine();
	
	private static final Pattern LESSCSS_IMPORT_PATTERN =
			Pattern.compile("^\\p{Blank}*@import\\p{Blank}+\"([^\"]+)\"\\p{Blank}*;\\p{Blank}*$", Pattern.MULTILINE);
	
	@Override
	@Cacheable(value = "lessCssService.getCss",
			key = "T(fr.openwide.core.wicket.more.lesscss.service.LessCssServiceImpl).getCacheKey(#name, #locale, #style, #variation)")
	public CssStylesheetInformation getCss(IResourceStream resourceStream,
			Class<?> scope, String name, Locale locale, String style, String variation,
			boolean processLess) {
		
		return doGetCss(resourceStream, scope, name, locale, style, variation, processLess);
	}

	private CssStylesheetInformation doGetCss(IResourceStream resourceStream,
			Class<?> scope, String name, Locale locale, String style, String variation,
			boolean processLess) {
		String lessSource = null;
		Time lastModifiedTime = null;
		try {
			lessSource = IOUtils.toString(resourceStream.getInputStream(), "UTF-8");
			lastModifiedTime = resourceStream.lastModifiedTime();
		} catch (IOException e) {
			throw new WicketLessCssException(String.format("Error reading lesscss source for %1$s (%2$s, %3$s, %4$s)", name, locale, style, variation), e);
		} catch (ResourceStreamNotFoundException e) {
			throw new WicketLessCssException(String.format("Error reading lesscss source for %1$s (%2$s, %3$s, %4$s)", name, locale, style, variation), e);
		}
		
		Matcher matcher = LESSCSS_IMPORT_PATTERN.matcher(lessSource);
		
		LessCssResource importedResource;
		IResourceStream importedResourceStream = null;
		
		while (matcher.find()) {
			String filename = getRelativeToScopePath(name, matcher.group(1));
			
			try {
				importedResource = new LessCssResource(scope, filename, locale, style, variation, false);
				importedResourceStream = importedResource.getResourceStream();
				String imported;
			
				imported = IOUtils.toString(importedResourceStream.getInputStream());
				
				if (importedResourceStream.lastModifiedTime().after(lastModifiedTime)) {
					lastModifiedTime = importedResourceStream.lastModifiedTime();
				}
				
				lessSource = lessSource.replaceFirst(Matcher.quoteReplacement(matcher.group()), imported);
			} catch (IOException e) {
				throw new WicketLessCssException(
						String.format("Error reading lesscss source for %1$s in %2$s (%3$s, %4$s, %5$s)",
								filename, name, locale, style, variation), e);
			} catch (ResourceStreamNotFoundException e) {
				throw new WicketLessCssException(
						String.format("Error reading lesscss source for %1$s in %2$s (%3$s, %4$s, %5$s)",
								filename, name, locale, style, variation), e);
			} finally {
				if (importedResourceStream != null) {
					try {
						importedResourceStream.close();
					} catch (IOException e) {
						LOGGER.error(String.format("Error closing the resource stream for: %1$s", filename));
					}
				}
			}
		}
		
		String css;
		if (processLess) {
			try {
				css = LESS_ENGINE.compile(lessSource);
			} catch (LessException e) {
				throw new WicketLessCssException(String.format("Error compiling %1$s (%2$s, %3$s, %4$s)",
						name, locale, style, variation), e);
			}
		} else {
			css = lessSource;
		}
		return new CssStylesheetInformation(css, lastModifiedTime);
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
	
	public static String getCacheKey(String name, Locale locale, String style, String variation) {
		StringBuilder cacheKeyBuilder = new StringBuilder();
		cacheKeyBuilder.append(name);
		if (locale != null) {
			cacheKeyBuilder.append("-");
			cacheKeyBuilder.append(locale.getDisplayName());
		}
		cacheKeyBuilder.append("-");
		cacheKeyBuilder.append(style);
		cacheKeyBuilder.append("-");
		cacheKeyBuilder.append(variation);
		
		return cacheKeyBuilder.toString();
	}

}
