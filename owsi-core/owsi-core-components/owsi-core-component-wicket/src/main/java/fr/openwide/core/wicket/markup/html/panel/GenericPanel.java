package fr.openwide.core.wicket.markup.html.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class GenericPanel<T> extends Panel {

	private static final long serialVersionUID = 7229728038574137817L;
	
	public GenericPanel(String id, IModel<T> model) {
		super(id, model);
	}
	
	public final void setModel(IModel<T> model) {
		super.setDefaultModel(model);
	}
	
	@SuppressWarnings("unchecked")
	public final IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}
	
	@SuppressWarnings("unchecked")
	public final T getModelObject() {
		return (T) getDefaultModelObject();
	}

}