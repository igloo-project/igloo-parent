package igloo.bootstrap.renderer;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.rendering.IRenderer;

import igloo.bootstrap.common.IBootstrapColor;

public interface IBootstrapRenderer<T> extends IRenderer<T> {

	IBootstrapColor renderColor(T value, Locale locale);

	String renderIconCssClass(T value, Locale locale);

	String renderTooltip(T value, Locale locale);

	IBootstrapRendererModel asModel(IModel<? extends T> valueModel);

}