package org.iglooproject.basicapp.web.application.common.renderer;

import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;

import igloo.bootstrap.common.BootstrapColor;
import igloo.bootstrap.common.IBootstrapColor;
import igloo.bootstrap.renderer.IBootstrapRenderer;

public final class ActionRenderers {

	public static <T> IBootstrapRenderer<T> constant(final String resourceKey, final String icon, final IBootstrapColor color) {
		return constant(resourceKey, resourceKey, icon, color);
	}

	public static <T> IBootstrapRenderer<T> constant(final String labelResourceKey, final String tooltipResourceKey, final String icon, final IBootstrapColor color) {
		return BootstrapRenderer.constant(labelResourceKey, tooltipResourceKey, icon, color);
	}

	public static <T> IBootstrapRenderer<T> add() {
		return constant("common.action.add", "fa fa-fw fa-plus", BootstrapColor.PRIMARY);
	}

	public static <T> IBootstrapRenderer<T> edit() {
		return constant("common.action.edit", "fa fa-fw fa-pencil-alt", BootstrapColor.PRIMARY);
	}

	public static <T> IBootstrapRenderer<T> remove() {
		return constant("common.action.remove", "fa fa-fw fa-times", BootstrapColor.DANGER);
	}

	public static <T> IBootstrapRenderer<T> delete() {
		return constant("common.action.delete", "fa fa-fw fa-trash-alt", BootstrapColor.DANGER);
	}

	public static <T> IBootstrapRenderer<T> enable() {
		return constant("common.action.enable", "fa fa-fw fa-toggle-on", BootstrapColor.SUCCESS);
	}

	public static <T> IBootstrapRenderer<T> disable() {
		return constant("common.action.disable", "fa fa-fw fa-toggle-off", BootstrapColor.DANGER);
	}

	public static <T> IBootstrapRenderer<T> view() {
		return constant("common.action.view", "fa fa-fw fa-search", BootstrapColor.SECONDARY);
	}

	private ActionRenderers() {
	}

}
