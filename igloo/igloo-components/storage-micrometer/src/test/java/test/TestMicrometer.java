package test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.logging.LogManager;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.Assertions;
import org.igloo.storage.api.IStorageStatisticsService;
import org.igloo.storage.micrometer.MicrometerConfig;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

class TestMicrometer {
  static {
    // jboss-logging -> slf4j
    System.setProperty("org.jboss.logging.provider", "slf4j");
    // enable JUL logging
    try {
      ByteArrayInputStream is = new ByteArrayInputStream(".level=ALL".getBytes());
      LogManager.getLogManager().readConfiguration(is);
    } catch (SecurityException | IOException e) {
      throw new IllegalStateException(e);
    }
    SLF4JBridgeHandler.install();
  }

  protected static final Logger LOGGER = LoggerFactory.getLogger(TestMicrometer.class);

  /**
   * Test micrometer extraction, filtering by tags. Test statistics (mocks) are not consistent
   * (check vs failures vs ...)
   */
  @Test
  void testMicrometer() {
    IStorageStatisticsService statistics = mock(IStorageStatisticsService.class);
    SimpleMeterRegistry registry = new SimpleMeterRegistry();
    when(statistics.getStorageStatistics())
        .thenReturn(
            List.of(
                storageStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE1,
                    FichierStatus.ALIVE,
                    13,
                    10_000_000l),
                storageStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE2,
                    FichierStatus.ALIVE,
                    7,
                    3_000_000l),
                storageStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE3,
                    FichierStatus.TRANSIENT,
                    2,
                    100_000l),
                storageStatistic(
                    2l,
                    StorageUnitType.TYPE2,
                    FichierType.FTYPE1,
                    FichierStatus.ALIVE,
                    9,
                    1_000_000l),
                storageStatistic(
                    3l,
                    StorageUnitType.TYPE3,
                    FichierType.FTYPE2,
                    FichierStatus.ALIVE,
                    15,
                    23_000_000l)));
    when(statistics.getStorageCheckStatistics())
        .thenReturn(
            List.of(
                storageCheckStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    Duration.ofDays(1),
                    Duration.ofDays(2),
                    Duration.ofMinutes(15),
                    Duration.ofMinutes(75)),
                storageCheckStatistic(
                    2l,
                    StorageUnitType.TYPE2,
                    Duration.ofDays(5),
                    Duration.ofDays(5),
                    Duration.ofMinutes(20),
                    Duration.ofMinutes(55)),
                storageCheckStatistic(
                    3l,
                    StorageUnitType.TYPE3,
                    Duration.ofDays(3),
                    null,
                    Duration.ofMinutes(6),
                    null)));
    when(statistics.getStorageFailureStatistics())
        .thenReturn(
            List.of(
                storageFailureStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE1,
                    FichierStatus.ALIVE,
                    StorageFailureStatus.ALIVE,
                    StorageFailureType.MISSING_FILE,
                    10,
                    10_000l),
                storageFailureStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE2,
                    FichierStatus.ALIVE,
                    StorageFailureStatus.ALIVE,
                    StorageFailureType.MISSING_FILE,
                    5,
                    1_000l),
                storageFailureStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE2,
                    FichierStatus.TRANSIENT,
                    StorageFailureStatus.ALIVE,
                    StorageFailureType.CHECKSUM_MISMATCH,
                    2,
                    1_000l),
                storageFailureStatistic(
                    1l,
                    StorageUnitType.TYPE1,
                    FichierType.FTYPE2,
                    FichierStatus.ALIVE,
                    StorageFailureStatus.ACKNOWLEDGED,
                    StorageFailureType.CHECKSUM_MISMATCH,
                    1,
                    1_000l),
                storageFailureStatistic(
                    2l,
                    StorageUnitType.TYPE2,
                    FichierType.FTYPE3,
                    FichierStatus.ALIVE,
                    StorageFailureStatus.ALIVE,
                    StorageFailureType.SIZE_MISMATCH,
                    1,
                    1_000l)));
    when(statistics.getStorageOrphanStatistics())
        .thenReturn(
            List.of(
                storageOrphanStatistic(1l, StorageUnitType.TYPE1, StorageFailureStatus.ALIVE, 10),
                storageOrphanStatistic(1l, StorageUnitType.TYPE1, StorageFailureStatus.FIXED, 5),
                storageOrphanStatistic(2l, StorageUnitType.TYPE2, StorageFailureStatus.ALIVE, 3),
                storageOrphanStatistic(3l, StorageUnitType.TYPE3, StorageFailureStatus.ALIVE, 1)));
    new MicrometerConfig(statistics, registry).refresh();
    verify(statistics, times(1)).getStorageStatistics();
    verify(statistics, times(1)).getStorageCheckStatistics();
    verify(statistics, times(1)).getStorageOrphanStatistics();
    verify(statistics, times(1)).getStorageFailureStatistics();
    assertThat(
            registry,
            "storage.fileCounts",
            Tags.of("fichierType", FichierType.FTYPE1.name()),
            (a, b) -> a + b)
        .isEqualTo(22);
    assertThat(
            registry,
            "storage.fileCounts",
            Tags.of("fichierType", FichierType.FTYPE2.name()),
            (a, b) -> a + b)
        .isEqualTo(22);
    assertThat(
            registry,
            "storage.fileCounts",
            Tags.of("fichierType", FichierType.FTYPE3.name()),
            (a, b) -> a + b)
        .isEqualTo(2);
    assertThat(
            registry,
            "storage.fileCounts",
            Tags.of(
                "fichierType",
                FichierType.FTYPE3.name(),
                "fichierStatus",
                FichierStatus.ALIVE.name()),
            (a, b) -> a + b)
        .isNull();
    assertThat(
            registry,
            "storage.fileCounts",
            Tags.of("fichierType", FichierType.FTYPE2.name(), "storageUnitId", "1"),
            (a, b) -> a + b)
        .isEqualTo(7);
    assertThat(registry, "storage.lastCheckAge", Tags.of("storageUnitId", "1"), (a, b) -> a + b)
        .isEqualTo(1);
    assertThat(registry, "storage.lastCheckAge", Tags.of("storageUnitId", "2"), (a, b) -> a + b)
        .isEqualTo(5);
    assertThat(registry, "storage.lastCheckAge", Tags.of("storageUnitId", "3"), (a, b) -> a + b)
        .isEqualTo(3);
    assertThat(registry, "storage.lastChecksumAge", Tags.of("storageUnitId", "1"), (a, b) -> a + b)
        .isEqualTo(2);
    assertThat(registry, "storage.lastChecksumAge", Tags.of("storageUnitId", "2"), (a, b) -> a + b)
        .isEqualTo(5);
    assertThat(registry, "storage.lastChecksumAge", Tags.of("storageUnitId", "3"), (a, b) -> a + b)
        .isNull();
    assertThat(
            registry, "storage.lastCheckDuration", Tags.of("storageUnitId", "1"), (a, b) -> a + b)
        .isEqualTo(15 * 60);
    assertThat(
            registry, "storage.lastCheckDuration", Tags.of("storageUnitId", "2"), (a, b) -> a + b)
        .isEqualTo(20 * 60);
    assertThat(
            registry, "storage.lastCheckDuration", Tags.of("storageUnitId", "3"), (a, b) -> a + b)
        .isEqualTo(6 * 60);
    assertThat(
            registry,
            "storage.lastChecksumDuration",
            Tags.of("storageUnitId", "1"),
            (a, b) -> a + b)
        .isEqualTo(75 * 60);
    assertThat(
            registry,
            "storage.lastChecksumDuration",
            Tags.of("storageUnitId", "2"),
            (a, b) -> a + b)
        .isEqualTo(55 * 60);
    assertThat(
            registry,
            "storage.lastChecksumDuration",
            Tags.of("storageUnitId", "3"),
            (a, b) -> a + b)
        .isNull();
    assertThat(
            registry,
            "storage.failures",
            Tags.of(
                "failureStatus",
                StorageFailureStatus.ALIVE.name(),
                "fichierStatus",
                FichierStatus.ALIVE.name()),
            (a, b) -> a + b)
        .isEqualTo(16);
    assertThat(
            registry,
            "storage.failures",
            Tags.of(
                "failureType",
                StorageFailureType.MISSING_FILE.name(),
                "failureStatus",
                StorageFailureStatus.ALIVE.name(),
                "fichierStatus",
                FichierStatus.ALIVE.name()),
            (a, b) -> a + b)
        .isEqualTo(15);
    assertThat(
            registry,
            "storage.failures",
            Tags.of(
                "failureType",
                StorageFailureType.SIZE_MISMATCH.name(),
                "failureStatus",
                StorageFailureStatus.ALIVE.name(),
                "fichierStatus",
                FichierStatus.ALIVE.name()),
            (a, b) -> a + b)
        .isEqualTo(1);
    assertThat(
            registry,
            "storage.failures",
            Tags.of(
                "failureType",
                StorageFailureType.CHECKSUM_MISMATCH.name(),
                "failureStatus",
                StorageFailureStatus.ALIVE.name(),
                "fichierStatus",
                FichierStatus.ALIVE.name()),
            (a, b) -> a + b)
        .isNull();
    assertThat(
            registry,
            "storage.orphans",
            Tags.of("failureStatus", StorageFailureStatus.ALIVE.name()),
            (a, b) -> a + b)
        .isEqualTo(14);
    assertThat(registry, "storage.orphans", Tags.of("storageUnitId", "2"), (a, b) -> a + b)
        .isEqualTo(3);
  }

  private StorageOrphanStatistic storageOrphanStatistic(
      long storageUnitId,
      StorageUnitType storageUnitType,
      StorageFailureStatus storageFailureStatus,
      int count) {
    StorageOrphanStatistic o = new StorageOrphanStatistic();
    o.setStorageUnitId(storageUnitId);
    o.setStorageUnitType(storageUnitType);
    o.setFailureStatus(storageFailureStatus);
    o.setCount(count);
    return o;
  }

  private StorageFailureStatistic storageFailureStatistic(
      long storageUnitId,
      StorageUnitType storageUnitType,
      FichierType fichierType,
      FichierStatus fichierStatus,
      StorageFailureStatus storageFailureStatus,
      StorageFailureType storageFailureType,
      int count,
      long size) {
    StorageFailureStatistic f = new StorageFailureStatistic();
    f.setStorageUnitId(storageUnitId);
    f.setStorageUnitType(storageUnitType);
    f.setFichierType(fichierType);
    f.setFichierStatus(fichierStatus);
    f.setFailureStatus(storageFailureStatus);
    f.setFailureType(storageFailureType);
    f.setCount(count);
    f.setSize(size);
    return f;
  }

  private StorageCheckStatistic storageCheckStatistic(
      long storageUnitId,
      StorageUnitType storageUnitType,
      Duration lastAge,
      Duration lastChecksumAge,
      Duration lastDuration,
      Duration lastChecksumDuration) {
    StorageCheckStatistic c = new StorageCheckStatistic();
    c.setStorageUnitId(storageUnitId);
    c.setStorageUnitType(storageUnitType);
    c.setLastAge(lastAge);
    c.setLastChecksumAge(lastChecksumAge);
    if (lastAge != null) {
      c.setLastOn(LocalDateTime.now().minus(lastAge));
    }
    if (lastChecksumAge != null) {
      c.setLastChecksumOn(LocalDateTime.now().minus(lastChecksumAge));
    }
    c.setLastDuration(lastDuration);
    c.setLastChecksumDuration(lastChecksumDuration);
    return c;
  }

  <T extends Meter> AbstractLongAssert<?> assertThat(
      MeterRegistry registry, String name, Tags tags, BinaryOperator<Long> aggregate) {
    return assertThat(
        registry,
        name,
        meterId ->
            tags.stream().allMatch(tag -> meterId.getTag(tag.getKey()).equals(tag.getValue())),
        aggregate);
  }

  <T extends Meter> AbstractLongAssert<?> assertThat(
      MeterRegistry registry,
      String name,
      Predicate<Meter.Id> predicate,
      BinaryOperator<Long> aggregate) {
    return Assertions.assertThat(
        registry.find(name).gauges().stream()
            .filter(g -> predicate.test(g.getId()))
            .map(Gauge::value)
            .map(Math::round)
            .reduce(aggregate)
            .orElse(null));
  }

  private StorageStatistic storageStatistic(
      long storateUnitId,
      StorageUnitType storageUnitType,
      FichierType fichierType,
      FichierStatus fichierStatus,
      int count,
      long size) {
    StorageStatistic s = new StorageStatistic();
    s.setStorageUnitId(storateUnitId);
    s.setStorageUnitType(storageUnitType);
    s.setFichierType(fichierType);
    s.setFichierStatus(fichierStatus);
    s.setCount(count);
    s.setSize(size);
    return s;
  }
}
