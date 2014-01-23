package fr.openwide.core.wicket.markup.html.basic;

import org.apache.wicket.model.IModel;

public class HideableMultiLineLabel extends AbstractCoreLabel<HideableMultiLineLabel> {

	private static final long serialVersionUID = 1L;

	public HideableMultiLineLabel(String id, IModel<?> model) {
		super(id, model);
		hideIfEmpty();
		multiline();
	}

	public HideableMultiLineLabel(String id, String label) {
		super(id, label);
		hideIfEmpty();
		multiline();
	}

	@Override
	protected HideableMultiLineLabel thisAsT() {
		return this;
	}

}
