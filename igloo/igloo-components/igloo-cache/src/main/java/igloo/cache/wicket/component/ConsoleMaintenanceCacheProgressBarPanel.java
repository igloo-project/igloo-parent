package igloo.cache.wicket.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class ConsoleMaintenanceCacheProgressBarPanel extends GenericPanel<Float> {

  private static final long serialVersionUID = 3992803188589160149L;

  private static final String CLASS_PROGRESS_SUCCESS = "bg-success";
  private static final String CLASS_PROGRESS_WARNING = "bg-warning";
  private static final String CLASS_PROGRESS_DANGER = "bg-danger";

  public ConsoleMaintenanceCacheProgressBarPanel(
      String id, IModel<Float> valueModel, boolean sign, float low, float high) {
    super(id, valueModel);

    if (low > high) {
      throw new IllegalArgumentException("low threshold has to be less lower than high threshold");
    }

    IModel<Double> widthModel =
        () -> {
          Float value = valueModel.getObject();
          return value != null ? Math.ceil(value * 100) : 0;
        };

    IModel<String> colorModel =
        () -> {
          Float value = valueModel.getObject();

          if (value == null) {
            return null;
          }

          if (sign) {
            if (getModelObject() < low) {
              return CLASS_PROGRESS_SUCCESS;
            } else if (getModelObject() < high) {
              return CLASS_PROGRESS_WARNING;
            } else {
              return CLASS_PROGRESS_DANGER;
            }
          } else {
            if (getModelObject() < low) {
              return CLASS_PROGRESS_DANGER;
            } else if (getModelObject() < high) {
              return CLASS_PROGRESS_WARNING;
            } else {
              return CLASS_PROGRESS_SUCCESS;
            }
          }
        };

    add(
        new WebMarkupContainer("progressBar")
            .add(new AttributeModifier("style", () -> "width:" + widthModel.getObject() + "%;"))
            .add(new ClassAttributeAppender(colorModel)));
  }
}
