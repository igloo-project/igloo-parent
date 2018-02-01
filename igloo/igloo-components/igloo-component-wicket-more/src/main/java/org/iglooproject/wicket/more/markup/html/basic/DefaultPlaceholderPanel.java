package org.iglooproject.wicket.more.markup.html.basic;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.ResourceModel;

public class DefaultPlaceholderPanel extends PlaceholderContainer {
	
	private static final long serialVersionUID = 4513913046135693892L;
	
	public DefaultPlaceholderPanel(String id) {
		super(id);
		add(new Label("emptyField", new ResourceModel("common.field.empty")));
	}
	
	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new PanelMarkupSourcingStrategy(false);
	}
}
