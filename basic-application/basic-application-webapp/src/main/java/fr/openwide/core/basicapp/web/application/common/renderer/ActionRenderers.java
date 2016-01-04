package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

public final class ActionRenderers {

	public static <T> BootstrapRenderer<T> validate() {
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation doRender(T value, Locale locale) {
				return BootstrapRendererInformation.builder()
						.label(getString("common.action.validate", locale))
						.tooltip(getString("common.action.validate", locale))
						.icon("fa fa-fw fa-check")
						.color(BootstrapColor.SUCCESS)
						.build();
			}
		};
	}

	public static <T> BootstrapRenderer<T> add() {
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation doRender(T value, Locale locale) {
				return BootstrapRendererInformation.builder()
						.label(getString("common.action.add", locale))
						.tooltip(getString("common.action.add", locale))
						.icon("fa fa-fw fa-plus")
						.color(BootstrapColor.PRIMARY)
						.build();
			}
		};
	}

	public static <T> BootstrapRenderer<T> remove() {
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation doRender(T value, Locale locale) {
				return BootstrapRendererInformation.builder()
						.label(getString("common.action.remove", locale))
						.tooltip(getString("common.action.remove", locale))
						.icon("fa fa-fw fa-trash-o")
						.color(BootstrapColor.DANGER)
						.build();
			}
		};
	}

	public static <T> BootstrapRenderer<T> enable() {
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation doRender(T value, Locale locale) {
				return BootstrapRendererInformation.builder()
						.label(getString("common.action.enable", locale))
						.tooltip(getString("common.action.enable", locale))
						.icon("fa fa-fw fa-toggle-on")
						.color(BootstrapColor.SUCCESS)
						.build();
			}
		};
	}

	public static <T> BootstrapRenderer<T> disable() {
		return new BootstrapRenderer<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected BootstrapRendererInformation doRender(T value, Locale locale) {
				return BootstrapRendererInformation.builder()
						.label(getString("common.action.disable", locale))
						.tooltip(getString("common.action.disable", locale))
						.icon("fa fa-fw fa-toggle-off")
						.color(BootstrapColor.DANGER)
						.build();
			}
		};
	}

}
