package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.analytics;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.springframework.util.StringUtils;

public class GoogleAnalyticsBehavior extends Behavior {

	private static final long serialVersionUID = 6908054186870735613L;

	private final GoogleAnalyticsHeaderContributor headerContributor;

	public GoogleAnalyticsBehavior(String account) {
		super();
		if (StringUtils.hasText(account)) {
			headerContributor = new GoogleAnalyticsHeaderContributor(account);
		} else {
			headerContributor = null;
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		if (headerContributor != null) {
			headerContributor.renderHead(response);
		}
	}

}
