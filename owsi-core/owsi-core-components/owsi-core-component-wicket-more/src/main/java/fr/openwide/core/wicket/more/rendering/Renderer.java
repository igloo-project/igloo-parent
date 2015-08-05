package fr.openwide.core.wicket.more.rendering;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.Session;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.Functions2;
import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.wicket.more.util.IDatePattern;
import fr.openwide.core.wicket.more.util.model.Models;

/**
 * A one-way wicket converter: converts an object to a String.
 * <p>This class implements {@link IRenderer}, which is not tied to Wicket.
 */
public abstract class Renderer<T> implements IConverter<T>, IRenderer<T> {

	private static final long serialVersionUID = 4900817523619252790L;

	@Override
	public abstract String render(T value, Locale locale);

	/**
	 * Utility method that can be used when a resource key value is needed in the render() implementation.
	 */
	protected static String getString(String key, Locale locale) {
		return getString(key, locale, null);
	}

	/**
	 * Utility method that can be used when a resource key value is needed in the render() implementation.
	 */
	protected static String getString(String key, Locale locale, IModel<?> model) {
		return Localizer.get().getString(key, null, model, locale, null, null);
	}

	/**
	 * Utility method that can be used when a resource key value is needed in the render() implementation.
	 * <p>Returns Optional.absent() when the key is not found.
	 */
	protected static Optional<String> getStringOptional(String key, Locale locale) {
		return getStringOptional(key, locale, null);
	}

	/**
	 * Utility method that can be used when a resource key value is needed in the render() implementation.
	 * <p>Returns Optional.absent() when the key is not found.
	 */
	protected static Optional<String> getStringOptional(String key, Locale locale, IModel<?> model) {
		String defaultValue = new String(); // NOSONAR
		String result = Localizer.get().getString(key, null, model, locale, null, defaultValue);
		if (result == defaultValue) { // NOSONAR
			return Optional.absent();
		} else {
			return Optional.of(result);
		}
	}

	/**
	 * @deprecated Implemented to satisfy the {@link IConverter} interface; do not use this method.
	 * @throws UnsupportedOperationException Always.
	 */
	@Override
	@Deprecated
	public final T convertToObject(String value, Locale locale) throws ConversionException {
		throw new UnsupportedOperationException("Renderers do not implement convertToObject");
	}

	/**
	 * @deprecated Implemented to satisfy the {@link IConverter} interface; use {@link #render(Object, Locale)} instead.
	 */
	@Override
	@Deprecated
	public final String convertToString(T value, Locale locale) {
		return render(value, locale);
	}

	public Renderer<T> nullsAsNull() {
		return withDefault(Predicates.notNull(), NULL_RENDERER);
	}
	
	private static final Renderer<Object> NULL_RENDERER = new ConstantRenderer<Object>(null) {
		private static final long serialVersionUID = 5711969182760960576L;
		private Object readResolve() {
			return NULL_RENDERER;
		}
	};

	public Renderer<T> nullsAsBlank() {
		return withDefault(Predicates.notNull(), BLANK_RENDERER);
	}
	
	private static final Renderer<Object> BLANK_RENDERER = new ConstantRenderer<Object>("") {
		private static final long serialVersionUID = 5711969182760960576L;
		private Object readResolve() {
			return BLANK_RENDERER;
		}
	};
	
	public Renderer<T> nullsAs(T defaultValue) {
		return withDefault(Predicates.notNull(), defaultValue);
	}
	
	public Renderer<T> nullsAs(String defaultRendering) {
		return withDefault(Predicates.notNull(), constant(defaultRendering));
	}
	
	public Renderer<T> withDefault(Predicate<? super T> validValuePredicate, T defaultValue) {
		return onResultOf(Functions2.defaultValue(validValuePredicate, Functions.constant(defaultValue)));
	}
	
