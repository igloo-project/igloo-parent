package fr.openwide.core.wicket.more.rendering.service;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.util.convert.IConverter;
import org.bindgen.BindingRoot;
import org.javatuples.Triplet;

import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;
import fr.openwide.core.wicket.more.notification.service.AbstractBackgroundWicketThreadContextBuilder;
import fr.openwide.core.wicket.more.rendering.EnumRenderer;
import fr.openwide.core.wicket.more.rendering.Renderer;

public abstract class AbstractRendererServiceImpl extends AbstractBackgroundWicketThreadContextBuilder
		implements IRendererService, IRendererRegistryService {
	
	private Map<Triplet<Class<?>, FieldPath, Class<?>>, Renderer<?>> cache = Maps.newHashMap();

	private Map<Triplet<Class<?>, FieldPath, Class<?>>, Renderer<?>> renderers = Maps.newHashMap();

	@Override
	@SuppressWarnings("unchecked")
	public final <T> Renderer<? super T> findRenderer(Class<?> rootType, FieldPath path, Class<T> valueType) {
		Triplet<Class<?>, FieldPath, Class<?>> parameters = Triplet.<Class<?>, FieldPath, Class<?>>with(rootType, path, valueType);
		
		Renderer<? super T> renderer = (Renderer<? super T>) cache.get(parameters);
		if (renderer != null) {
			return renderer;
		} else {
			renderer = findRendererNoCache(rootType, path, valueType);
			cache.put(parameters, renderer);
			return renderer;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> Renderer<? super T> findRendererNoCache(Class<?> rootType, FieldPath path, Class<T> valueType) {
		Renderer<? super T> renderer = (Renderer<? super T>) renderers.get(Triplet.with(rootType, path, valueType));
		if (renderer != null) {
			return renderer;
		}
		renderer = (Renderer<? super T>) renderers.get(Triplet.with(rootType, path, null));
		if (renderer != null) {
			return renderer;
		}
		renderer = (Renderer<? super T>) renderers.get(Triplet.with(rootType, null, valueType));
		if (renderer != null) {
			return renderer;
		}
		renderer = (Renderer<? super T>) renderers.get(Triplet.with(null, null, valueType));
		if (renderer != null) {
			return renderer;
		}
		return findDefaultRenderer(valueType);
	}
	
	protected final <T> Renderer<? super T> findDefaultRenderer(final Class<T> valueType) {
		IConverter<T> converter;
		try {
			converter = runWithContext(new Callable<IConverter<T>>() {
				@Override
				public IConverter<T> call() throws Exception {
					return Application.get().getConverterLocator().getConverter(valueType);
				}
			});
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected exception", e);
		}
		return new BackgroundWicketThreadContextRenderer<T>(converter).nullsAsNull();
	}
	
	/**
	 * Wrapper which allows to be sure that a renderer will have access to all the elements provided by Wicket
	 * (especially the Localizer) when it's executed.
	 */
	private class BackgroundWicketThreadContextRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3035924297487452645L;
		private final IConverter<? super T> delegate;
		public BackgroundWicketThreadContextRenderer(IConverter<? super T> delegate) {
			super();
			this.delegate = delegate;
		}
		@Override
		public String render(final T value, final Locale locale) {
			try {
				return runWithContext(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return delegate.convertToString(value, locale);
					}
				});
			} catch (Exception e) {
				throw new IllegalStateException("Unexpected exception", e);
			}
		}
	}

	@Override
	public final <T> Renderer<? super T> findRenderer(Class<?> rootType, Class<T> valueType) {
		return findRenderer(rootType, null, valueType);
	}

	@Override
	public final <T> Renderer<? super T> findRenderer(Class<T> valueType) {
		return findRenderer(null, null, valueType);
	}

	@Override
	public final <R, T> void registerRenderer(Class<R> rootType, FieldPath path, Class<T> valueType, Renderer<? super T> renderer) {
		cache.clear();
		renderers.put(
				Triplet.<Class<?>, FieldPath, Class<?>> with(
						rootType,
						path,
						valueType
				),
				new BackgroundWicketThreadContextRenderer<T>(renderer).nullsAsNull()
		);
	}

	@Override
	public final <R, T> void registerRenderer(Class<R> rootType, BindingRoot<R, T> binding, Class<T> valueType,
			Renderer<? super T> renderer) {
		registerRenderer(rootType, FieldPath.fromBinding(binding), valueType, renderer);
	}

	@Override
	public final <T> void registerRenderer(Class<?> rootType, Class<T> valueType, Renderer<? super T> renderer) {
		registerRenderer(rootType, (FieldPath) null, valueType, renderer);
	}

	@Override
	public final <R, T> void registerRenderer(Class<R> rootType, BindingRoot<R, T> binding, Renderer<? super T> renderer) {
		registerRenderer(rootType, FieldPath.fromBinding(binding), null, renderer);
	}

	@Override
	public final <R, T> void registerRenderer(Class<R> rootType, FieldPath path, Renderer<? super T> renderer) {
		registerRenderer(rootType, path, null, renderer);
	}

	@Override
	public final <T> void registerRenderer(Class<T> valueType, Renderer<? super T> renderer) {
		registerRenderer(null, (FieldPath) null, valueType, renderer);
	}

	@Override
	public String localize(String resourceKey, Locale locale) {
		return Localizer.get().getString(resourceKey, null, null, locale, null, (String) null);
	}

	@Override
	public String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale) {
		return EnumRenderer.with(prefix, suffix).render(enumValue, locale);
	}

	@Override
	public <T> String localize(Class<T> clazz, T value, Locale locale) {
		return Application.get().getConverterLocator().getConverter(clazz).convertToString(value, locale);
	}

}
