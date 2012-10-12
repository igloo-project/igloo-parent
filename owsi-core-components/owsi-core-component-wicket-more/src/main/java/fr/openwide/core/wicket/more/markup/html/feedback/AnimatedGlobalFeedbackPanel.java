package fr.openwide.core.wicket.more.markup.html.feedback;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.alert.AlertJavascriptResourceReference;

public class AnimatedGlobalFeedbackPanel extends GlobalFeedbackPanel {

	private static final long serialVersionUID = 2213180445046166086L;

	public AnimatedGlobalFeedbackPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		WebMarkupContainer closeTrigger = new WebMarkupContainer("closeTrigger");
		add(closeTrigger);
		closeTrigger.add(new CloseAlertBehavior());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(AlertJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().append("$.fn.alert.reset('#")
				.append(getMarkupId()).append("')").render()));
		
		super.renderHead(response);
	}

	private static class CloseAlertBehavior extends WiQueryEventBehavior {
		private static final long serialVersionUID = -5585291224997829649L;
		
		public CloseAlertBehavior() {
			super(new Event(MouseEvent.CLICK) {
				private static final long serialVersionUID = 2160875652552472051L;
				
				@Override
				public JsScope callback() {
					return JsScopeEvent.quickScope("$.fn.alert.close(event);");
				}
			});
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
				// TODO trac #240 : à enlever avec Wicket 1.5.7 cf WICKET-4536
				super.onConfigure();
				
				visible = anyMessage(level);
			}
		};
	}
}
