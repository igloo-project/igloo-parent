package fr.openwide.core.wicket.more.markup.html.template.component;

import org.apache.wicket.markup.html.basic.Label;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SimpleBreadCrumbElementPanel extends GenericPanel<String> {

	private static final long serialVersionUID = 5385792712763242343L;

	public SimpleBreadCrumbElementPanel(String id, BreadCrumbElement breadCrumbElement) {
		super(id, breadCrumbElement.getLabelModel());
		
		add(new Label("breadCrumbElementLabel", getModel()));
	}

}
