package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import java.util.Locale;
import java.util.Objects;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.rendering.Renderer;

public abstract class BootstrapRenderer<T> extends Renderer<T> {

	private static final long serialVersionUID = -715674989551497434L;

	public BootstrapRenderer() {
		super();
		nullsAsNull();
	}

	public static final <T> BootstrapRenderer<T> with(final BootstrapRendererInformation informationModel) {
		Objects.requireNonNull(informationModel);
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation with(T value) {
				return informationModel;
			}
		};
	}

	protected abstract BootstrapRendererInformation with(T value);

	@Override
	public String render(T value, Locale locale) {
		return with(value).getLabelRenderer().render(value, locale);
	}

	public final IModel<String> asIconCssClassModel(final IModel<? extends T> model) {
		return new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 4329152375254189580L;
			@Override
			public String getObject() {
				return with(model.getObject()).getIconCssClass();
			}
			@Override
			public void detach() {
				super.detach();
				model.detach();
			}
		};
	}

	public final IModel<IBootstrapColor> asColorModel(final IModel<? extends T> model) {
		return new AbstractReadOnlyModel<IBootstrapColor>() {
			private static final long serialVersionUID = 4329152375254189580L;
			@Override
			public IBootstrapColor getObject() {
				return with(model.getObject()).getColor();
			}
			@Override
			public void detach() {
				super.detach();
				model.detach();
			}
		};
	}

	public Renderer<? super T> getTooltipRenderer(final IModel<? extends T> model) {
		return with(model.getObject()).getTooltipRenderer();
	}

}
