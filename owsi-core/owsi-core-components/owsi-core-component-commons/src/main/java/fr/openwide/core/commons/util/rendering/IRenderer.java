package fr.openwide.core.commons.util.rendering;

import java.util.Locale;

/**
 * An object that can convert an object of type {@code T} to a String for a given {@link Locale}.
 * <p>Note: you probably want to extend WicketRenderer instead of implementing this. This class is primarily intended
 * to serve as a wicket-independant base.
 */
public interface IRenderer<T> {
	
	String render(T value, Locale locale);

}
