package igloo.console.maintenance.task.component;

import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownMultipleChoice;

public class TaskResultDropDownMultipleChoice extends EnumDropDownMultipleChoice<TaskResult> {

  private static final long serialVersionUID = -6536121467575076892L;

  public <C extends Collection<TaskResult>> TaskResultDropDownMultipleChoice(
      String id, IModel<C> model, SerializableSupplier2<? extends C> collectionSupplier) {
    super(id, model, collectionSupplier, TaskResult.class);
  }
}
