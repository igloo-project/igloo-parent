package igloo.console.common.form;

import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

public class JavaClassDropDownMultipleChoice
    extends GenericSelect2DropDownMultipleChoice<Class<?>> {

  private static final long serialVersionUID = 8484823009218408585L;

  public <C extends Collection<Class<?>>> JavaClassDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      IModel<? extends Collection<? extends Class<?>>> choicesModel) {
    super(id, model, collectionSupplier, choicesModel, JavaClassChoiceRenderer.get());
  }
}
