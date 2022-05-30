package org.iglooproject.bootstrap.api;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.iglooproject.bootstrap.api.badge.IBootstrapBadge;
import org.iglooproject.bootstrap.api.renderer.IBootstrapRenderer;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.sass.service.IScssService;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public interface IBootstrapProvider {

	BootstrapVersion getVersion();

	void registerImportScopes(IScssService scssService);

	void updateResourceBundles(ResourceBundles resourceBundles);

	void openModalOnClickRenderHead(Component component, IHeaderResponse response,
			IModalPopupPanel modal,
			JsStatement onModalStart, JsStatement onModalComplete, JsStatement onModalShow, JsStatement onModalHide);

	void renderHead(Component component, IHeaderResponse response);

	void modalRenderHead(Component component, IHeaderResponse response);

	Class<?> modalMarkupClass();

	IBootstrapModal createModal();

	default Component asComponent(IModalPopupPanel modal) {
		return (Component) modal;
	}

	default JsStatement show(Component modal) {
		return show(modal, null);
	}

	default JsStatement show(Component modal, IBootstrapModal options) {
		if (options == null) {
			return new JsStatement().$(modal).chain(modal()).append(";");
		} else {
			return new JsStatement().$(modal).chain(options).append(";");
		}
	}

	default IBootstrapModal modal() {
		return createModal();
	}

	default JsStatement hide(Component modal) {
		return new JsStatement().$(modal).chain("hide").append(";");
	}

	<T> SerializableSupplier2<? extends IBootstrapBadge<T, ? extends IBootstrapBadge<T, ?>>> badgeSupplier(String id, IModel<T> model, final IBootstrapRenderer<? super T> renderer);

	void confirmRenderHead(Component component, IHeaderResponse response);

	JsStatement confirmStatement(Component component);

}
