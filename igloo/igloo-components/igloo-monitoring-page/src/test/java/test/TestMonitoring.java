package test;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.MultiGauge.Row;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.igloo.monitoring.service.HealthLookup;
import org.igloo.monitoring.service.HealthStatus;
import org.igloo.monitoring.service.SimpleMicrometerHealthMetricService;
import org.junit.jupiter.api.Test;

class TestMonitoring {

  @Test
  void testSimpleMicrometerHealthService() {
    SimpleMeterRegistry registry = new SimpleMeterRegistry();
    MultiGauge gauge = MultiGauge.builder("metric.name").baseUnit("item").register(registry);
    gauge.register(
        List.of(
            Row.of(Tags.of("tag1", "value1", "tag2", "value1"), 1),
            Row.of(Tags.of("tag1", "value2", "tag2", "value1"), 2),
            Row.of(Tags.of("tag1", "value3", "tag2", "value1"), 3),
            Row.of(Tags.of("tag1", "value1", "tag2", "value2"), 4)),
        true);
    Predicate<Gauge> predicate = g -> g.getId().getTag("tag2").equals("value1");
    SimpleMicrometerHealthMetricService service =
        new SimpleMicrometerHealthMetricService(
            registry,
            "metric.name",
            null,
            null,
            predicate, // filter
            Collectors.<Gauge>summingLong(
                g -> Double.valueOf(g.value()).longValue()), // sum matching
            g -> g.longValue() < 3 ? HealthStatus.OK : HealthStatus.CRITICAL,
            g -> g.longValue() < 2 ? "All Right" : "Red Alert",
            null);
    {
      HealthLookup lookup = service.getHealthLookup();
      assertThat(lookup.getStatus()).isEqualTo(HealthStatus.CRITICAL);
      // no groupby -> one value
      assertThat(lookup.getPerfDatas()).hasSize(1);
      assertThat(lookup.getPerfDatas().get(0).getValue()).isEqualTo(1l + 2l + 3l);
      assertThat(lookup.getPerfDatas().get(0).getUnit()).isEqualTo("item");
      assertThat(lookup.getMessage()).isEqualTo("Red Alert");
    }
    gauge.register(
        List.of(
            Row.of(Tags.of("tag1", "value1", "tag2", "value1"), 0),
            Row.of(Tags.of("tag1", "value2", "tag2", "value1"), 1),
            Row.of(Tags.of("tag1", "value3", "tag2", "value1"), 1),
            Row.of(Tags.of("tag1", "value1", "tag2", "value2"), 4)),
        true);
    {
      HealthLookup lookup = service.getHealthLookup();
      assertThat(lookup.getStatus()).isEqualTo(HealthStatus.OK);
      // no groupby -> one value
      assertThat(lookup.getPerfDatas()).hasSize(1);
      assertThat(lookup.getPerfDatas().get(0).getValue()).isEqualTo(0l + 1l + 1l);
      assertThat(lookup.getPerfDatas().get(0).getUnit()).isEqualTo("item");
      assertThat(lookup.getMessage()).isEqualTo("Red Alert");
    }
  }

  @Test
  void testSimpleMicrometerHealthServiceGroupBy() {
    SimpleMeterRegistry registry = new SimpleMeterRegistry();
    MultiGauge gauge = MultiGauge.builder("metric.name").baseUnit("item").register(registry);
    SimpleMicrometerHealthMetricService service2 =
        new SimpleMicrometerHealthMetricService(
            registry,
            "metric.name",
            null,
            null,
            g -> g.getId().getTag("tag2").equals("value1"), // filter
            Collectors.<Gauge>summingLong(
                g -> Double.valueOf(g.value()).longValue()), // sum matching
            g -> g.longValue() < 6 ? HealthStatus.OK : HealthStatus.CRITICAL,
            g -> g.longValue() < 5 ? "All Right" : "Red alert",
            g -> g.getId().getTag("tag1")); // group by tag1
    gauge.register(
        List.of(
            Row.of(Tags.of("tag1", "value1", "tag2", "value1"), 1),
            Row.of(Tags.of("tag1", "value2", "tag2", "value1"), 2),
            Row.of(Tags.of("tag1", "value3", "tag2", "value1"), 0),
            Row.of(Tags.of("tag1", "value1", "tag2", "value2"), 4)),
        true);
    {
      HealthLookup lookup = service2.getHealthLookup();
      assertThat(lookup.getStatus()).isEqualTo(HealthStatus.OK);
      // no groupby -> one value
      assertThat(lookup.getPerfDatas()).hasSize(3);
      assertThat(lookup.getMessage()).isEqualTo("All Right");
      assertThat(lookup.getPerfDatas())
          .satisfiesExactlyInAnyOrder(
              i -> {
                assertThat(i.getName()).isEqualTo("value1");
                assertThat(i.getValue()).isEqualTo(1l);
                assertThat(i.getUnit()).isEqualTo("item");
              },
              i -> {
                assertThat(i.getName()).isEqualTo("value2");
                assertThat(i.getValue()).isEqualTo(2l);
                assertThat(i.getUnit()).isEqualTo("item");
              },
              i -> {
                assertThat(i.getName()).isEqualTo("value3");
                assertThat(i.getValue()).isEqualTo(0l);
                assertThat(i.getUnit()).isEqualTo("item");
              });
    }
  }
}
