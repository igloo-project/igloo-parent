package fr.openwide.core.basicapp.web.application.common.renderer;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

public final class ActionRenderers {

	public static <T> BootstrapRenderer<T> validate() {
		return BootstrapRenderer.with(
				BootstrapRendererInformation.builder()
						.label("common.action.validate")
						.tooltip("common.action.validate")
						.icon("fa fa-fw fa-check")
						.color(BootstrapColor.SUCCESS)
						.build()
		);
	}

	public static <T> BootstrapRenderer<T> add() {
		return BootstrapRenderer.with(
				BootstrapRendererInformation.builder()
						.label("common.action.add")
						.tooltip("common.action.add")
						.icon("fa fa-fw fa-plus")
						.color(BootstrapColor.PRIMARY)
						.build()
		);
	}

	public static <T> BootstrapRenderer<T> remove() {
		return BootstrapRenderer.with(
				BootstrapRendererInformation.builder()
						.label("common.action.remove")
						.tooltip("common.action.remove")
						.icon("fa fa-fw fa-trash-o")
						.color(BootstrapColor.DANGER)
						.build()
		);
	}

	public static <T> BootstrapRenderer<T> enable() {
		return BootstrapRenderer.with(
				BootstrapRendererInformation.builder()
						.label("common.action.enable")
						.tooltip("common.action.enable")
						.icon("fa fa-fw fa-toggle-on")
						.color(BootstrapColor.SUCCESS)
						.build()
		);
	}

	public static <T> BootstrapRenderer<T> disable() {
		return BootstrapRenderer.with(
				BootstrapRendererInformation.builder()
						.label("common.action.disable")
						.tooltip("common.action.disable")
						.icon("fa fa-fw fa-toggle-off")
						.color(BootstrapColor.DANGER)
						.build()
		);
	}

}
