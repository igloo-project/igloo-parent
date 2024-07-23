package igloo.console.maintenance.task.component;

import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownMultipleChoice;

public class TaskStatusDropDownMultipleChoice extends EnumDropDownMultipleChoice<TaskStatus> {

  private static final long serialVersionUID = 3147073422245294521L;

  public <C extends Collection<TaskStatus>> TaskStatusDropDownMultipleChoice(
      String id, IModel<C> model, SerializableSupplier2<? extends C> collectionSupplier) {
    super(id, model, collectionSupplier, TaskStatus.class);
  }
}
