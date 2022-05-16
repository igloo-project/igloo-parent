package org.igloo.monitoring.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MoreCollectors;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.search.MeterNotFoundException;

public class SimpleMicrometerHealthMetricService implements IHealthMetricService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMicrometerHealthMetricService.class);

	private final MeterRegistry registry;
	private final String metricName;
	private final String metricCheck;
	private final String metricCheckTagName;
	private final Predicate<Gauge> metricFilter;
	private final Collector<Gauge, ?, ? extends Number> reducer;
	private final Function<Number, HealthStatus> statusFunction;
	private final Function<Number, String> messageFunction;
	private final Function<Gauge, String> perfDataClassifier;

	/**
	 * <p>With this constructor, tag-filtered metric must be unique.</p>
	 * 
	 * @see SimpleMicrometerHealthMetricService#SimpleMicrometerHealthService(MeterRegistry, String, Predicate, Collector, Function, Function, Function)
	 * 
	 * @param registry
	 * @param metricName
	 * @param filters
	 * @param statusFunction
	 * @param messageFunction
	 */
	public SimpleMicrometerHealthMetricService(MeterRegistry registry, String metricName, Tags filters, Function<Number, HealthStatus> statusFunction, Function<Number, String> messageFunction) {
		this(registry, metricName, filters, null, statusFunction, messageFunction);
	}

	/**
	 * <p>Metric filtering is tag-based.</p>
	 * 
	 * @see SimpleMicrometerHealthMetricService#SimpleMicrometerHealthService(MeterRegistry, String, Predicate, Collector, Function, Function, Function)
	 */
	public SimpleMicrometerHealthMetricService(MeterRegistry registry, String metricName, Tags filters, Collector<Gauge, ?, ? extends Number> reducer, Function<Number, HealthStatus> statusFunction, Function<Number, String> messageFunction) {
		this(registry, metricName, null, null, tagFilter(filters), reducer, statusFunction, messageFunction);
	}

	/**
	 * @see SimpleMicrometerHealthMetricService#SimpleMicrometerHealthService(MeterRegistry, String, Predicate, Collector, Function, Function, Function)
	 */
	public SimpleMicrometerHealthMetricService(MeterRegistry registry, String metricName, String metricCheck, String metricCheckTagName, Predicate<Gauge> metricFilter, Collector<Gauge, ?, ? extends Number> reducer, Function<Number, HealthStatus> statusFunction, Function<Number, String> messageFunction) {
		this(registry, metricName, metricCheck, metricCheckTagName, metricFilter, reducer, statusFunction, messageFunction, null);
	}

	/**
	 * <p>This service can provides a {@link HealthLookup} object from a metric coordinate (name and tags), a reducer
	 * (how to collect multiple metrics to provide one number) and a status function (how to map a number to a status).</p>
	 * 
	 * <p>Metric filtering is done by a predicate.</p>
	 * 
	 * @param registry a micrometer registry to lookup metrics from.
	 * @param metricName a metric name for micrometer lookup.
	 * @param metricCheck a metric that allows to check if metricName is meant to be available. May be null.
	 * @param metricCheckTagName a tag name to filter metricCheck. May be null if metricCheck is null.
	 * @param metricFilter a filter to select appropriate metrics with the same metric name.
	 * @param reducer a function to build a unique number result from all matching metrics. If null, metric uniqueness is enforced.
	 * @param statusFunction a function to build a status from the metric calculation status.
	 * @param messageFunction may be null; if null a basic message is inserted in result.
	 * @param perfDataClassifier if perfData is needed, this classifier is used to split all matching metrics; each
	 *        classifier is used for a perfData line. If null, only one perfData line is generated.
	 */
	public SimpleMicrometerHealthMetricService(MeterRegistry registry, String metricName, String metricCheck, String metricCheckTagName, Predicate<Gauge> metricFilter, Collector<Gauge, ?, ? extends Number> reducer, Function<Number, HealthStatus> statusFunction, Function<Number, String> messageFunction, Function<Gauge, String> perfDataClassifier) {
		if (metricCheck != null) {
			Objects.requireNonNull(metricCheckTagName, "metricCheckTagName is required if metricCheck is not null.");
		}
		this.registry = registry;
		this.metricName = metricName;
		this.metricCheck = metricCheck;
		this.metricCheckTagName = metricCheckTagName;
		this.metricFilter = metricFilter;
		this.reducer = reducer;
		this.statusFunction = statusFunction;
		this.messageFunction = messageFunction;
		this.perfDataClassifier = perfDataClassifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HealthLookup getHealthLookup() {
		try {
			return doGetHealthLookup();
		} catch (LookupResultException e) {
			return e.lookup;
		}
	}

	private HealthLookup doGetHealthLookup() throws LookupResultException {
		try {
			checkAvailability();
			// metric may be missing (example: 0 failure)
			String baseUnit = lookupBaseUnit();
			Collection<Gauge> gauges = registry.find(metricName).gauges();
			Number value;
			value = computeValue(gauges);
			HealthStatus status = statusFunction.apply(value);
			String message = computeMessage(value);
			List<PerfData> perfDatas = computePerfDatas(baseUnit, gauges, value);
			return new HealthLookup(metricName, status, message, perfDatas);
		} catch (RuntimeException e) {
			LOGGER.error("Unable to build status from micrometer metric {}", metricName, e);
			throw unknownStatus(metricName, e.getMessage());
		}
	}

	private List<PerfData> computePerfDatas(String baseUnit, Collection<Gauge> gauges, Number value) {
		List<PerfData> perfDatas;
		if (perfDataClassifier != null) {
			perfDatas = new ArrayList<>();
			gauges.stream()
					.filter(metricFilter)
					.collect(Collectors.groupingBy(perfDataClassifier, reducer))
					.forEach((k, v) -> {
						perfDatas.add(new PerfData(k, baseUnit, v, null, null, null, null));
					});
		} else {
			perfDatas = List.of(new PerfData(metricName, baseUnit, value, null, null, null, null));
		}
		return perfDatas;
	}

	private String computeMessage(Number value) {
		String message;
		if (messageFunction == null) {
			message = String.format("Metric value is %s", value != null ? value.toString() : "null");
		} else {
			message = messageFunction.apply(value);
		}
		return message;
	}

	private Number computeValue(Collection<Gauge> gauges) throws LookupResultException {
		Number value;
		if (reducer != null) {
			value = gauges.stream().filter(metricFilter).collect(reducer);
		} else {
			try {
				value = gauges.stream().filter(metricFilter).collect(MoreCollectors.onlyElement()).value();
			} catch (NoSuchElementException e) {
				value = null;
			} catch (IllegalArgumentException e) {
				throw unknownStatus(metricName, "More than one metric and no reducer provided");
			}
		}
		return value;
	}

	private void checkAvailability() throws LookupResultException {
		if (metricCheck != null) {
			Gauge gauge = registry.find(metricCheck).tag(metricCheckTagName, metricName).gauge();
			if (gauge == null) {
				throw unknownStatus(metricName, String.format("No computation found for metric %s", metricName));
			}
		}
	}

	private String lookupBaseUnit() {
		String baseUnit;
		try {
			baseUnit = Optional.ofNullable(registry.get(metricName)).map(m -> m.gauge().getId().getBaseUnit()).orElse("");
		} catch (MeterNotFoundException e) {
			baseUnit = "";
		}
		return baseUnit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsPerfData() {
		return false;
	}

	/**
	 * Generate an unknown status.
	 * 
	 * @param message message to push with unknown status.
	 * @throws LookupResultException 
	 */
	private static LookupResultException unknownStatus(String metricName, String message) throws LookupResultException {
		throw new LookupResultException(new HealthLookup(metricName, HealthStatus.UNKNOWN, message, null));
	}

	/**
	 * Build a predicate that match metric tags against {@code filters}
	 * 
	 * @param filters Set of tags that metric must contains with the same values.
	 * @return a predicate filtering metrics by tags
	 */
	public static Predicate<Gauge> tagFilter(Tags filters) {
		return g -> filters.stream().allMatch(t -> Objects.equals(t.getValue(), g.getId().getTag(t.getKey())));
	}

	/**
	 * Local exception to allow to use a throw pattern in getHealthLookup method
	 */
	private static class LookupResultException extends Exception {
		private static final long serialVersionUID = -5174295046100386940L;
		private final HealthLookup lookup;
		public LookupResultException(HealthLookup lookup) {
			this.lookup = lookup;
		}
	}

}
