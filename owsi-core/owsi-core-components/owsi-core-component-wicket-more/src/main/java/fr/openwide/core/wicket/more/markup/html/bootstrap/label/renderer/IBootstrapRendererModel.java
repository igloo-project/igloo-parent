package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;

public interface IBootstrapRendererModel extends IModel<String> {
	
	IModel<String> getIconCssClassModel();

	IModel<IBootstrapColor> getColorModel();

	IModel<String> getTooltipModel();
	
}