package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;

public abstract class UserActiveRenderer extends BootstrapRenderer<User> {

	private static final long serialVersionUID = 8417578372352258838L;

	private static final UserActiveRenderer INSTANCE = new UserActiveRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		protected BootstrapRendererInformation doRender(User value, Locale locale) {
			if (value == null) {
				return null;
			}
			if (value.isActive()) {
				return BootstrapRendererInformation.builder()
						.label(getString("business.user.active.true", locale))
						.icon("fa fa-check fa-fw")
						.color(BootstrapColor.SUCCESS)
						.build();
			} else {
				return BootstrapRendererInformation.builder()
						.label(getString("business.user.active.false", locale))
						.icon("fa fa-times fa-fw")
						.color(BootstrapColor.SECONDARY)
						.build();
			}
		}
	};

	public static final UserActiveRenderer get() {
		return INSTANCE;
	}

	private UserActiveRenderer() {
	}

}
