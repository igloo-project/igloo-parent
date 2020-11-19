package org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.IBootstrapColor;
import org.iglooproject.wicket.more.util.model.Detachables;

public class BootstrapColorBehavior extends ClassAttributeAppender {

	private static final long serialVersionUID = 7272137227196691195L;

	public static BootstrapColorBehavior text(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("text-", colorModel);
	}

	public static BootstrapColorBehavior badge(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("badge badge-", colorModel);
	}

	public static BootstrapColorBehavior alert(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("alert alert-", colorModel);
	}

	public static BootstrapColorBehavior bg(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("bg-", colorModel);
	}

	public static BootstrapColorBehavior btn(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("btn btn-", colorModel);
	}

	public static BootstrapColorBehavior btnOutline(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("btn btn-outline-", colorModel);
	}

	public BootstrapColorBehavior(final String cssClassPrefix, final IModel<IBootstrapColor> colorModel) {
		super(new IModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				IBootstrapColor color = colorModel.getObject();
				if (color == null) {
					return null;
				} else {
					return cssClassPrefix + color.getCssClassSuffix();
				}
			}
			
			@Override
			public void detach() {
				IModel.super.detach();
				Detachables.detach(colorModel);
			}
		});
	}

}
