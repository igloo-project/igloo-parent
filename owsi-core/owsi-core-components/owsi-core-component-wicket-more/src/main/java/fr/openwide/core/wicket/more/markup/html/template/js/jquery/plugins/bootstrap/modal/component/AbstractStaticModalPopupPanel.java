package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component;

import org.apache.wicket.model.IModel;

public abstract class AbstractStaticModalPopupPanel<O> extends AbstractModalPopupPanel<O> {

	private static final long serialVersionUID = 1202659281986076127L;

	public AbstractStaticModalPopupPanel(String id, IModel<? extends O> model) {
		super(id, model);
	}

}