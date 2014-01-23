package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.analytics;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.odlabs.wiquery.core.javascript.JsUtils;

public class GoogleAnalyticsHeaderContributor implements IHeaderContributor {

	private static final long serialVersionUID = 1208938645166790043L;

	private final CharSequence script;

	public GoogleAnalyticsHeaderContributor(String account) {
		super();
		
		StringBuilder sb = new StringBuilder();
		sb.append("var gaJsHost = (('https:' == document.location.protocol) ? 'https://ssl.' : 'http://www.');").append("\n");
		sb.append("$.getScript(gaJsHost + 'google-analytics.com/ga.js', function(){").append("\n");
		// - début callback de chargement google-analytics.com/ga.js
		sb.append("try {").append("\n");
		sb.append("_gaq.push(['_setAccount', ").append(JsUtils.quotes(account)).append("]);").append("\n");
		sb.append("_gaq.push(['_trackPageview']);").append("\n");
		sb.append("} catch (err) {}").append("\n");
		// - fin callback de chargement google-analytics/ga.js
		sb.append("});").append("\n");
		
		this.script = sb.toString();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(OnDomReadyHeaderItem.forScript(script));
	}

}