package fr.openwide.core.wicket.gmap.resource;

import java.util.Locale;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class GMarkerImageResourceReference extends ResourceReference implements IResource {
	private static final long serialVersionUID = 1L;

	public GMarkerImageResourceReference(Class<?> scope, String name) {
		this(scope, name, null, null, null);
	}
	
	public GMarkerImageResourceReference(Class<?> scope, String name, Locale locale, String style, String variation) {
		super(scope, name, locale, style, variation);
	}

	@Override
	public IResource getResource() {
		return new GMarkerImageResourceReference(getScope(), getName(), getLocale(), getStyle(), getVariation());
	}

	@Override
	public void respond(Attributes attributes) {
	}
}
