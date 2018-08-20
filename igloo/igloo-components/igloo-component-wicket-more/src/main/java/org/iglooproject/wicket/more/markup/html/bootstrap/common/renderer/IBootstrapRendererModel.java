package org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.IBootstrapColor;

public interface IBootstrapRendererModel extends IModel<String> {

	IModel<String> getIconCssClassModel();

	IModel<IBootstrapColor> getColorModel();

	IModel<String> getTooltipModel();

}