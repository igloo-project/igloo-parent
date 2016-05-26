package fr.openwide.core.wicket.more.bindable.form;

import fr.openwide.core.wicket.more.bindable.model.IBindableModel;
import fr.openwide.core.wicket.more.markup.html.form.ModelValidatingForm;

/**
 * A form that automatically write {@link IBindableModel}'s caches to the underlying objects before validation and
 * upon validation error, so that the underlying objects are always up-to-date.
 * 
 * <p>This is useful when you have to use the underlying objects for treatments that expect these to be up-to-date when
 * an error occurs.
 */
public class CacheWritingForm<E> extends ModelValidatingForm<E> {

	private static final long serialVersionUID = 5036749558996684273L;

	private final IBindableModel<E> rootModel;
	
	public CacheWritingForm(String id, IBindableModel<E> rootModel) {
		super(id, rootModel);
		this.rootModel = rootModel;
	}

	@Override
	protected void onValidateModelObjects() {
		// Make sure sub-form models are up-to-date
		rootModel.writeAll();
		super.onValidateModelObjects();
	}
	
	@Override
	protected void onError() {
		super.onError();
		this.updateFormComponentModels();
		rootModel.writeAll();
	}
}
