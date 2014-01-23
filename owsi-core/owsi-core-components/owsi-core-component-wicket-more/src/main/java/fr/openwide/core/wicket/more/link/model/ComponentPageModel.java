package fr.openwide.core.wicket.more.link.model;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.springframework.util.Assert;

public class ComponentPageModel extends AbstractReadOnlyModel<Page> {
	
	private static final long serialVersionUID = 6402816198383449645L;
	
	private final Component component;

	public ComponentPageModel(Component component) {
		this.component = component;
		Assert.notNull(component);
	}
	
	@Override
	public Page getObject() {
		return component.getPage();
	}

}
