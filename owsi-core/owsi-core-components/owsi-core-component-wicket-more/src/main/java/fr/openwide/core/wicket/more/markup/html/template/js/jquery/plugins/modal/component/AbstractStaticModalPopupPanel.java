package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.javascript.JsScope;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ModalOpenOnClickBehavior;

public abstract class AbstractStaticModalPopupPanel<O> extends AbstractModalPopupPanel<O> {

	private static final long serialVersionUID = 2561629860588700049L;

	public AbstractStaticModalPopupPanel(String id, IModel<O> model) {
		super(id, model);
	}

	public void prepareLink(final Component link) {
		link.add(new ModalOpenOnClickBehavior(getContainer()) {
			private static final long serialVersionUID = 6174499573502055353L;
			
			@Override
			protected JsScope onComplete() {
				return AbstractStaticModalPopupPanel.this.onModalComplete();
			}
			
		});
	}

	public JsScope onModalComplete() {
		return null;
	}

}
