package basicapp.front.common.form;

import basicapp.back.business.referencedata.controller.IReferenceDataControllerService;
import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.front.referencedata.renderer.ReferenceDataRenderer;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.form.GenericEntityRendererToChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

public class ReferenceDataDropDownMultipleChoice<T extends ReferenceData<? super T>>
    extends GenericSelect2DropDownMultipleChoice<T> {

  private static final long serialVersionUID = -4170507171595192143L;

  public <C extends Collection<T>> ReferenceDataDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      Class<T> clazz) {
    this(id, model, collectionSupplier, new GenericReferenceDataModel<>(clazz));
  }

  public <C extends Collection<T>> ReferenceDataDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      IModel<? extends Collection<? extends T>> choicesModel) {
    super(
        id,
        model,
        collectionSupplier,
        choicesModel,
        GenericEntityRendererToChoiceRenderer.of(ReferenceDataRenderer.get()));
  }

  private static final class GenericReferenceDataModel<T extends ReferenceData<? super T>>
      extends LoadableDetachableModel<List<T>> {

    private static final long serialVersionUID = 1L;

    private final Class<T> clazz;

    @SpringBean private IReferenceDataControllerService<T> referenceDataControllerService;

    public GenericReferenceDataModel(Class<T> clazz) {
      super();
      Injector.get().inject(this);
      this.clazz = clazz;
    }

    @Override
    protected List<T> load() {
      return referenceDataControllerService.findAllEnabled(clazz);
    }
  }
}
