package fr.openwide.core.wicket.more.lesscss;

import java.util.Locale;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class LessCssResourceReference extends ResourceReference {

	private static final long serialVersionUID = -1009491608247244399L;

	private boolean processLess;

	public LessCssResourceReference(Class<?> scope, String name) {
		this(scope, name, true);
	}
	
	public LessCssResourceReference(Class<?> scope, String name, boolean processLess) {
		this(scope, name, null, null, null, processLess);
	}

	public LessCssResourceReference(Class<?> scope, String name, Locale locale, String style, String variation,
			boolean processLess) {
		super(scope, name, locale, style, variation);
		this.processLess = processLess;
	}

	@Override
	public IResource getResource() {
		return new LessCssResource(getScope(), getName(), getLocale(), getStyle(), getVariation(), processLess);
	}

}