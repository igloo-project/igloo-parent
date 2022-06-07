package org.iglooproject.wicket.more.rendering.service;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.renderer.Renderer;
import org.javatuples.Triplet;

import com.google.common.collect.Maps;

public class RendererServiceImpl implements IRendererService, IRendererRegistry {
	
	private Map<Triplet<Class<?>, FieldPath, Class<?>>, Renderer<?>> cache = Maps.newHashMap();

	private Map<Triplet<Class<?>, FieldPath, Class<?>>, Renderer<?>> renderers = Maps.newHashMap();
	
	private IWicketContextProvider wicketContextProvider;
	
	public RendererServiceImpl(IWicketContextProvider wicketContextProvider) {
		super();
		this.wicketContextProvider = wicketContextProvider;
	}
	
	@Override
	public IExecutionContext context() {
		return wicketContextProvider.context();
	}
	
	@Override
	public IExecutionContext context(Locale locale) {
		return wicketContextProvider.context(locale);
	}

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
	
	private <T> Renderer<? super T> findDefaultRenderer(final Class<T> valueType) {
		IConverter<T> converter;
		try (ITearDownHandle handle = context().open()) {
			converter = Application.get().getConverterLocator().getConverter(valueType);
		}
		return new BackgroundWicketThreadContextRenderer<T>(converter).nullsAsNull();
	}
	
	/**
	 * Wrapper which makes sure that a renderer will have access to all the elements provided by Wicket
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
			try (ITearDownHandle handle = context().open()) {
				return delegate.convertToString(value, locale);
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
	public final String localize(final String resourceKey) {
		return localize(resourceKey, null);
	}
	
	@Override
	public String localize(String resourceKey, Object namedParameters, Object... positionalParameters) {
		return localize(resourceKey, null, namedParameters, positionalParameters);
	}
	
	@Override
	public String localize(Enum<?> enumValue, String prefix, String suffix) {
		return localize(enumValue, prefix, suffix, null);
	}
	
	@Override
	public <T> String localize(Class<T> clazz, T value) {
		return localize(clazz, value, null);
	}

	@Override
	public final String localize(final String resourceKey, final Locale locale) {
		try (ITearDownHandle handle = context().open()) {
			return Localizer.get().getString(resourceKey, null, null, locale, null, (String) null);
		}
	}
	
	@Override
	public String localize(final String resourceKey, final Locale locale, final Object namedParameters, final Object... positionalParameters) {
		try (ITearDownHandle handle = context().open()) {
			IModel<?> modelParameter;
			if (namedParameters instanceof IModel) {
				modelParameter = (IModel<?>) namedParameters;
			} else {
				modelParameter = new IModel<Object>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Object getObject() {
						return namedParameters;
					}
				};
			}
			
			return new StringResourceModel(resourceKey)
				.setModel(modelParameter)
				.setParameters(positionalParameters)
				.wrapOnAssignment(new WebPage() {
					private static final long serialVersionUID = 1L;
					@Override
					public Locale getLocale() {
						return locale;
					}
				})
				.getObject();
		}
	}

	@Override
	public final String localize(final Enum<?> enumValue, final String prefix, final String suffix, final Locale locale) {
		try (ITearDownHandle handle = context().open()) {
			return EnumRenderer.with(prefix, suffix)
					.render(enumValue, locale != null ? locale : Session.get().getLocale());
		}
	}

	@Override
	public final <T> String localize(final Class<T> clazz, final T value, final Locale locale) {
		try (ITearDownHandle handle = context().open()) {
			return Application.get().getConverterLocator().getConverter(clazz)
					.convertToString(value, locale != null ? locale : Session.get().getLocale());
		}
	}

}
