package org.igloo.storage.micrometer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.Assertions;
import org.igloo.storage.api.IStorageStatisticsService;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class TestMicrometer {

	@Test
	void testMicrometer() {
		IStorageStatisticsService statistics = mock(IStorageStatisticsService.class);
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		when(statistics.getStorageStatistics()).thenReturn(List.of(
				storageStatistic(1l, StorageUnitType.TYPE1, FichierType.FTYPE1, FichierStatus.ALIVE, 13, 10_000_000l),
				storageStatistic(1l, StorageUnitType.TYPE1, FichierType.FTYPE2, FichierStatus.ALIVE, 7, 3_000_000l),
				storageStatistic(1l, StorageUnitType.TYPE1, FichierType.FTYPE3, FichierStatus.TRANSIENT, 2, 100_000l),
				storageStatistic(1l, StorageUnitType.TYPE2, FichierType.FTYPE1, FichierStatus.ALIVE, 9, 1_000_000l),
				storageStatistic(1l, StorageUnitType.TYPE3, FichierType.FTYPE2, FichierStatus.ALIVE, 15, 23_000_000l)
		));
		when(statistics.getStorageCheckStatistics()).thenReturn(List.of());
		when(statistics.getStorageFailureStatistics()).thenReturn(List.of());
		when(statistics.getStorageOrphanStatistics()).thenReturn(List.of());
		new MicrometerConfig(statistics, registry).refresh();
		verify(statistics, times(1)).getStorageStatistics();
		assertThat(registry, "storage.fileCounts", Tags.of("fichierType", FichierType.FTYPE1.name()), (a, b) -> a + b).isEqualTo(22);
	}

	<T extends Meter> AbstractLongAssert<?> assertThat(MeterRegistry registry, String name, Tags tags, BinaryOperator<Long> aggregate) {
		return assertThat(registry, name, i -> tags.stream().allMatch(t -> i.getTag(t.getKey()).equals(t.getValue())), aggregate);
	}

	<T extends Meter> AbstractLongAssert<?> assertThat(MeterRegistry registry, String name, Predicate<Meter.Id> predicate, BinaryOperator<Long> aggregate) {
		return Assertions.assertThat(registry.find(name).gauges().stream().filter(g -> predicate.test(g.getId())).map(Gauge::value).map(Math::round).reduce(aggregate).get());	
	}

	private StorageStatistic storageStatistic(long storateUnitId, StorageUnitType storageUnitType, FichierType fichierType, FichierStatus fichierStatus, int count, long size) {
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
