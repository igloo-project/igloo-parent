package igloo.wicket.component;

import org.apache.wicket.model.IModel;

public abstract class AbstractGenericLabel<E, T extends AbstractGenericLabel<E, T>> extends AbstractCoreLabel<T> {
	
	private static final long serialVersionUID = 1512490126457208708L;
	
	private IModel<E> mainModel;
	
	public AbstractGenericLabel(String id, IModel<E> model) {
		super(id, model);
		this.mainModel = wrap(model);
	}
	
	@Override
	protected abstract T thisAsT();
	
	@SuppressWarnings("unchecked")
	public final IModel<E> getModel() {
		return (IModel<E>) getDefaultModel();
	}
	
	@SuppressWarnings("unchecked")
	public final E getModelObject() {
		return (E) getDefaultModelObject();
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		if (mainModel != null) {
			mainModel.detach();
		}
	}
}
