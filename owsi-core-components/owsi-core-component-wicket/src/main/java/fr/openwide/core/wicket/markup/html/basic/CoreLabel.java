package fr.openwide.core.wicket.markup.html.basic;

import org.apache.wicket.model.IModel;

public class CoreLabel extends AbstractCoreLabel<CoreLabel> {
	
	private static final long serialVersionUID = 1697388050602143288L;
	
	public CoreLabel(String id, IModel<?> model) {
		super(id, model);
	}
	
	@Override
	protected CoreLabel thisAsT() {
		return this;
	}
}
