package fr.openwide.core.wicket.more.lesscss;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.time.Time;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

public class LessCssResource extends PackageResource {

	private static final long serialVersionUID = 9067443522105165705L;

	private boolean processLess;

	private String name;

	private Locale locale;

	private String variation;

	public LessCssResource(Class<?> scope, String name) {
		this(scope, name, null, null, null);
	}

	public LessCssResource(Class<?> scope, String name, Locale locale, String style, String variation) {
		this(scope, name, locale, style, variation, true);
	}

	public LessCssResource(Class<?> scope, String name, Locale locale, String style, String variation, boolean processLess) {
		super(scope, name, locale, style, variation);
		
		this.name = name;
		this.locale = locale;
		this.variation = variation;
		this.processLess = processLess;
	}

	public String getName() {
		return name;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getVariation() {
		return variation;
	}

	@Override
	public IResourceStream getResourceStream() {
		IResourceStream resourceStream = super.getResourceStream();
		
		if (resourceStream != null) {
			String lessSource = null;
			Time lastModifiedTime = null;
			try {
				lessSource = IOUtils.toString(resourceStream.getInputStream(), "UTF-8");
				lastModifiedTime = resourceStream.lastModifiedTime();
			} catch (IOException e) {
				throw new WicketLessCssException(String.format("Error reading lesscss source for %1$s (%2$s, %3$s, %4$s)", getName(), getLocale(), getStyle(), getVariation()), e);
			} catch (ResourceStreamNotFoundException e) {
				throw new WicketLessCssException(String.format("Error reading lesscss source for %1$s (%2$s, %3$s, %4$s)", getName(), getLocale(), getStyle(), getVariation()), e);
			}
			
			Pattern pattern = LessCssUtil.LESSCSS_IMPORT_PATTERN;
			Matcher matcher = pattern.matcher(lessSource);
			while (matcher.find()) {
				String filename = LessCssUtil.getRelativeToScopePath(getName(), matcher.group(1));
				
				LessCssResource importedResource = new LessCssResource(getScope(), filename, getLocale(), getStyle(), getVariation(), false);
				IResourceStream importedResourceStream = importedResource.getResourceStream();
				String imported;
				try {
					imported = IOUtils.toString(importedResourceStream.getInputStream());
					
					if (importedResourceStream.lastModifiedTime().after(lastModifiedTime)) {
						lastModifiedTime = importedResourceStream.lastModifiedTime();
					}
					
					lessSource = lessSource.replaceFirst(Matcher.quoteReplacement(matcher.group()), imported);
				} catch (IOException e) {
					throw new WicketLessCssException(
							String.format("Error reading lesscss source for %1$s in %2$s (%3$s, %4$s, %5$s)", filename, getName(), getLocale(), getStyle(), getVariation()), e);
				} catch (ResourceStreamNotFoundException e) {
					throw new WicketLessCssException(
							String.format("Error reading lesscss source for %1$s in %2$s (%3$s, %4$s, %5$s)", filename, getName(), getLocale(), getStyle(), getVariation()), e);
				}
			}
			
			String css;
			if (processLess) {
				LessEngine less = new LessEngine();
				try {
					css = less.compile(lessSource);
				} catch (LessException e) {
					throw new WicketLessCssException(String.format("Error compiling %1$s (%2$s, %3$s, %4$s)", getName(), getLocale(), getStyle(), getVariation()), e);
				}
			} else {
				css = lessSource;
			}
			StringResourceStream lessCssResourceStream = new StringResourceStream(css, "text/css");
			lessCssResourceStream.setCharset(Charset.forName("UTF-8"));
			lessCssResourceStream.setLastModified(lastModifiedTime);
			
			return lessCssResourceStream;
		} else {
			throw new WicketLessCssException(String.format("Error reading lesscss source for %1$s (%2$s, %3$s, %4$s)", getName(), getLocale(), getStyle(), getVariation()));
		}
	}

}
