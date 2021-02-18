package org.iglooproject.wicket.more.markup.html.feedback;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;

import java.util.concurrent.TimeUnit;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.feedback.FeedbackJavaScriptResourceReference;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class AnimatedGlobalFeedbackPanel extends GlobalFeedbackPanel {

	private static final long serialVersionUID = 2213180445046166086L;
	
	@SpringBean
	private IPropertyService propertyService;
	
	private Integer autohideDelayValue;
	
	private TimeUnit autohideDelayUnit;
	
	public AnimatedGlobalFeedbackPanel(String id) {
		this(id, null, null);
	}
	
	/**
	 * @param id Id wicket.
	 * @param autohideDelay Délai de fermeture automatique, en secondes.
	 * 						Si < 0 : le feedback ne se cache pas automatiquement
	 */
	public AnimatedGlobalFeedbackPanel(String id, Integer autohideDelayValue, TimeUnit autohideDelayUnit) {
		super(id);
		setOutputMarkupId(true);
		
		this.autohideDelayValue = autohideDelayValue != null ? autohideDelayValue : propertyService.get(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE);
		this.autohideDelayUnit = autohideDelayUnit != null ? autohideDelayUnit : propertyService.get(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT);
		
		Args.notNull(this.autohideDelayValue, "autohideDelayValue");
		Args.notNull(this.autohideDelayUnit, "autohideDelayUnit");
		
		WebMarkupContainer closeTrigger = new WebMarkupContainer("closeTrigger");
		add(closeTrigger);
		closeTrigger.add(new CloseAlertBehavior());
		
		// To retrieve main panel on close event.
		add(new ClassAttributeAppender(new Model<String>("animated-global-feedback")));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(FeedbackJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().append("$.fn.feedback.reset('#")
				.append(getMarkupId())
				.append("', ")
				.append(String.valueOf(autohideDelayUnit.toMillis(autohideDelayValue)))
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
					return JsScopeEvent.quickScope("$.fn.feedback.close(event);");
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
