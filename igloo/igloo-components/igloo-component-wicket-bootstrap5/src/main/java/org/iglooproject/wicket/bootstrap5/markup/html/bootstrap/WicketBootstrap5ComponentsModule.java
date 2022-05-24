package org.iglooproject.wicket.bootstrap5.markup.html.bootstrap;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap5.markup.html.bootstrap.component.BootstrapBadge;
import org.iglooproject.wicket.more.application.IWicketBootstrapComponentsModule;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;

public class WicketBootstrap5ComponentsModule implements IWicketBootstrapComponentsModule {

	@Override
	public <T> SerializableSupplier2<BootstrapBadge<T>> badgeSupplier(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		return () -> new BootstrapBadge<>(id, model, renderer);
	}

}
