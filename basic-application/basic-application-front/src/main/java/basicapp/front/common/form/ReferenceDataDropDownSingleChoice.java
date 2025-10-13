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
import org.iglooproject.wicket.more.markup.html.form.GenericEntityRendererToChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.model.GenericReferenceDataModel;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class ReferenceDataDropDownSingleChoice<T extends ReferenceData<? super T>>
    extends GenericSelect2DropDownSingleChoice<T> {

  private static final long serialVersionUID = -1857374154341270451L;

  public ReferenceDataDropDownSingleChoice(String id, IModel<T> model, Class<T> clazz) {
    this(id, model, new GenericReferenceDataModel<>(clazz));
  }

  public ReferenceDataDropDownSingleChoice(
      String id, IModel<T> model, IModel<? extends Collection<? extends T>> choicesModel) {
    super(
        id,
        model,
        choicesModel,
        GenericEntityRendererToChoiceRenderer.of(ReferenceDataRenderer.get()));
    setNullValid(true);
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
