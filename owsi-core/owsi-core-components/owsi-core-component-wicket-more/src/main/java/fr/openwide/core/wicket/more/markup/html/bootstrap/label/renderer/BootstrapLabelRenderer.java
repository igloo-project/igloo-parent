package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import java.util.Locale;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.rendering.Renderer;

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

	public abstract IBootstrapColor getColor(T value);

	public abstract String getIconCssClass(T value);

	public Renderer<? super T> getTooltipRenderer() {
		return DEFAULT_TOOLTIP_RENDERER;
	}

	@Override
	protected BootstrapRendererInformation doRender(T value, Locale locale) {
		return BootstrapRendererInformation.builder()
				.label(render(value, locale))
				.color(getColor(value))
				.icon(getIconCssClass(value))
				.tooltip(getTooltipRenderer().render(value, locale))
				.build();
	}

}
