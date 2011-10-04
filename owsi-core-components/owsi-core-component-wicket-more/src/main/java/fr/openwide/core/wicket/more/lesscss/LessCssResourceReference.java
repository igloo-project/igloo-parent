package fr.openwide.core.wicket.more.lesscss;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.Application;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Packages;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.util.time.Time;

public class LessCssResourceReference extends ResourceReference {

	private static final long serialVersionUID = -1009491608247244399L;

	private boolean processLess;

	public LessCssResourceReference(Class<?> scope, String name) {
		this(scope, name, true);
	}

	public LessCssResourceReference(Class<?> scope, String name, boolean processLess) {
		super(scope, name);
		this.processLess = processLess;
	}

	@Override
	public Resource newResource() {
		return new LessCssResource(getScope(), getName(), processLess);
	}

	@Override
	public Time lastModifiedTime()
	{
		Time lastModifiedTime = lookupStream(getScope(), getName(), null, null).lastModifiedTime();
		
		String content;
		try {
			content = IOUtils.toString(lookupStream(getScope(), getName(), getLocale(), getStyle()).getInputStream(), "UTF-8");
		} catch (IOException e) {
			throw new WicketLessCssException(
					String.format("Error importing file %1$s", getName()), e);
		} catch (ResourceStreamNotFoundException e) {
			throw new WicketLessCssException(
					String.format("Error importing file %1$s", getName()), e);
		}
		
		Pattern pattern = LessCssUtil.LESSCSS_IMPORT_PATTERN;
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String filename = LessCssUtil.getRelativeToScopePath(getName(), matcher.group(1));
			
			IResourceStream resourceStream = lookupStream(getScope(), filename, null, null);
			if (resourceStream == null) {
				throw new WicketLessCssException(
						String.format("Error importing %1$s imported in %2$s ; resource not found", getName(), filename));
			}
			Time importedLastModifiedTime = resourceStream.lastModifiedTime();
			
			if (importedLastModifiedTime.after(lastModifiedTime)) {
				lastModifiedTime = importedLastModifiedTime;
			}
		}
		
		return lastModifiedTime;
	}
	
	private IResourceStream lookupStream(Class<?> scope, String path, Locale locale, String style) {
		IResourceStreamLocator locator = Application.get()
			.getResourceSettings()
			.getResourceStreamLocator();
		String absolutePath = Packages.absolutePath(scope, path);
		IResourceStream stream = locator.locate(scope, absolutePath, style, locale, null);
		return stream;
	}

}
