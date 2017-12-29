package org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer;

import java.util.Locale;

import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import org.iglooproject.wicket.more.rendering.Renderer;

/**
 * @deprecated Will be removed. Use {@link BootstrapRenderer} instead.
 */
@Deprecated
public abstract class BootstrapLabelRenderer<T> extends BootstrapRenderer<T> {

	private static final long serialVersionUID = 5966093285228006373L;

	protected static final Renderer<Object> DEFAULT_TOOLTIP_RENDERER = Renderer.constant(null);

	public BootstrapLabelRenderer() {
		super();
	}

	@Override
	public abstract String render(T value, Locale locale);

	protected abstract IBootstrapColor getColor(T value);

	protected abstract String getIconCssClass(T value);

	public Renderer<? super T> getTooltipRenderer() {
		return DEFAULT_TOOLTIP_RENDERER;
	}

	@Override
	protected final BootstrapRendererInformation doRender(T value, Locale locale) {
		return BootstrapRendererInformation.builder()
				.label(render(value, locale))
				.color(getColor(value))
				.icon(getIconCssClass(value))
				.tooltip(getTooltipRenderer().render(value, locale))
				.build();
	}
	
	public IModel<String> asIconCssClassModel(IModel<? extends T> valueModel) {
		return asModel(valueModel).getIconCssClassModel();
	}
	
	public IModel<IBootstrapColor> asColorModel(IModel<? extends T> valueModel) {
		return asModel(valueModel).getColorModel();
	}

}
