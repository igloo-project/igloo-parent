package igloo.bootstrap.renderer;

import igloo.bootstrap.common.IBootstrapColor;
import org.apache.wicket.model.IModel;

public interface IBootstrapRendererModel extends IModel<String> {

  IModel<String> getIconCssClassModel();

  IModel<IBootstrapColor> getColorModel();

  IModel<String> getTooltipModel();
}
