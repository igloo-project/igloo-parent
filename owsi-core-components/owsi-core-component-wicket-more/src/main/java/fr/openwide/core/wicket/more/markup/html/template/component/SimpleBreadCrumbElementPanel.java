package fr.openwide.core.wicket.more.markup.html.template.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;

import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

public class SimpleBreadCrumbElementPanel extends GenericPanel<String> {

	private static final long serialVersionUID = 5385792712763242343L;

	public SimpleBreadCrumbElementPanel(String id, BreadCrumbElement breadCrumbElement, BreadCrumbMarkupTagRenderingBehavior renderingBehavior) {
		super(id, breadCrumbElement.getLabelModel());
		
		add(
				new Label("breadCrumbElementLabel", getModel())
						.add(renderingBehavior)
		);
	}

}
