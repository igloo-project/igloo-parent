package fr.openwide.core.jpa.more.rendering.service;

import java.util.Locale;

import fr.openwide.core.commons.util.context.IExecutionContext;
import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.context.IContextualService;


@SuppressWarnings("deprecation")
public interface IRendererService extends IContextualService {
	
	IExecutionContext context();
	
	IExecutionContext context(Locale locale);
	
	<T> IRenderer<? super T> findRenderer(Class<T> valueType);

	<T> IRenderer<? super T> findRenderer(Class<?> rootType, Class<T> valueType);

	<T> IRenderer<? super T> findRenderer(Class<?> rootType, FieldPath path, Class<T> valueType);

	String localize(String resourceKey);

	String localize(String resourceKey, Object namedParameters, final Object ... positionalParameters);

	String localize(Enum<?> enumValue, String prefix, String suffix);

	<T> String localize(Class<T> clazz, T value);

	String localize(String resourceKey, Locale locale);

	String localize(String resourceKey, Locale locale, Object namedParameters, final Object ... positionalParameters);

	String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale);

	<T> String localize(Class<T> clazz, T value, Locale locale);
	
}