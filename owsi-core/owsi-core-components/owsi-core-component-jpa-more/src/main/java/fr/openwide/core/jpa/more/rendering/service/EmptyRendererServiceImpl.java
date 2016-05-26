package fr.openwide.core.jpa.more.rendering.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import fr.openwide.core.commons.util.context.ExecutionContexts;
import fr.openwide.core.commons.util.context.IExecutionContext;
import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.commons.util.functional.Joiners;
import fr.openwide.core.commons.util.rendering.IRenderer;

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
	public IExecutionContext context() {
		return ExecutionContexts.noOp();
	}
	
	@Override
	public IExecutionContext context(Locale locale) {
		return ExecutionContexts.noOp();
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
	public String localize(String resourceKey) {
		return resourceKey;
	}

	@Override
	public String localize(String resourceKey, Object namedParameters, Object... positionalParameters) {
		return resourceKey + "(" + namedParameters + ", " + positionalParameters + ")";
	}

	@Override
	public String localize(Enum<?> enumValue, String prefix, String suffix) {
		return Joiners.onDot().join(prefix, enumValue, suffix);
	}

	@Override
	public <T> String localize(Class<T> clazz, T value) {
		return clazz.getName() + " / " + value;
	}

	@Override
	public String localize(String resourceKey, Locale locale) {
		return localize(resourceKey);
	}

	@Override
	public String localize(String resourceKey, Locale locale, Object namedParameters, Object... positionalParameters) {
		return localize(resourceKey, namedParameters, positionalParameters);
	}

	@Override
	public String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale) {
		return localize(enumValue, prefix, suffix);
	}

	@Override
	public <T> String localize(Class<T> clazz, T value, Locale locale) {
		return localize(clazz, value);
	}
	
}
