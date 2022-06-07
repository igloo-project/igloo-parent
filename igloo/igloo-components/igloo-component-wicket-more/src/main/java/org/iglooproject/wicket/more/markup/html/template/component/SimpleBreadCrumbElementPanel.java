package org.iglooproject.wicket.more.markup.html.template.component;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.component.CoreLabel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

public class SimpleBreadCrumbElementPanel extends GenericPanel<String> {

	private static final long serialVersionUID = 5385792712763242343L;

	public SimpleBreadCrumbElementPanel(String id, BreadCrumbElement breadCrumbElement, BreadCrumbMarkupTagRenderingBehavior renderingBehavior) {
		super(id, breadCrumbElement.getLabelModel());
		
		add(
			new CoreLabel("breadCrumbElementLabel", getModel())
				.add(renderingBehavior)
		);
	}

}
