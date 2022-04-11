package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.tester.WicketTesterExtension;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.igloo.monitoring.perfdata.HealthLookup;
import org.igloo.monitoring.perfdata.HealthService;
import org.igloo.monitoring.perfdata.HealthStatus;
import org.igloo.monitoring.perfdata.IHealthMetricService;
import org.igloo.monitoring.perfdata.IHealthService;
import org.igloo.monitoring.perfdata.PerfData;
import org.igloo.monitoring.wicket.MonitoringResource;
import org.igloo.monitoring.wicket.SpringBackedMonitoringResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class TestPage {

	@RegisterExtension
	public WicketTesterExtension scope = new WicketTesterExtension();

	@Test
	void testPageOk() {
		WicketTester tester = scope.getTester();
		ResourceReference resourceReference = resourceReference(HealthStatus.OK, "All Right", null);
		tester.startResourceReference(resourceReference);
		assertThat(tester.getLastResponseAsString()).isEqualTo("OK metric.name - All Right");
		assertThat(tester.getLastResponse().getHeader("Content-Type")).isEqualTo("text/plain; charset=UTF-8");
		assertThat(tester.getLastResponse().getHeader("Cache-Control")).isEqualTo("no-cache");
		assertThat(tester.getLastResponse().getHeader("X-Status")).isEqualTo("0"); // 0 for OK
	}

	@Test
	void testPageWarning() {
		WicketTester tester = scope.getTester();
		ResourceReference resourceReference = resourceReference(HealthStatus.WARNING, "Some problems here !", null);
		tester.startResourceReference(resourceReference);
		assertThat(tester.getLastResponseAsString()).isEqualTo("WARNING metric.name - Some problems here !");
		assertThat(tester.getLastResponse().getHeader("Content-Type")).isEqualTo("text/plain; charset=UTF-8");
		assertThat(tester.getLastResponse().getHeader("Cache-Control")).isEqualTo("no-cache");
		assertThat(tester.getLastResponse().getHeader("X-Status")).isEqualTo("1"); // 1 for WARNING
	}

	@Test
	void testPageCritical() {
		WicketTester tester = scope.getTester();
		ResourceReference resourceReference = resourceReference(HealthStatus.CRITICAL, "Big trouble :-/", null);
		tester.startResourceReference(resourceReference);
		assertThat(tester.getLastResponseAsString()).isEqualTo("CRITICAL metric.name - Big trouble :-/");
		assertThat(tester.getLastResponse().getHeader("Content-Type")).isEqualTo("text/plain; charset=UTF-8");
		assertThat(tester.getLastResponse().getHeader("Cache-Control")).isEqualTo("no-cache");
		assertThat(tester.getLastResponse().getHeader("X-Status")).isEqualTo("2"); // 2 for CRITICAL
	}

	@Test
	void testPageUnknown() {
		WicketTester tester = scope.getTester();
		ResourceReference resourceReference = resourceReference(HealthStatus.UNKNOWN, "Status not known", null);
		tester.startResourceReference(resourceReference);
		assertThat(tester.getLastResponseAsString()).isEqualTo("UNKNOWN metric.name - Status not known");
		assertThat(tester.getLastResponse().getHeader("Content-Type")).isEqualTo("text/plain; charset=UTF-8");
		assertThat(tester.getLastResponse().getHeader("Cache-Control")).isEqualTo("no-cache");
		assertThat(tester.getLastResponse().getHeader("X-Status")).isEqualTo("3"); // 3 for UNKNOWN
	}

	@Test
	void testSpring() {
		WicketTester tester = scope.getTester();
		ApplicationContextMock ctx = new ApplicationContextMock();
		new SpringComponentInjector(tester.getApplication(), ctx);
		IHealthMetricService service1 = mock(IHealthMetricService.class);
		IHealthMetricService service2 = mock(IHealthMetricService.class);
		when(service1.getHealthLookup()).thenReturn(new HealthLookup("metric.name1", HealthStatus.OK, "All Right", null));
		when(service2.getHealthLookup()).thenReturn(new HealthLookup("metric.name2", HealthStatus.WARNING, "Some problems", null));
		IHealthService service = HealthService.of("metric.name1", service1, "metric.name2", service2);
		ctx.putBean(service);
		IResource resource1 = new SpringBackedMonitoringResource("metric.name1");
		IResource resource2 = new SpringBackedMonitoringResource("metric.name2");
		
		tester.startResource(resource1);
		assertThat(tester.getLastResponseAsString()).isEqualTo("OK metric.name1 - All Right");

		tester.startResource(resource2);
		assertThat(tester.getLastResponseAsString()).isEqualTo("WARNING metric.name2 - Some problems");
	}

	@Test
	void testUrl() {
		WicketTester tester = scope.getTester();
		ResourceReference resourceReference = resourceReference(HealthStatus.UNKNOWN, "Status not known", null);
		tester.getApplication().mountResource("/monitoring/metric1", resourceReference);
		tester.executeUrl("./monitoring/metric1");
		assertThat(tester.getLastResponseAsString()).isEqualTo("UNKNOWN metric.name - Status not known");
	}

	private ResourceReference resourceReference(HealthStatus status, String message, List<PerfData> perfDatas) {
		IHealthMetricService service1 = mock(IHealthMetricService.class);
		when(service1.getHealthLookup()).thenReturn(new HealthLookup("metric.name", status, message, perfDatas));
		IHealthService service = HealthService.of("metric.name", service1);
		SerializableSupplier<IResource> resourceSupplier = () -> new MonitoringResource() {
			private static final long serialVersionUID = 1229871683511127023L;

			@Override
			public HealthLookup getHealthLookup() {
				return service.getHealthLookup("metric.name");
			}
		};
		ResourceReference resourceReference = SharedResourceReference.of("metric1", resourceSupplier);
		return resourceReference;
	}

}
