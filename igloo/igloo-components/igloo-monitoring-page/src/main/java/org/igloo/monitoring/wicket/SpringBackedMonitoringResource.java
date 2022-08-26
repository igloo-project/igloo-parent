package org.igloo.monitoring.wicket;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.igloo.monitoring.service.HealthLookup;
import org.igloo.monitoring.service.IHealthService;

/**
 * This {@link MonitoringResource} is made to be used with a spring context providing a unique {@link IHealthService}.
 * Metrics is looked up by {@code metricName} provided in constructor.
 */
public class SpringBackedMonitoringResource extends MonitoringResource {

	private static final long serialVersionUID = 1397957655541241281L;

	public static final ResourceReference get(String name) {
		return new ResourceReference(
				SpringBackedMonitoringResource.class,
				"name"
			) {
				private static final long serialVersionUID = 1L;
				@Override
				public IResource getResource() {
					return new SpringBackedMonitoringResource(name);
				}
			};
	}

	private String metricName;

	@SpringBean
	private IHealthService healthService;

	public SpringBackedMonitoringResource(String metricName) {
		Injector.get().inject(this);
		this.metricName = metricName;
	}

	@Override
	public HealthLookup getHealthLookup() {
		return healthService.getHealthLookup(metricName);
	}
}
