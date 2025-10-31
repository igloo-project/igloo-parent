package org.iglooproject.wicket.more.bindable.form;

import igloo.wicket.model.Detachables;
import java.util.Arrays;
import java.util.List;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;

/**
 * A form that automatically write {@link IBindableModel}'s caches to the underlying objects before
 * validation and upon validation error, so that the underlying objects are always up-to-date.
 *
 * <p>This is useful when you have to use the underlying objects for treatments that expect these to
 * be up-to-date when an error occurs.
 */
public class CacheWritingForm<E> extends ModelValidatingForm<E> {

  private static final long serialVersionUID = 5036749558996684273L;

  private final IBindableModel<E> mainRootModel;

  private final List<IBindableModel<?>> otherRootModels;

  public CacheWritingForm(String id, IBindableModel<E> mainRootModel) {
    this(id, mainRootModel, (IBindableModel<E>[]) null);
  }

  public CacheWritingForm(
      String id, IBindableModel<E> mainRootModel, IBindableModel<?>... otherRootModels) {
    super(id, mainRootModel);
    this.mainRootModel = mainRootModel;
    this.otherRootModels =
        otherRootModels == null ? List.of() : List.copyOf(Arrays.asList(otherRootModels));
  }

  // TODO WICKET - REMOVE THIS WHEN UPGRADE UP TO 10.7.0
  @Override
  protected void onValidateModelObjects() {
    // Make sure sub-form models are up-to-date
    writeAll();
    super.onValidateModelObjects();
  }

  // TODO WICKET - ADD THIS WHEN UPGRADE UP TO 10.7.0
  //  @Override
  //  protected void onAfterUpdateFormComponentModels() {
  //    // Make sure sub-form models are up-to-date
  //    writeAll();
  //    super.onAfterUpdateFormComponentModels();
  //  }

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
