package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.rendering.Renderer;

public final class ActionRenderers {

	private static final class ValidateRenderer<T> extends BootstrapLabelRenderer<T> {
		private static final long serialVersionUID = 1L;
		@Override
		public IBootstrapColor getColor(T value) {
			return BootstrapColor.SUCCESS;
		}
		@Override
		public String getIconCssClass(T value) {
			return "fa fa-fw fa-check";
		}
		@Override
		public String render(T value, Locale locale) {
			return getString("common.action.validate", locale);
		}
		@Override
		public Renderer<T> getTooltipRenderer() {
			return Renderer.fromResourceKey("common.action.validate");
		}
	}

	public static <T> BootstrapLabelRenderer<T> validate() {
		return new ValidateRenderer<>();
	}

	private static final class AddRenderer<T> extends BootstrapLabelRenderer<T> {
		private static final long serialVersionUID = 1L;
		@Override
		public IBootstrapColor getColor(T value) {
			return BootstrapColor.PRIMARY;
		}
		@Override
		public String getIconCssClass(T value) {
			return "fa fa-fw fa-plus";
		}
		@Override
		public String render(T value, Locale locale) {
			return getString("common.action.add", locale);
		}
		@Override
		public Renderer<T> getTooltipRenderer() {
			return Renderer.fromResourceKey("common.action.add");
		}
	};
	
	public static <T> BootstrapLabelRenderer<T> add() {
		return new AddRenderer<>();
	}

	private static final class RemoveRenderer<T> extends BootstrapLabelRenderer<T> {
		private static final long serialVersionUID = 1L;
		@Override
		public IBootstrapColor getColor(T value) {
			return BootstrapColor.DANGER;
		}
		@Override
		public String getIconCssClass(T value) {
			return "fa fa-fw fa-trash-o";
		}
		@Override
		public String render(T value, Locale locale) {
			return getString("common.action.remove", locale);
		}
		@Override
		public Renderer<Object> getTooltipRenderer() {
			return Renderer.fromResourceKey("common.action.remove");
		}
	};
	
	public static <T> BootstrapLabelRenderer<T> remove() {
		return new RemoveRenderer<>();
	}

	private static final class EnableRenderer<T> extends BootstrapLabelRenderer<T> {
		private static final long serialVersionUID = 1L;
		@Override
		public IBootstrapColor getColor(T value) {
			return BootstrapColor.SUCCESS;
		}
		@Override
		public String getIconCssClass(T value) {
			return "fa fa-fw fa-toggle-on";
		}
		@Override
		public String render(T value, Locale locale) {
			return getString("common.action.enable", locale);
		}
		@Override
		public Renderer<T> getTooltipRenderer() {
			return Renderer.fromResourceKey("common.action.enable");
		}
	};
	
	public static <T> BootstrapLabelRenderer<T> enable() {
		return new EnableRenderer<>();
	}

	private static final class DisableRenderer<T> extends BootstrapLabelRenderer<T> {
		private static final long serialVersionUID = 1L;
		@Override
		public IBootstrapColor getColor(T value) {
			return BootstrapColor.DANGER;
		}
		@Override
		public String getIconCssClass(T value) {
			return "fa fa-fw fa-toggle-off";
		}
		@Override
		public String render(T value, Locale locale) {
			return getString("common.action.disable", locale);
		}
		@Override
		public Renderer<T> getTooltipRenderer() {
			return Renderer.fromResourceKey("common.action.disable");
		}
	};
	
	public static <T> BootstrapLabelRenderer<T> disable() {
		return new DisableRenderer<>();
	}

}
