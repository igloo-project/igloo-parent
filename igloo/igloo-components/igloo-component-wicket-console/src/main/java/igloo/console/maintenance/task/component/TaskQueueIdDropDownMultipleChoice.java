package igloo.console.maintenance.task.component;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.task.model.DefaultQueueId;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

public class TaskQueueIdDropDownMultipleChoice
    extends GenericSelect2DropDownMultipleChoice<String> {

  private static final long serialVersionUID = 3147073422245294521L;

  private static final String DEFAULT_QUEUE_ID_VALUE = DefaultQueueId.DEFAULT.getUniqueStringId();

  public <C extends Collection<String>> TaskQueueIdDropDownMultipleChoice(
      String id, IModel<C> model, SerializableSupplier2<? extends C> collectionSupplier) {
    this(id, model, collectionSupplier, new ChoicesModel());
  }

  public <C extends Collection<String>> TaskQueueIdDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      IModel<? extends Collection<? extends String>> choicesModel) {
    super(id, model, collectionSupplier, choicesModel, new ChoiceRenderer());
  }

  private static class ChoicesModel extends LoadableDetachableModel<List<String>> {
    private static final long serialVersionUID = 1L;

    @SpringBean private IQueuedTaskHolderManager taskManager;

    @SpringBean private IRendererService rendererService;

    public ChoicesModel() {
      Injector.get().inject(this);
    }

    @Override
    protected List<String> load() {
      return ImmutableList.<String>builder()
          .add(rendererService.localize("console.maintenance.task.common.queue.default"))
          .addAll(
              taskManager.getQueueIds().stream()
                  .map(IQueueId::getUniqueStringId)
                  .collect(ImmutableList.toImmutableList()))
          .build();
    }
  }

  private static class ChoiceRenderer
      extends org.apache.wicket.markup.html.form.ChoiceRenderer<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String getIdValue(String object, int index) {
      if (object == null) {
        return DEFAULT_QUEUE_ID_VALUE;
      }
      return object;
    }
  }
}
