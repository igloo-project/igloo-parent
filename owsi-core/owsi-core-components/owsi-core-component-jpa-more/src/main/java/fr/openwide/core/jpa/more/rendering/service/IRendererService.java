package fr.openwide.core.jpa.more.rendering.service;

import java.util.Locale;

import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.context.IContextualService;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;


public interface IRendererService extends IContextualService {
	
	<T> IRenderer<? super T> findRenderer(Class<T> valueType);

	<T> IRenderer<? super T> findRenderer(Class<?> rootType, Class<T> valueType);

	<T> IRenderer<? super T> findRenderer(Class<?> rootType, FieldPath path, Class<T> valueType);

	String localize(String resourceKey, Locale locale);

	String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale);

	<T> String localize(Class<T> clazz, T value, Locale locale);
	
}
