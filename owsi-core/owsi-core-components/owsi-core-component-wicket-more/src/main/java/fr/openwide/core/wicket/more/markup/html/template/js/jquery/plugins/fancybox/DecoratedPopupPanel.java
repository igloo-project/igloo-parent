package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.draggable.DraggableJavaScriptResourceReference;


/**
 * wrap popup content with title
 */
public abstract class DecoratedPopupPanel extends FancyboxPopupPanel {

	private static final long serialVersionUID = -8004613923770413483L;

	private boolean draggable;

	public DecoratedPopupPanel(String id, String popupTitle, boolean hidden, boolean draggable) {
		super(id, hidden);
		
		this.draggable = draggable;
		getReplaceableContainer().add(new Label("popupTitle", new ResourceModel(popupTitle)));
	}

	@Override
	protected final Panel getPopupContentPanel(String id) {
		return getNotDecoratedPopupContentPanel(id);
	}
	
	protected abstract Panel getNotDecoratedPopupContentPanel(String id);

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (draggable) {
			response.render(JavaScriptHeaderItem.forReference(DraggableJavaScriptResourceReference.get()));
		}
	}

	@Override
	public void show(AjaxRequestTarget target) {
		super.show(target);
		
		if (draggable) {
			Options options = new Options();
			options.put("handle", JsUtils.quotes(".popup-title"));
			JsStatement statement = new JsStatement();
			statement.append(new JsStatement().$(getReplaceableContainer(), ".popup-title").addClass("draggable-handle").render(true))
					.append(new JsStatement().$(null, "#fancybox-wrap").chain("draggable", options.getJavaScriptOptions()).render(true));
			target.appendJavaScript(statement.render());
		}
	}

}
