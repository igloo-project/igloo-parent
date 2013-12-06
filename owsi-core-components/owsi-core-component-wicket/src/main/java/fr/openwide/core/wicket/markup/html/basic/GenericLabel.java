package fr.openwide.core.wicket.markup.html.basic;

import org.apache.wicket.model.IModel;

public class GenericLabel<E, T extends GenericLabel<E, T>> extends AbstractCoreLabel<GenericLabel<E, T>> {
	
	private static final long serialVersionUID = -6956425366331256600L;
	
	private IModel<E> mainModel;
	
	public GenericLabel(String id, IModel<E> model) {
		super(id, model);
		this.mainModel = wrap(model);
	}
	
	@Override
	protected GenericLabel<E, T> thisAsT() {
		return this;
	}
	
	@Override
	protected E getMainModelObject() {
		if (mainModel != null) {
			return mainModel.getObject();
		}
		return null;
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		if (mainModel != null) {
			mainModel.detach();
		}
	}
}
