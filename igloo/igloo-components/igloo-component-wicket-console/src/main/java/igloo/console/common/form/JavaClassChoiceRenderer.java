package igloo.console.common.form;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public final class JavaClassChoiceRenderer extends ChoiceRenderer<Class<?>> {

  private static final long serialVersionUID = 1L;

  private static final JavaClassChoiceRenderer INSTANCE = new JavaClassChoiceRenderer();

  public static JavaClassChoiceRenderer get() {
    return INSTANCE;
  }

  private JavaClassChoiceRenderer() {}

  @Override
  public Object getDisplayValue(Class<?> clazz) {
    return clazz != null ? clazz.getSimpleName() : "";
  }

  @Override
  public String getIdValue(Class<?> clazz, int index) {
    return clazz != null ? String.valueOf(index) : "-1";
  }
}
