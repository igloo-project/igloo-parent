package fr.openwide.core.wicket.more.markup.html.template.component;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class BreadCrumbPanel extends Panel {
	
	private static final long serialVersionUID = 5796314367995953163L;

	public BreadCrumbPanel(String id, IModel<List<BreadCrumbElement>> breadCrumbModel) {
		super(id, breadCrumbModel);
		
		add(new BreadCrumbListView("breadCrumbElementListView", breadCrumbModel));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isVisible() {
		return ((List<BreadCrumbElement>) getDefaultModelObject()).size() > 0;
	}

}
