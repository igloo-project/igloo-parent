package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.rendering.Renderer;

public abstract class BootstrapLabelRenderer<T> extends Renderer<T> {

	private static final long serialVersionUID = 5966093285228006373L;

	public BootstrapLabelRenderer() {
		super();
	}

	public abstract IBootstrapColor getColor(T value);

	public final IModel<IBootstrapColor> asColorModel(final IModel<? extends T> model) {
		return new AbstractReadOnlyModel<IBootstrapColor>() {
			private static final long serialVersionUID = 4329152375254189580L;
			@Override
			public IBootstrapColor getObject() {
				return getColor(model.getObject());
			}
			@Override
			public void detach() {
				super.detach();
				model.detach();
			}
		};
	}

	public abstract String getIconCssClass(T value);

	public final IModel<String> asIconCssClassModel(final IModel<? extends T> model) {
		return new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 4329152375254189580L;
			@Override
			public String getObject() {
				return getIconCssClass(model.getObject());
			}
			@Override
			public void detach() {
				super.detach();
				model.detach();
			}
		};
	}

}
