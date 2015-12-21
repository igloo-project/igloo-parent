package fr.openwide.core.jpa.more.rendering.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
public class EmptyRendererServiceImpl implements IRendererService {
	
	private enum DefaultRenderer implements IRenderer<Object> {
		INSTANCE;
		@Override
		public String render(Object value, Locale locale) {
			return String.valueOf(value);
		}
	}
	
	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return callable.call();
	}

	@Override
	public <T> IRenderer<? super T> findRenderer(Class<T> valueType) {
		return DefaultRenderer.INSTANCE;
	}

	@Override
	public <T> IRenderer<? super T> findRenderer(Class<?> rootType, Class<T> valueType) {
		return DefaultRenderer.INSTANCE;
	}

	@Override
	public <T> IRenderer<? super T> findRenderer(Class<?> rootType, FieldPath path, Class<T> valueType) {
		return DefaultRenderer.INSTANCE;
	}

	@Override
	public String localize(String resourceKey, Locale locale) {
		return null;
	}

	@Override
	public String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale) {
		return null;
	}

	@Override
	public <T> String localize(Class<T> clazz, T value, Locale locale) {
		return null;
	}
	
}
