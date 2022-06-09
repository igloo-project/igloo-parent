package igloo.wicket.markup.html.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class GenericPanel<T> extends Panel {

	private static final long serialVersionUID = 7229728038574137817L;

	public GenericPanel(String id) {
		super(id);
	}
	
	public GenericPanel(String id, IModel<? extends T> model) {
		super(id, model);
	}
	
	public void setModel(IModel<? extends T> model) {
		super.setDefaultModel(model);
	}
	
	public void setModelObject(T object) {
		super.setDefaultModelObject(object);
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