package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import java.util.Locale;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.model.LocaleAwareReadOnlyModel;
import fr.openwide.core.wicket.more.rendering.Renderer;

public abstract class BootstrapRenderer<T> extends Renderer<T> {

	private static final long serialVersionUID = -715674989551497434L;

	public static <T> BootstrapRenderer<T> constant(final String resourceKey, final String icon, final IBootstrapColor color) {
		return constant(resourceKey, resourceKey, icon, color);
	}

	public static <T> BootstrapRenderer<T> constant(final String labelResourceKey, final String tooltipResourceKey, final String icon, final IBootstrapColor color) {
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation doRender(T value, Locale locale) {
				return BootstrapRendererInformation.builder()
						.label(getString(labelResourceKey, locale))
						.tooltip(getString(tooltipResourceKey, locale))
						.icon(icon)
						.color(color)
						.build();
			}
		};
	}

	public BootstrapRenderer() {
		super();
	}

	protected abstract BootstrapRendererInformation doRender(T value, Locale locale);

	@Override
	public String render(T value, Locale locale) {
		return doRender(value, locale).getLabel();
	}

	public final IModel<String> asIconCssClassModel(final IModel<? extends T> model) {
		return new LocaleAwareReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject(Locale locale) {
				return doRender(model.getObject(), locale).getIconCssClass();
			}
		};
	}

	public final IModel<IBootstrapColor> asColorModel(final IModel<? extends T> model) {
		return new LocaleAwareReadOnlyModel<IBootstrapColor>() {
			private static final long serialVersionUID = 1L;
			@Override
			public IBootstrapColor getObject(Locale locale) {
				return doRender(model.getObject(), locale).getColor();
			}
		};
	}

	public final IModel<String> asTooltipModel(final IModel<? extends T> model) {
		return new LocaleAwareReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject(Locale locale) {
				return doRender(model.getObject(), locale).getTooltip();
			}
		};
	}

}
