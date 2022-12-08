package igloo.bootstrap.renderer;

import org.apache.wicket.model.IModel;

import igloo.bootstrap.common.IBootstrapColor;

public interface IBootstrapRendererModel extends IModel<String> {

	IModel<String> getIconCssClassModel();

	IModel<IBootstrapColor> getColorModel();

	IModel<String> getTooltipModel();

}