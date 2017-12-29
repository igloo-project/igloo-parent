package org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer;

import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;

public interface IBootstrapRendererModel extends IModel<String> {
	
	IModel<String> getIconCssClassModel();

	IModel<IBootstrapColor> getColorModel();

	IModel<String> getTooltipModel();
	
}