package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;

public abstract class GeneralMessageActiveRenderer extends BootstrapRenderer<GeneralMessage> {

	private static final long serialVersionUID = 8417578372352258838L;

	private static final GeneralMessageActiveRenderer INSTANCE = new GeneralMessageActiveRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		protected BootstrapRendererInformation doRender(GeneralMessage value, Locale locale) {
			if (value == null) {
				return null;
			}
			if (value.isActive()) {
				return BootstrapRendererInformation.builder()
						.label(getString("business.generalMessage.active.true", locale))
						.icon("fa fa-check fa-fw")
						.color(BootstrapColor.SUCCESS)
						.build();
			} else {
				return BootstrapRendererInformation.builder()
						.label(getString("business.generalMessage.active.false", locale))
						.icon("fa fa-ban fa-fw")
						.color(BootstrapColor.SECONDARY)
						.build();
			}
		}
	};

	public static final GeneralMessageActiveRenderer get() {
		return INSTANCE;
	}

	private GeneralMessageActiveRenderer() {
	}

}
