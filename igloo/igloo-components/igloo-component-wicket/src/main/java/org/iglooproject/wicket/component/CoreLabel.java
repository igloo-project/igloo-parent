package org.iglooproject.wicket.component;

import java.io.Serializable;

import org.apache.wicket.model.IModel;

public class CoreLabel extends AbstractCoreLabel<CoreLabel> {
	
	private static final long serialVersionUID = 1697388050602143288L;
	
	public CoreLabel(String id, IModel<?> model) {
		super(id, model);
	}
	
	public CoreLabel(String id, Serializable label) {
		super(id, label);
	}
	
	@Override
	protected CoreLabel thisAsT() {
		return this;
	}
}
