package fr.openwide.core.wicket.more.markup.html.feedback;

import java.util.concurrent.TimeUnit;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.alert.AlertJavascriptResourceReference;

public class AnimatedGlobalFeedbackPanel extends GlobalFeedbackPanel {

	private static final long serialVersionUID = 2213180445046166086L;
	
	private static final int DEFAULT_AUTOHIDE_DELAY_VALUE = 5;
	
	private static final TimeUnit DEFAULT_AUTOHIDE_DELAY_UNIT = TimeUnit.SECONDS;

	private int autohideDelayValue;
	
	private TimeUnit autohideDelayUnit;
	
	public AnimatedGlobalFeedbackPanel(String id) {
		this(id, DEFAULT_AUTOHIDE_DELAY_VALUE, DEFAULT_AUTOHIDE_DELAY_UNIT);
	}
	
	/**
	 * @param id Id wicket.
	 * @param autohideDelay Délai de fermeture automatique, en secondes.
	 * 						Si < 0 : le feedback ne se cache pas automatiquement
	 */
	public AnimatedGlobalFeedbackPanel(String id, int autohideDelayValue, TimeUnit autohideDelayUnit) {
		super(id);
		setOutputMarkupId(true);
		
		this.autohideDelayValue = autohideDelayValue;
		this.autohideDelayUnit = autohideDelayUnit;
		
		WebMarkupContainer closeTrigger = new WebMarkupContainer("closeTrigger");
		add(closeTrigger);
		closeTrigger.add(new CloseAlertBehavior());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(AlertJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().append("$.fn.alert.reset('#")
				.append(getMarkupId())
				.append("', ")
				.append(String.valueOf(autohideDelayUnit.toMicros(autohideDelayValue)))
				.append(")").render()));
		
		super.renderHead(response);
	}

	private static class CloseAlertBehavior extends Behavior {
		private static final long serialVersionUID = -5585291224997829649L;

		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			super.renderHead(component, response);
			
			Event event = new Event(MouseEvent.CLICK) {
				private static final long serialVersionUID = 2160875652552472051L;
				
				@Override
				public JsScope callback() {
					return JsScopeEvent.quickScope("$.fn.alert.close(event);");
				}
			};
			
			response.render(OnDomReadyHeaderItem.forScript(new JsQuery(component).$().chain(event).render()));
		}
	}

	@Override
	public FeedbackPanel getFeedbackPanel(String id, int level, MarkupContainer component) {
		return new ErrorLevelFeedbackPanel(id, level) {
			private static final long serialVersionUID = 4843857703266180362L;
			
			private boolean visible = false;
			
			@Override
			public boolean isVisible() {
				return visible;
			}
			
			@Override
			protected void onConfigure() {
				// cf WICKET-4536 : ceci dit, même après mise à jour, on a encore eu des cas avec le problème
				// du coup, on laisse comme ça
				super.onConfigure();
				
				visible = anyMessage(level);
			}
		};
	}
}
