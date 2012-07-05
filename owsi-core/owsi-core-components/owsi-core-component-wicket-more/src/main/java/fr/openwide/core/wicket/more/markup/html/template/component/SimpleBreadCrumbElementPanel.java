package fr.openwide.core.wicket.more.markup.html.template.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SimpleBreadCrumbElementPanel extends GenericPanel<BreadCrumbElement> {

	private static final long serialVersionUID = 5385792712763242343L;

	public SimpleBreadCrumbElementPanel(String id, IModel<BreadCrumbElement> model) {
		super(id, model);
		
		add(new Label("breadCrumbElementLabel", new ResourceModel(getModelObject().getLabelKey())));
	}

}
