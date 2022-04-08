package org.igloo.storage.micrometer;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.igloo.storage.api.IStorageStatisticsService;
import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.MultiGauge.Row;
import io.micrometer.core.instrument.Tags;

public class MicrometerConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MicrometerConfig.class);
	
	private final IStorageStatisticsService storageStatisticsService;
	
	private AtomicReference<List<StorageStatistic>> statistics = new AtomicReference<>();
	private AtomicReference<List<StorageOrphanStatistic>> orphanStatistics = new AtomicReference<>();
	private AtomicReference<List<StorageFailureStatistic>> failureStatistics = new AtomicReference<>();
	private AtomicReference<List<StorageCheckStatistic>> checkStatistics = new AtomicReference<>();

	private MultiGauge fileCount;
	private MultiGauge size;
	private MultiGauge failure;
	private MultiGauge orphan;
	private MultiGauge lastAge;
	private MultiGauge lastChecksumAge;
	private MultiGauge lastDuration;
	private MultiGauge lastChecksumDuration;
	
	public MicrometerConfig(IStorageStatisticsService storageStatisticsService, MeterRegistry registry) {
		this.storageStatisticsService = storageStatisticsService;
		fileCount = MultiGauge.builder("storage.fileCounts")
				.description("Number of Fichier")
				.baseUnit("Fichier")
				.register(registry);
		size = MultiGauge.builder("storage.size")
				.description("Storage size")
				.baseUnit("Fichier")
				.register(registry);
		failure = MultiGauge.builder("storage.failures")
				.description("Number of Fichier")
				.baseUnit("Fichier")
				.register(registry);
		orphan = MultiGauge.builder("storage.orphans")
				.description("Number of orphans")
				.baseUnit("Files")
				.register(registry);
		lastAge = MultiGauge.builder("storage.lastAge")
				.description("Last check age")
				.baseUnit("Age in hours")
				.register(registry);
		lastChecksumAge = MultiGauge.builder("storage.lastChecksumAge")
				.description("Last checksum age")
				.baseUnit("Age in hours")
				.register(registry);
		lastDuration = MultiGauge.builder("storage.lastDuration")
				.description("Last check duration")
				.baseUnit("Duration in seconds")
				.register(registry);
		lastChecksumDuration = MultiGauge.builder("storage.lastChecksumDuration")
				.description("Last checksum duration")
				.baseUnit("Duration in seconds")
				.register(registry);
	}

	public void start() {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "StorageStatistics");
			t.setDaemon(true);
			return t;
		});
		executor.scheduleWithFixedDelay(this::refreshStatistics, 0, 120, TimeUnit.SECONDS);
	}

	public void refresh() {
		this.refreshStatistics();
	}

	private void refreshStatistics() {
		try {
			statistics.set(storageStatisticsService.getStorageStatistics());
			fileCount.register(statisticRows(StorageStatistic::getCount), true);
			size.register(statisticRows(StorageStatistic::getSize), true);
			
			orphanStatistics.set(storageStatisticsService.getStorageOrphanStatistics());
			orphan.register(orphanRows(), true);
			
			checkStatistics.set(storageStatisticsService.getStorageCheckStatistics());
			lastAge.register(checkRows(s -> durationToAge(s, StorageCheckStatistic::getLastAge, Duration.ofDays(1))), true);
			lastChecksumAge.register(checkRows(s -> durationToAge(s, StorageCheckStatistic::getLastChecksumAge, Duration.ofDays(1))), true);
			lastDuration.register(checkRows(s -> durationToAge(s, StorageCheckStatistic::getLastDuration, Duration.ofSeconds(1))), true);
			lastChecksumDuration.register(checkRows(s -> durationToAge(s, StorageCheckStatistic::getLastChecksumDuration, Duration.ofSeconds(1))), true);
			
			failureStatistics.set(storageStatisticsService.getStorageFailureStatistics());
			failure.register(failureRows(), true);
		} catch (RuntimeException e) {
			LOGGER.error("Error collecting storage statistics", e);
		}
	}

	private <T> Long durationToAge(T o, Function<T, Duration> objectToDuration, Duration durationUnit) {
		return Optional.ofNullable(objectToDuration.apply(o)).map(d -> d.dividedBy(durationUnit)).orElse(null);
	}

	private Iterable<Row<?>> statisticRows(Function<StorageStatistic, Number> getter) {
		return statistics.get().stream().<Row<Number>>map(s -> this.fileCountRow(s, getter))
				.collect(Collectors.toList());
	}

	private Iterable<Row<?>> orphanRows() {
		return orphanStatistics.get().stream().<Row<Number>>map(this::orphanRow)
				.collect(Collectors.toList());
	}

	private Iterable<Row<?>> checkRows(Function<StorageCheckStatistic, Number> getter) {
		// filter -> age can be null, for this case we do not register a value (else null value is transformed to 0 by micrometer)
		return checkStatistics.get().stream().filter(s -> getter.apply(s) != null).<Row<Number>>map(s -> this.checkRow(s, getter))
				.collect(Collectors.toList());
	}

	private Iterable<Row<?>> failureRows() {
		return failureStatistics.get().stream().<Row<Number>>map(this::failureRow)
				.collect(Collectors.toList());
	}

	private Row<Number> fileCountRow(StorageStatistic s, Function<StorageStatistic, Number> getter) {
		Tags tags = Tags.of(
				"storageUnitId", s.getStorageUnitId().toString(),
				"storageUnitType", s.getStorageUnitType().getName(),
				"fichierType", s.getFichierType().getName(),
				"fichierStatus", s.getFichierStatus().name());
		return Row.of(tags, getter.apply(s));
	}

	private Row<Number> orphanRow(StorageOrphanStatistic s) {
		Tags tags = Tags.of(
				"storageUnitId", s.getStorageUnitId().toString(),
				"storageUnitType", s.getStorageUnitType().getName(),
				"failureStatus", s.getFailureStatus().name());
		return Row.of(tags, s.getCount());
	}

	private Row<Number> failureRow(StorageFailureStatistic s) {
		Tags tags = Tags.of(
				"storageUnitId", s.getStorageUnitId().toString(),
				"storageUnitType", s.getStorageUnitType().getName(),
				"fichierType", s.getFichierType().getName(),
				"fichierStatus", s.getFichierStatus().name(),
				"failureType", s.getFailureType().name(),
				"failureStatus", s.getFailureStatus().name());
		return Row.of(tags, s.getCount());
	}

	private Row<Number> checkRow(StorageCheckStatistic s, Function<StorageCheckStatistic, Number> getter) {
		Tags tags = Tags.of(
				"storageUnitId", s.getStorageUnitId().toString(),
				"storageUnitType", s.getStorageUnitType().getName());
		return Row.of(tags, getter.apply(s));
	}

}
