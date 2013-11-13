package fr.openwide.core.wicket.markup.html.basic;

import org.apache.wicket.model.IModel;

public class HideableLabel extends AbstractCoreLabel<HideableLabel> {

	private static final long serialVersionUID = 1L;

	public HideableLabel(String id, IModel<?> model) {
		super(id, model);
		hideIfEmpty();
	}

	public HideableLabel(String id, String label) {
		super(id, label);
		hideIfEmpty();
	}

	@Override
	protected HideableLabel thisAsT() {
		return this;
	}

}
