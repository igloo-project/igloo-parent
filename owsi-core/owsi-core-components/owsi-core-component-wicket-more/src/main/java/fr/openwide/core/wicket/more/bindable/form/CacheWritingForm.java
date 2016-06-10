package fr.openwide.core.wicket.more.bindable.form;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.bindable.model.IBindableModel;
import fr.openwide.core.wicket.more.markup.html.form.ModelValidatingForm;
import fr.openwide.core.wicket.more.util.model.Detachables;

/**
 * A form that automatically write {@link IBindableModel}'s caches to the underlying objects before validation and
 * upon validation error, so that the underlying objects are always up-to-date.
 * 
 * <p>This is useful when you have to use the underlying objects for treatments that expect these to be up-to-date when
 * an error occurs.
 */
public class CacheWritingForm<E> extends ModelValidatingForm<E> {

	private static final long serialVersionUID = 5036749558996684273L;

	private final IBindableModel<E> mainRootModel;
	
	private final ImmutableList<IBindableModel<?>> otherRootModels;
	
	public CacheWritingForm(String id, IBindableModel<E> mainRootModel) {
		this(id, mainRootModel, (IBindableModel<E>[]) null);
	}
	
	public CacheWritingForm(String id, IBindableModel<E> mainRootModel, IBindableModel<?> ... otherRootModels) {
		super(id, mainRootModel);
		this.mainRootModel = mainRootModel;
		this.otherRootModels =
				otherRootModels == null ? ImmutableList.<IBindableModel<?>>of() : ImmutableList.copyOf(otherRootModels);
	}

	@Override
	protected void onValidateModelObjects() {
		// Make sure sub-form models are up-to-date
		writeAll();
		super.onValidateModelObjects();
	}
	
	@Override
	protected void onError() {
		super.onError();
		this.updateFormComponentModels();
		writeAll();
	}
	
	protected void writeAll() {
		mainRootModel.writeAll();
		for (IBindableModel<?> otherRootModel : otherRootModels) {
			otherRootModel.writeAll();
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(otherRootModels);
	}
}
