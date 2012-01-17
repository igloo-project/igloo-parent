package fr.openwide.core.showcase.web.application.util.application;

import org.apache.wicket.Application;
import org.apache.wicket.IApplicationListener;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.resource.aggregation.ResourceReferenceAndStringData;
import org.odlabs.wiquery.core.WiQueryDecoratingHeaderResponse;

public class OverrideWiQueryHeaderResponseApplicationListener implements IApplicationListener {

	@Override
	public void onAfterInitialized(Application application) {
		application.setHeaderResponseDecorator(new IHeaderResponseDecorator() {
			public IHeaderResponse decorate(IHeaderResponse response) {
				return new WiQueryDecoratingHeaderResponse(response) {
					@Override
					protected String newGroupingKey(ResourceReferenceAndStringData ref) {
						return null;
					}
				};
			}
		});
	}

	@Override
	public void onBeforeDestroyed(Application application) {
	}

}
