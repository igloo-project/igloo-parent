package igloo.console.common.form;

import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class JavaClassDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<Class<?>> {

  private static final long serialVersionUID = 8484823009218408585L;

  public JavaClassDropDownSingleChoice(
      String id,
      IModel<Class<?>> model,
      IModel<? extends Collection<? extends Class<?>>> choicesModel) {
    super(id, model, choicesModel, JavaClassChoiceRenderer.get());
  }
}
