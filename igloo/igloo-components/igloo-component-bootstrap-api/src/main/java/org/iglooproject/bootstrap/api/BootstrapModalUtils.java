package org.iglooproject.bootstrap.api;

import java.util.ServiceLoader;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public final class BootstrapModalUtils {

	public static Component asComponent(IModalPopupPanel modal) {
		return (Component) modal;
	}

	public static final JsStatement show(Component modal) {
		return show(modal, null);
	}

	public static final JsStatement show(Component modal, IBootstrapModal options) {
		if (options == null) {
			return new JsStatement().$(modal).chain(modal()).append(";");
		} else {
			return new JsStatement().$(modal).chain(options).append(";");
		}
	}

	public static final IBootstrapModal modal() {
		// TODO: how to use the right implementation
		return ServiceLoader.load(IBootstrapModal.class).findFirst().orElseThrow();
	}

	public static final JsStatement hide(Component modal) {
		return new JsStatement().$(modal).chain("hide").append(";");
	}

	private BootstrapModalUtils() {
	}

}
