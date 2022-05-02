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
	
	public static final String METER_LAST_CHECKSUM_DURATION = "storage.lastChecksumDuration";
	public static final String METER_LAST_CHECK_DURATION = "storage.lastCheckDuration";
	public static final String METER_LAST_CHECKSUM_AGE = "storage.lastChecksumAge";
	public static final String METER_LAST_CHECK_AGE = "storage.lastCheckAge";
	public static final String METER_ORPHANS = "storage.orphans";
	public static final String METER_FAILURES = "storage.failures";
	public static final String METER_SIZE = "storage.size";
	public static final String METER_FILE_COUNT = "storage.fileCounts";

	public static final String TAG_FAILURE_STATUS = "failureStatus";
	public static final String TAG_FICHIER_STATUS = "fichierStatus";
	public static final String TAG_FICHIER_TYPE = "fichierType";
	public static final String TAG_STORAGE_UNIT_ID = "storageUnitId";
	public static final String TAG_STORAGE_UNIT_TYPE = "storageUnitType";

	private static final String UNIT_FICHIER = "Fichier";
	private static final String UNIT_SECOND = "Second";
	private static final String UNIT_HOUR = "Hour";
	private static final String UNIT_FILE = "File";

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
		fileCount = MultiGauge.builder(METER_FILE_COUNT)
				.description("Number of Fichier")
				.baseUnit(UNIT_FICHIER)
				.register(registry);
		size = MultiGauge.builder(METER_SIZE)
				.description("Storage size")
				.baseUnit(UNIT_FICHIER)
				.register(registry);
		failure = MultiGauge.builder(METER_FAILURES)
				.description("Number of Fichier")
				.baseUnit(UNIT_FICHIER)
				.register(registry);
		orphan = MultiGauge.builder(METER_ORPHANS)
				.description("Number of orphans")
				.baseUnit(UNIT_FILE)
				.register(registry);
		lastAge = MultiGauge.builder(METER_LAST_CHECK_AGE)
				.description("Last check age")
				.baseUnit(UNIT_HOUR)
				.register(registry);
		lastChecksumAge = MultiGauge.builder(METER_LAST_CHECKSUM_AGE)
				.description("Last checksum age")
				.baseUnit(UNIT_HOUR)
				.register(registry);
		lastDuration = MultiGauge.builder(METER_LAST_CHECK_DURATION)
				.description("Last check duration")
				.baseUnit(UNIT_SECOND)
				.register(registry);
		lastChecksumDuration = MultiGauge.builder(METER_LAST_CHECKSUM_DURATION)
				.description("Last checksum duration")
				.baseUnit(UNIT_SECOND)
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
				TAG_STORAGE_UNIT_ID, s.getStorageUnitId().toString(),
				TAG_STORAGE_UNIT_TYPE, s.getStorageUnitType().getName(),
				TAG_FICHIER_TYPE, s.getFichierType().getName(),
				TAG_FICHIER_STATUS, s.getFichierStatus().name());
		return Row.of(tags, getter.apply(s));
	}

	private Row<Number> orphanRow(StorageOrphanStatistic s) {
		Tags tags = Tags.of(
				TAG_STORAGE_UNIT_ID, s.getStorageUnitId().toString(),
				TAG_STORAGE_UNIT_TYPE, s.getStorageUnitType().getName(),
				TAG_FAILURE_STATUS, s.getFailureStatus().name());
		return Row.of(tags, s.getCount());
	}

	private Row<Number> failureRow(StorageFailureStatistic s) {
		Tags tags = Tags.of(
				TAG_STORAGE_UNIT_ID, s.getStorageUnitId().toString(),
				TAG_STORAGE_UNIT_TYPE, s.getStorageUnitType().getName(),
				TAG_FICHIER_TYPE, s.getFichierType().getName(),
				TAG_FICHIER_STATUS, s.getFichierStatus().name(),
				"failureType", s.getFailureType().name(),
				TAG_FAILURE_STATUS, s.getFailureStatus().name());
		return Row.of(tags, s.getCount());
	}

	private Row<Number> checkRow(StorageCheckStatistic s, Function<StorageCheckStatistic, Number> getter) {
		Tags tags = Tags.of(
				TAG_STORAGE_UNIT_ID, s.getStorageUnitId().toString(),
				TAG_STORAGE_UNIT_TYPE, s.getStorageUnitType().getName());
		return Row.of(tags, getter.apply(s));
	}

}
