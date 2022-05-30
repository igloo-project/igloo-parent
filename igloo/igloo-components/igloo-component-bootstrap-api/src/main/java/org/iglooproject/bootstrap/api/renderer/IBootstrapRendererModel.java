package org.iglooproject.bootstrap.api.renderer;

import org.apache.wicket.model.IModel;
import org.iglooproject.bootstrap.api.common.IBootstrapColor;

public interface IBootstrapRendererModel extends IModel<String> {

	IModel<String> getIconCssClassModel();

	IModel<IBootstrapColor> getColorModel();

	IModel<String> getTooltipModel();

}