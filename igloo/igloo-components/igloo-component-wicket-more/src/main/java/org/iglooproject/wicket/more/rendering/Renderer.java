package org.iglooproject.wicket.more.rendering;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.apache.wicket.Localizer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.api.Models;
import org.iglooproject.wicket.api.model.LocaleAwareReadOnlyModel;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.api.util.IDatePattern;
import org.iglooproject.wicket.markup.html.basic.AbstractCoreLabel;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Streams;

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
		return Localizer.get().getString(key, null, model, locale, null, (IModel<String>) null);
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
		return withDefault(Predicates2.notNull(), NULL_RENDERER);
	}
	
	private static final Renderer<Object> NULL_RENDERER = new ConstantRenderer<Object>(null) {
		private static final long serialVersionUID = 5711969182760960576L;
		private Object readResolve() {
			return NULL_RENDERER;
		}
	};

	public Renderer<T> nullsAsBlank() {
		return withDefault(Predicates2.notNull(), BLANK_RENDERER);
	}
	
	private static final Renderer<Object> BLANK_RENDERER = new ConstantRenderer<Object>("") {
		private static final long serialVersionUID = 5711969182760960576L;
		private Object readResolve() {
			return BLANK_RENDERER;
		}
	};
	
	public Renderer<T> nullsAs(T defaultValue) {
		return withDefault(Predicates2.notNull(), defaultValue);
	}
	
	public Renderer<T> nullsAs(IRenderer<? super T> defaultRenderer) {
		return withDefault(Predicates2.notNull(), defaultRenderer);
	}
	
	public Renderer<T> nullsAsResourceKey(String resourceKey) {
		return withDefault(Predicates2.notNull(), withResourceKey(resourceKey));
	}
	
	public Renderer<T> nullsAsConstant(String defaultRendering) {
		return withDefault(Predicates2.notNull(), constant(defaultRendering));
	}
	
	public Renderer<T> withDefault(SerializablePredicate2<? super T> validValuePredicate, T defaultValue) {
		return onResultOf(Functions2.defaultValue(validValuePredicate, Functions2.constant(defaultValue)));
	}
	
	public Renderer<T> withDefault(SerializablePredicate2<? super T> validValuePredicate, IRenderer<? super T> defaultRenderer) {
		return new DefaultingRenderer<>(validValuePredicate, this, defaultRenderer);
	}
	
	private static class DefaultingRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final SerializablePredicate2<? super T> validValuePredicate;
		private final IRenderer<? super T> validValueDelegate;
		private final IRenderer<? super T> invalidValueDelegate;
		
		public DefaultingRenderer(SerializablePredicate2<? super T> validValuePredicate, IRenderer<? super T> validValueDelegate,
				IRenderer<? super T> invalidValueDelegate) {
			super();
			this.validValuePredicate = checkNotNull(validValuePredicate);
			this.validValueDelegate = checkNotNull(validValueDelegate);
			this.invalidValueDelegate = checkNotNull(invalidValueDelegate);
		}

		@Override
		public String render(T value, Locale locale) {
			if (validValuePredicate.test(value)) {
				return validValueDelegate.render(value, locale);
			} else {
				return invalidValueDelegate.render(value, locale);
			}
		}
	}
	
	@SafeVarargs
	public final Renderer<T> append(SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction, Renderer<? super T> ... appendedRenderers) {
		return new JoiningRenderer<>(joinerFunction, Lists.asList(this, appendedRenderers));
	}
	
	private static class JoiningRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction;
		private final Iterable<? extends Renderer<? super T>> renderers;

		public JoiningRenderer(SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction, Iterable<? extends Renderer<? super T>> renderers) {
			super();
			this.joinerFunction = joinerFunction;
			this.renderers = renderers;
		}

		@Override
		public String render(final T value, final Locale locale) {
			checkNotNull(locale);
			checkNotNull(renderers);
			return joinerFunction.apply(locale).join(
					Streams.stream(renderers)
							.map(input -> input.render(value, locale))
							.iterator()
			);
		}
	}

	public <F> Renderer<F> onResultOf(SerializableFunction2<? super F, ? extends T> function) {
		return new ByFunctionRenderer<>(function, this);
	}
	
	private static class ByFunctionRenderer<F, T> extends Renderer<F> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final SerializableFunction2<? super F, ? extends T> function;
		private final Renderer<T> delegate;
		
		public ByFunctionRenderer(SerializableFunction2<? super F, ? extends T> function, Renderer<T> delegate) {
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

	public Renderer<T> compose(SerializableFunction2<? super String, ? extends String> function) {
		return new ComposeRenderer<>(function, this);
	}
	
	private static class ComposeRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 3566036942853574753L;
		
		private final SerializableFunction2<? super String, ? extends String> function;
		private final Renderer<T> delegate;
		
		public ComposeRenderer(SerializableFunction2<? super String, ? extends String> function, Renderer<T> delegate) {
			super();
			this.function = function;
			this.delegate = delegate;
		}
		
		@Override
		public String render(T value, Locale locale) {
			return function.apply(delegate.render(value, locale));
		}
	}

	public SerializableFunction2<T, String> asFunction(Locale locale) {
		return new RendererFunction(locale);
	}
	
	private class RendererFunction implements SerializableFunction2<T, String>, Serializable {
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
	
	private class RendererModel extends LocaleAwareReadOnlyModel<String> {
		private static final long serialVersionUID = -1080017215611311157L;
		
		private final IModel<? extends T> valueModel;
		
		public RendererModel(IModel<? extends T> valueModel) {
			super();
			this.valueModel = checkNotNull(valueModel);
		}
		
		@Override
		public String getObject(Locale locale) {
			return Renderer.this.render(valueModel.getObject(), locale);
		}
		
		@Override
		public void detach() {
			super.detach();
			Detachables.detach(valueModel);
		}
	}
	
	public AbstractCoreLabel<?> asLabel(String id, IModel<? extends T> valueModel) {
		return new CoreLabel(id, asModel(valueModel));
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
			IModel<?> model = parameterRenderer.asModel(new IModel<T>() {
				private static final long serialVersionUID = 1L;
				@Override
				public T getObject() {
					return value;
				}
			});
			return Renderer.getString(resourceKey, locale, model);
		}
		
		@Override
		public String toString() {
			return "fromResourceKey(" + resourceKey + ")";
		}
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
	
	public static <T extends Number> Renderer<T> fromNumberFormat(SerializableFunction2<? super Locale, ? extends NumberFormat> formatFunction) {
		return fromFormat(formatFunction);
	}
	
	public static <T extends Date> Renderer<T> fromDateFormat(DateFormat format) {
		return fromFormat(format);
	}
	
	public static <T extends Date> Renderer<T> fromDateFormat(SerializableFunction2<? super Locale, ? extends DateFormat> formatFunction) {
		return fromFormat(formatFunction);
	}
	
	protected static <T> Renderer<T> fromFormat(Format format) {
		checkNotNull(format);
		final Format copiedFormat = (Format) format.clone(); // Ignore changes on 'format'
		return new FormatRenderer<>(locale -> (Format) copiedFormat.clone());
	}
	
	protected static <T> Renderer<T> fromFormat(SerializableFunction2<? super Locale, ? extends Format> formatFunction) {
		return new FormatRenderer<T>(formatFunction).nullsAsBlank();
	}
	
	private static class FormatRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = -2023856554950929671L;
		
		private final SerializableFunction2<? super Locale, ? extends Format> formatFunction;

		public FormatRenderer(SerializableFunction2<? super Locale, ? extends Format> formatFunction) {
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
	
	public static Renderer<Iterable<?>> fromJoiner(SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction) {
		return fromJoiner(joinerFunction, stringValue());
	}
	
	public static <T> Renderer<Iterable<? extends T>> fromJoiner(SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction, Renderer<T> itemRenderer) {
		return new IterableJoinerRenderer<>(joinerFunction, itemRenderer);
	}
	
	private static class IterableJoinerRenderer<T> extends Renderer<Iterable<? extends T>> {
		private static final long serialVersionUID = -3594965870562973846L;
		
		private final SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction;
		private final Renderer<T> itemRenderer;

		public IterableJoinerRenderer(SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction, Renderer<T> itemRenderer) {
			super();
			this.joinerFunction = checkNotNull(joinerFunction);
			this.itemRenderer = itemRenderer;
		}

		@Override
		public String render(Iterable<? extends T> value, Locale locale) {
			checkNotNull(locale);
			checkNotNull(value);
			return joinerFunction.apply(locale).join(
					Streams.stream(value)
							.map(itemRenderer.asFunction(locale))
							.iterator()
			);
		}
	}
	
	public static Renderer<Map<?, ?>> fromMapJoiner(SerializableFunction2<? super Locale, ? extends MapJoiner> joinerFunction) {
		return new MapJoinerRenderer(joinerFunction);
	}
	
	private static class MapJoinerRenderer extends Renderer<Map<?, ?>> {
		private static final long serialVersionUID = 200989454412696381L;
		
		private final SerializableFunction2<? super Locale, ? extends MapJoiner> joinerFunction;

		public MapJoinerRenderer(SerializableFunction2<? super Locale, ? extends MapJoiner> joinerFunction) {
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
	
	public static <K, V> Renderer<Map<? extends K, ? extends V>> mapRenderer(
			SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction,
			SerializableFunction2<? super Locale, ? extends Joiner> joinerKeyValueFunction,
			Renderer<K> keyRenderer,
			Renderer<V> valueRenderer
	) {
		return new MapRenderer<>(joinerFunction, joinerKeyValueFunction, keyRenderer, valueRenderer);
	}

	private static class MapRenderer<K, V> extends Renderer<Map<? extends K, ? extends V>> {
		private static final long serialVersionUID = 200989454412696381L;

		private final SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction;
		private final SerializableFunction2<? super Locale, ? extends Joiner> joinerKeyValueFunction;
		private final Renderer<K> keyRenderer;
		private final Renderer<V> valueRenderer;

		public MapRenderer(
				SerializableFunction2<? super Locale, ? extends Joiner> joinerFunction,
				SerializableFunction2<? super Locale, ? extends Joiner> joinerKeyValueFunction,
				Renderer<K> keyRenderer,
				Renderer<V> valueRenderer
		) {
			super();
			this.joinerFunction = checkNotNull(joinerFunction);
			this.joinerKeyValueFunction = checkNotNull(joinerKeyValueFunction);
			this.keyRenderer = checkNotNull(keyRenderer);
			this.valueRenderer = checkNotNull(valueRenderer);
		}

		@Override
		public String render(Map<? extends K, ? extends V> value, final Locale locale) {
			checkNotNull(locale);
			Joiner joiner = joinerFunction.apply(locale);
			final Joiner joinerKeyValue = joinerKeyValueFunction.apply(locale);
			
			return joiner.join(
					value.entrySet()
							.stream()
							.map(input -> joinerKeyValue.join(
									keyRenderer.render(input.getKey(), locale),
									valueRenderer.render(input.getValue(), locale)
							))
							.iterator()
			);
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
			return Renderer.getString(resourceKey, locale, model);
		}
		
		@Override
		public String toString() {
			return "fromResourceKey(" + resourceKey + ")";
		}
	}
	
	public static <T> Renderer<T> fromStringFormat(String stringFormat) {
		return new FromStringFormatRenderer<>(stringFormat);
	}
	
	private static class FromStringFormatRenderer<T> extends Renderer<T> {
		private static final long serialVersionUID = 1L;
		
		private final String stringFormat;
		
		public FromStringFormatRenderer(String stringFormat) {
			super();
			this.stringFormat = stringFormat;
		}
		
		@Override
		public String render(final Object value, Locale locale) {
			return String.format(locale, stringFormat, value);
		}
		
		@Override
		public String toString() {
			return "fromStringFormat(" + stringFormat + ")";
		}
	}
	
	public static <T extends Date> Renderer<T> fromDatePattern(final IDatePattern datePattern) {
		Renderer<T> renderer = fromFormat(
				locale -> new SimpleDateFormat(Localizer.get().getString(datePattern.getJavaPatternKey(), null, null, locale, null, (IModel<String>) null), locale)
		);
		if (datePattern.capitalize()) {
			renderer = renderer.compose(Functions2.capitalize());
		}
		return renderer;
	}
	
	public static <T extends Number> Renderer<T> count(String resourceKeyPrefix) {
		return count(resourceKeyPrefix, null);
	}
	
	public static <T extends Number> Renderer<T> count(String resourceKeyPrefix, Renderer<? super T> numberRenderer) {
		return new CountRenderer<T>(resourceKeyPrefix, numberRenderer).nullsAsNull();
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
			return Renderer.getString(resourceKeyBuilder.toString(), locale, mapModel);
		}
	}
	
	/**
	 * <p>A simplified version of the {@link #range(String, Renderer, Renderer)} function.
	 * <p>Returns a static renderer for {@link Range} elements in which every value is 
	 * rendered the same way - either with or without units.
	 * <p>See {@link #range(String, Renderer, Renderer)} for more information regarding the syntax, the context or 
	 * the prerequisites of {@link Range}-rendering functions.
	 * 
	 * @param rangeResourceKeyPrefix
	 *         the prefix of the translation key pointing to the range-based display text patterns
	 * @param renderer
	 *         a renderer used to display the value of a given {@link Range} bound
	 */
	public static <C extends Comparable<?>> Renderer<Range<C>> range(String rangeResourceKeyPrefix, Renderer<C> renderer) {
		return range(rangeResourceKeyPrefix, renderer, renderer);
	}
	
	/**
	 * <p>Returns a static renderer for {@link Range} type elements in which values may have units.
	 * <p>This function uses two renderers, one for the rendering of a plain value and 
	 * another for the rendering of a value with its unit.
	 * <p>Knowing whether to render the first or last element with or without its unit is left to the <i>resource</i>, 
	 * as it may differ from one interval to another and may need to be determined dynamically.
	 * <p><b>Note:</b> There are four different string variables recognised within a resource: <ul>
	 * <li><b>${start}:</b> substituted with the lower bound rendered without a unit
	 * <li><b>${startUnit}:</b> substituted with the lower bound rendered with its unit
	 * <li><b>${end}:</b> substituted with the upper bound rendered without a unit
	 * <li><b>${endUnit}:</b> substituted with the upper bound rendered with its unit
	 * </ul>
	 * <p><b>Note:</b> There are several resource key suffixes dynamically read: <ul>
	 * <li><b>&#60key&#62.both:</b> read if the range possesses a lower and an upper bound
	 * <li><b>&#60key&#62.from:</b> read if the range possesses a lower bound but no upper bound
	 * <li><b>&#60key&#62.upto:</b> read if the range possesses an upper bound but no lower bound
	 * <li><b>&#60key&#62.solo:</b> read if the range possesses a lower and an upper bound which have the same value
	 * </ul>
	 * <p><b>Example:</b> The resources:<ul>
	 * <li><b>base.key.upto=</b><i>Up to ${endUnit}</i> may be rendered as <i>Up to 1 mile</i> or <i>Up to 3 miles</i>.
	 * <li><b>base.key.both=</b><i>From ${start} to ${endUnit}</i> may be rendered as <i>From 10 to 15 people</i>.
	 * <li><b>base.key.from=</b><i>Starting at ${startUnit}</i> may be rendered as <i>Starting at $4</i>.
	 * <li><b>base.key.solo=</b><i>Exactly ${startUnit}</i> may be rendered as <i>Exactly 5 camels.</i>.
	 * <li><b>base.key.solo=</b><i>Closing at ${end}</i> may be rendered as <i>Closing at 5:00pm</i> using a specific Date Renderer.
	 * </ul>
	 * 
	 * @param rangeResourceKey
	 *         the prefix of the translation key pointing to the range-based display text patterns
	 * @param valueRenderer <br>
	 *         a renderer used to display the plain value of a given {@link Range} bound
	 * @param valueAndUnitRenderer 
	 *         a renderer used to display the value and the unit associated with a given {@link Range} bound
	 * @see {@link #count(String)}, {@link #count(String, Renderer)}
	 *         for rendering any countable value with its unit
	 * @see {@link #fromDatePattern(IDatePattern)}, {@link #fromDateFormat(DateFormat)}
	 *         for fully rendering dates as without-unit-values without bothering with custom units
	 * @throws MissingResourceException
	 *         if the resource key formed by associating the base key and one of the suffixes cannot be found
	 */
	public static <C extends Comparable<?>> Renderer<Range<C>> range(String rangeResourceKey, Renderer<C> withUnitValueRenderer, Renderer<C> withoutUnitValueRenderer) {
		return new RangeRenderer<C>(
				rangeResourceKey, 
				withoutUnitValueRenderer, 
				(withUnitValueRenderer == null) ? withoutUnitValueRenderer : withUnitValueRenderer
		)
				.nullsAsNull();
	}
	
	private static class RangeRenderer<C extends Comparable<?>> extends Renderer<Range<C>> {
		private static final long serialVersionUID = -3069600849637042624L;
		private final String resourceKeyPrefix;
		private final Renderer<C> withUnitValueRenderer;
		private final Renderer<C> withoutUnitValueRenderer;
		
		public RangeRenderer(String resourceKeyPrefix, Renderer<C> withoutUnitValueRenderer, Renderer<C> withUnitValueRenderer) {
			this.resourceKeyPrefix = resourceKeyPrefix;
			this.withoutUnitValueRenderer = withoutUnitValueRenderer;
			this.withUnitValueRenderer = withUnitValueRenderer;
		}
		
		@Override
		public String render(final Range<C> value, Locale locale) {
			IModel<Map<String, Object>> mapModel;
			StringBuilder resourceKeyBuilder = new StringBuilder(resourceKeyPrefix);
			if (!value.hasLowerBound() && !value.hasUpperBound()) {
				return null;
			} else if (!value.hasLowerBound()) {
				resourceKeyBuilder.append(".upto");
				mapModel = Models.dataMap()
						.put("end", this.withoutUnitValueRenderer.asModel(Models.transientModel(value.upperEndpoint())))
						.put("endUnit", this.withUnitValueRenderer.asModel(Models.transientModel(value.upperEndpoint())))
						.build();
			} else if (!value.hasUpperBound()) {
				resourceKeyBuilder.append(".from");
				mapModel = Models.dataMap()
						.put("start", this.withoutUnitValueRenderer.asModel(Models.transientModel(value.lowerEndpoint())))
						.put("startUnit", this.withUnitValueRenderer.asModel(Models.transientModel(value.lowerEndpoint())))
						.build();
			} else if (value.lowerEndpoint().equals(value.upperEndpoint())) {
				resourceKeyBuilder.append(".solo");
				mapModel = Models.dataMap()
						.put("start", this.withoutUnitValueRenderer.asModel(Models.transientModel(value.lowerEndpoint())))
						.put("startUnit", this.withUnitValueRenderer.asModel(Models.transientModel(value.lowerEndpoint())))
						.put("end", this.withoutUnitValueRenderer.asModel(Models.transientModel(value.upperEndpoint())))
						.put("endUnit", this.withUnitValueRenderer.asModel(Models.transientModel(value.upperEndpoint())))
						.build();
			} else {
				resourceKeyBuilder.append(".both");
				mapModel = Models.dataMap()
						.put("start", this.withoutUnitValueRenderer.asModel(Models.transientModel(value.lowerEndpoint())))
						.put("startUnit", this.withUnitValueRenderer.asModel(Models.transientModel(value.lowerEndpoint())))
						.put("end", this.withoutUnitValueRenderer.asModel(Models.transientModel(value.upperEndpoint())))
						.put("endUnit", this.withUnitValueRenderer.asModel(Models.transientModel(value.upperEndpoint())))
						.build();
			}
			return Renderer.getString(resourceKeyBuilder.toString(), locale, mapModel);
		}
	}
}