	public Renderer<T> withDefault(Predicate<? super T> validValuePredicate, IRenderer<? super T> defaultRenderer) {
		return new DefaultingRenderer<>(validValuePredicate, this, defaultRenderer);
	}
	
	private static class DefaultingRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final Predicate<? super T> validValuePredicate;
		private final IRenderer<? super T> validValueDelegate;
		private final IRenderer<? super T> invalidValueDelegate;
		
		public DefaultingRenderer(Predicate<? super T> validValuePredicate, IRenderer<? super T> validValueDelegate,
				IRenderer<? super T> invalidValueDelegate) {
			super();
			this.validValuePredicate = checkNotNull(validValuePredicate);
			this.validValueDelegate = checkNotNull(validValueDelegate);
			this.invalidValueDelegate = checkNotNull(invalidValueDelegate);
		}

		@Override
		public String render(T value, Locale locale) {
			if (validValuePredicate.apply(value)) {
				return validValueDelegate.render(value, locale);
			} else {
				return invalidValueDelegate.render(value, locale);
			}
		}
	}
	
	@SafeVarargs
	public final Renderer<T> append(Function<? super Locale, ? extends Joiner> joinerFunction, Renderer<? super T> ... appendedRenderers) {
		return new JoiningRenderer<>(joinerFunction, Lists.asList(this, appendedRenderers));
	}
	
	private static class JoiningRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final Function<? super Locale, ? extends Joiner> joinerFunction;
		private final Iterable<? extends Renderer<? super T>> renderers;

		public JoiningRenderer(Function<? super Locale, ? extends Joiner> joinerFunction, Iterable<? extends Renderer<? super T>> renderers) {
			super();
			this.joinerFunction = joinerFunction;
			this.renderers = renderers;
		}

		@Override
		public String render(final T value, final Locale locale) {
			checkNotNull(locale);
			Joiner joiner = joinerFunction.apply(locale);
			Iterable<String> renderedItems = Iterables.transform(renderers, new Function<Renderer<? super T>, String>() {
				@Override
				public String apply(Renderer<? super T> input) {
					return input.render(value, locale);
				}
			});
			return joiner.join(renderedItems);
		}
	}

	public <F> Renderer<F> onResultOf(Function<? super F, ? extends T> function) {
		return new ByFunctionRenderer<F, T>(function, this);
	}
	
	private static class ByFunctionRenderer<F, T> extends Renderer<F> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final Function<? super F, ? extends T> function;
		private final Renderer<T> delegate;
		
		public ByFunctionRenderer(Function<? super F, ? extends T> function, Renderer<T> delegate) {
			super();
			this.function = function;
			this.delegate = delegate;
		}
		
		@Override
		public String render(F value, Locale locale) {
			return delegate.render(function.apply(value), locale);
		}
	}
	
	/**
	 * Returns an empty string whenever the rendered value is null.
	 */
	public Renderer<T> orBlank() {
		return compose(Functions2.defaultValue(""));
	}

	public Renderer<T> compose(Function<? super String, ? extends String> function) {
		return new ComposeRenderer<T>(function, this);
	}
	
	private static class ComposeRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final Function<? super String, ? extends String> function;
		private final Renderer<T> delegate;
		
		public ComposeRenderer(Function<? super String, ? extends String> function, Renderer<T> delegate) {
			super();
			this.function = function;
			this.delegate = delegate;
		}
		
		@Override
		public String render(T value, Locale locale) {
			return function.apply(delegate.render(value, locale));
		}
	}

	public Function<T, String> asFunction(Locale locale) {
		return new RendererFunction(locale);
	}
	
	private class RendererFunction implements Function<T, String>, Serializable {
		private static final long serialVersionUID = -1080017215611311157L;
		
		private final Locale locale;

		public RendererFunction(Locale locale) {
			super();
			this.locale = checkNotNull(locale);
		}
		
		@Override
		public String apply(T input) {
			return Renderer.this.render(input, locale);
		}
	}

	public IModel<String> asModel(IModel<? extends T> valueModel) {
		return new RendererModel(valueModel);
	}
	
	private class RendererModel extends AbstractReadOnlyModel<String> implements IComponentAssignedModel<String> {
		private static final long serialVersionUID = -1080017215611311157L;
		
		private final IModel<? extends T> valueModel;

		public RendererModel(IModel<? extends T> valueModel) {
			super();
			this.valueModel = checkNotNull(valueModel);
		}

		@Override
		public String getObject() {
			return Renderer.this.render(valueModel.getObject(), Session.get().getLocale());
		}

		@Override
		public IWrapModel<String> wrapOnAssignment(Component component) {
			return new WrapModel(component);
		}
		
		private class WrapModel extends AbstractReadOnlyModel<String> implements IWrapModel<String> {
			private static final long serialVersionUID = -1080017215611311157L;
			
			private final Component component;
			
			public WrapModel(Component component) {
				super();
				this.component = checkNotNull(component);
			}
			
			@Override
			public String getObject() {
				return Renderer.this.render(valueModel.getObject(), component.getLocale());
			}
			
			@Override
			public IModel<?> getWrappedModel() {
				return RendererModel.this;
			}
		}
		
		@Override
		public void detach() {
			super.detach();
			valueModel.detach();
		}
	}
	
	public Renderer<T> withResourceKey(String resourceKey) {
		return new ResourceKeyWithRenderedParameterRenderer<>(resourceKey, this);
	}
	
	private static class ResourceKeyWithRenderedParameterRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 7436587266161009083L;
		
		private final Renderer<T> parameterRenderer;
		
		private final String resourceKey;

		public ResourceKeyWithRenderedParameterRenderer(String resourceKey, Renderer<T> parameterRenderer) {
			super();
			this.resourceKey = resourceKey;
			this.parameterRenderer = parameterRenderer;
		}

		@Override
		public String render(final T value, Locale locale) {
			IModel<?> model = parameterRenderer.asModel(new AbstractReadOnlyModel<T>() {
				private static final long serialVersionUID = 1L;
				@Override
				public T getObject() {
					return value;
				}
			});
			return Localizer.get().getString(resourceKey, null, model, locale, null, null);
		}
		
		@Override
		public String toString() {
			return "fromResourceKey(" + resourceKey + ")";
		}
	}
	
	/**
	 * @deprecated There is no reason to pass a WicketRenderer to {@code from()}. Use the renderer as-is.
	 */
	@Deprecated
	public static <T> Renderer<T> from(Renderer<? super T> renderer) {
		return from((IConverter<? super T>) renderer);
	}
	
	public static <T> Renderer<T> from(IConverter<? super T> converter) {
		return new FromConverterRenderer<>(converter);
	}
	
	private static class FromConverterRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 7436587266161009083L;
		
		private final IConverter<? super T> converter;

		public FromConverterRenderer(IConverter<? super T> converter) {
			super();
			this.converter = converter;
		}

		@Override
		public String render(T value, Locale locale) {
			return this.converter.convertToString(value, locale);
		}
		
		@Override
		public String toString() {
			return converter.toString();
		}
	}
	
	public static <T> Renderer<T> from(IRenderer<? super T> renderer) {
		return new FromRendererWicketRenderer<>(renderer);
	}
	
	private static class FromRendererWicketRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 7436587266161009083L;
		
		private final IRenderer<? super T> renderer;

		public FromRendererWicketRenderer(IRenderer<? super T> renderer) {
			super();
			this.renderer = renderer;
		}

		@Override
		public String render(T value, Locale locale) {
			return this.renderer.render(value, locale);
		}
		
		@Override
		public String toString() {
			return renderer.toString();
		}
	}
	
	public static <T> Renderer<T> constant(String value) {
		return new ConstantRenderer<>(value);
	}
	
	private static class ConstantRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 7436587266161009083L;
		
		private final String value;

		public ConstantRenderer(String value) {
			super();
			this.value = value;
		}

		@Override
		public String render(T value, Locale locale) {
			return this.value;
		}
		
		@Override
		public String toString() {
			return "constant(" + value + ")";
		}
	}
	
	@SuppressWarnings("unchecked") // Works for any T
	public static <T> Renderer<T> stringValue() {
		return (Renderer<T>) STRING_VALUE_RENDERER;
	}
	
	private static final Renderer<Object> STRING_VALUE_RENDERER = new Renderer<Object>() {
		private static final long serialVersionUID = 5711969182760960576L;
		@Override
		public String render(Object value, Locale locale) {
			return String.valueOf(value);
		}
		private Object readResolve() {
			return STRING_VALUE_RENDERER;
		}
	};
	
	public static <T extends Number> Renderer<T> fromNumberFormat(NumberFormat format) {
		return fromFormat(format);
	}
	
	public static <T extends Number> Renderer<T> fromNumberFormat(Function<? super Locale, ? extends NumberFormat> formatFunction) {
		return fromFormat(formatFunction);
	}
	
	public static <T extends Date> Renderer<T> fromDateFormat(DateFormat format) {
		return fromFormat(format);
	}
	
	public static <T extends Date> Renderer<T> fromDateFormat(Function<? super Locale, ? extends DateFormat> formatFunction) {
		return fromFormat(formatFunction);
	}
	
	protected static <T> Renderer<T> fromFormat(Format format) {
		checkNotNull(format);
		final Format copiedFormat = (Format) format.clone(); // Ignore changes on 'format'
		return new FormatRenderer<T>(new SerializableFunction<Locale, Format>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Format apply(Locale input) {
				return (Format) copiedFormat.clone();
			}
			@Override
			public String toString() {
				return copiedFormat.toString();
			}
		});
	}
	
	protected static <T> Renderer<T> fromFormat(Function<? super Locale, ? extends Format> formatFunction) {
		return new FormatRenderer<T>(formatFunction).nullsAsBlank();
	}
	
	private static class FormatRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = -2023856554950929671L;
		
		private final Function<? super Locale, ? extends Format> formatFunction;

		public FormatRenderer(Function<? super Locale, ? extends Format> formatFunction) {
			super();
			this.formatFunction = checkNotNull(formatFunction);
		}

		@Override
		public String render(Object value, Locale locale) {
			checkNotNull(locale);
			Format format = formatFunction.apply(locale);
			return format.format(value);
		}
	}
	
	public static Renderer<Iterable<?>> fromJoiner(Function<? super Locale, ? extends Joiner> joinerFunction) {
		return fromJoiner(joinerFunction, stringValue());
	}
	
	public static <T> Renderer<Iterable<? extends T>> fromJoiner(Function<? super Locale, ? extends Joiner> joinerFunction, Renderer<T> itemRenderer) {
		return new IterableJoinerRenderer<T>(joinerFunction, itemRenderer);
	}
	
	private static class IterableJoinerRenderer<T> extends Renderer<Iterable<? extends T>> {
		private static final long serialVersionUID = -3594965870562973846L;
		
		private final Function<? super Locale, ? extends Joiner> joinerFunction;
		private final Renderer<T> itemRenderer;

		public IterableJoinerRenderer(Function<? super Locale, ? extends Joiner> joinerFunction, Renderer<T> itemRenderer) {
			super();
			this.joinerFunction = checkNotNull(joinerFunction);
			this.itemRenderer = itemRenderer;
		}

		@Override
		public String render(Iterable<? extends T> value, Locale locale) {
			checkNotNull(locale);
			Joiner joiner = joinerFunction.apply(locale);
			Iterable<String> renderedItems = Iterables.transform(value, itemRenderer.asFunction(locale));
			return joiner.join(renderedItems);
		}
	}
	
	public static Renderer<Map<?, ?>> fromMapJoiner(Function<? super Locale, ? extends MapJoiner> joinerFunction) {
		return new MapJoinerRenderer(joinerFunction);
	}
	
	private static class MapJoinerRenderer extends Renderer<Map<?, ?>> {
		private static final long serialVersionUID = 200989454412696381L;
		
		private final Function<? super Locale, ? extends MapJoiner> joinerFunction;

		public MapJoinerRenderer(Function<? super Locale, ? extends MapJoiner> joinerFunction) {
			super();
			this.joinerFunction = checkNotNull(joinerFunction);
		}

		@Override
		public String render(Map<?, ?> value, Locale locale) {
			checkNotNull(locale);
			MapJoiner joiner = joinerFunction.apply(locale);
			return joiner.join(value);
		}
	}
	
	public static <T> Renderer<T> fromResourceKey(String resourceKey) {
		return new FromResourceKeyRenderer<>(resourceKey);
	}
	
	private static class FromResourceKeyRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 7436587266161009083L;
		
		private final String resourceKey;

		public FromResourceKeyRenderer(String resourceKey) {
			super();
			this.resourceKey = resourceKey;
		}

		@Override
		public String render(final Object value, Locale locale) {
			IModel<?> model = Models.transientModel(value);
			return Localizer.get().getString(resourceKey, null, model, locale, null, null);
		}
		
		@Override
		public String toString() {
			return "fromResourceKey(" + resourceKey + ")";
		}
	}
	
	public static <T extends Date> Renderer<T> fromDatePattern(final IDatePattern datePattern) {
		Renderer<T> renderer = fromFormat(new SerializableFunction<Locale, DateFormat>() {
			private static final long serialVersionUID = 1L;
			@Override
			public DateFormat apply(Locale locale) {
				return new SimpleDateFormat(Localizer.get().getString(datePattern.getJavaPatternKey(), null, null, locale, null, null), locale);
			}
		});
		if (datePattern.capitalize()) {
			renderer = renderer.compose(new SerializableFunction<String, String>() {
				private static final long serialVersionUID = 1L;
				@Override
				public String apply(String input) {
					return WordUtils.capitalize(input);
				}
			});
		}
		return renderer;
	}
	
	public static <T extends Number> Renderer<T> count(String resourceKeyPrefix) {
		return count(resourceKeyPrefix, null);
	}
	
	public static <T extends Number> Renderer<T> count(String resourceKeyPrefix, Renderer<? super T> numberRenderer) {
		return new CountRenderer<>(resourceKeyPrefix, numberRenderer).nullsAsNull();
	}
	
	private static class CountRenderer<T extends Number> extends Renderer<T> {
		private static final long serialVersionUID = 7436587266161009083L;
		
		private final String resourceKeyPrefix;
		
		private final Renderer<? super T> numberRenderer;

		public CountRenderer(String resourceKeyPrefix, Renderer<? super T> numberRenderer) {
			super();
			this.resourceKeyPrefix = resourceKeyPrefix;
			this.numberRenderer = numberRenderer;
		}

		@Override
		public String render(final T value, Locale locale) {
			IModel<T> model = Models.transientModel(value);
			IModel<Map<String, Object>> mapModel;
			
			if (numberRenderer  != null) {
				mapModel = Models.dataMap().put("count", numberRenderer.asModel(model)).build();
			} else {
				mapModel = Models.dataMap().put("count", value).build();
			}
			
			StringBuilder resourceKeyBuilder = new StringBuilder(resourceKeyPrefix);
			double doubleValue = value.doubleValue();
			
			if (doubleValue == 0) {
				resourceKeyBuilder.append(".zero");
			} else if (-1 <= doubleValue && doubleValue <= 1) {
				resourceKeyBuilder.append(".one");
			} else {
				resourceKeyBuilder.append(".many");
			}
			
			return Localizer.get().getString(resourceKeyBuilder.toString(), null, mapModel, locale, null, null);
		}
	}

}
