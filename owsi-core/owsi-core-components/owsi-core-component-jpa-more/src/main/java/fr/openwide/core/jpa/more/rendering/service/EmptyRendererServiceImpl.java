package fr.openwide.core.jpa.more.rendering.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import com.google.common.base.Joiner;

import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;

/**
 * Mock implementation, only used to fill the dependency.
 */
public class EmptyRendererServiceImpl implements IRendererService {
	
	protected enum DefaultRenderer implements IRenderer<Object> {
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
		return resourceKey;
	}

	@Override
	public String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale) {
		// TODO GSM: move to joiners once merged
		return Joiner.on(".").join(prefix, enumValue, suffix);
	}

	@Override
	public <T> String localize(Class<T> clazz, T value, Locale locale) {
		return clazz.getName() + " / " + value;
	}
	
}
