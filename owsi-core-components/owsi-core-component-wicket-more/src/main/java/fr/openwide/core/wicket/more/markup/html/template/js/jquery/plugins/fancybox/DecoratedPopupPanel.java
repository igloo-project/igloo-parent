package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;


/**
 * wrap popup content with title
 */
public abstract class DecoratedPopupPanel extends FancyboxPopupPanel {

	private static final long serialVersionUID = -8004613923770413483L;

	protected DecoratedPopupPanel(String id, boolean hidden) {
		super(id, hidden);
	}

	public DecoratedPopupPanel(String id, String popupTitle, boolean hidden) {
		this(id, hidden);
		
		getReplaceableContainer().add(new Label("popupTitle", new ResourceModel(popupTitle)));
	}

	@Override
	protected final Panel getPopupContentPanel(String id) {
		return getNotDecoratedPopupContentPanel(id);
	}
	
	protected abstract Panel getNotDecoratedPopupContentPanel(String id);

}
