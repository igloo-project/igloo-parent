package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.waypoints;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class WaypointsStickyBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = 7238632559473294644L;

	public WaypointsStickyBehavior() {
		super();
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(WaypointsStickyJavaScriptResourceReference.get()));
		
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

	public JsStatement statement() {
		return new JsStatement().append("var sticky" + getComponent().getMarkupId() + " = new Waypoint.Sticky({element: $('#" + getComponent().getMarkupId() + "')})");
	}

}
