package org.iglooproject.wicket.bootstrap4.markup.html.bootstrap;

import java.util.function.Supplier;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.bootstrap4.markup.html.bootstrap.component.BootstrapBadge;
import org.iglooproject.wicket.more.application.IWicketBootstrapComponentsModule;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;

@org.springframework.stereotype.Component
public class WicketBootstrapComponentsModule implements IWicketBootstrapComponentsModule {

	@Override
	public <T> Supplier<BootstrapBadge<T>> badgeSupplier(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		return new Supplier<BootstrapBadge<T>>() {
			@Override
			public BootstrapBadge<T> get() {
				return new BootstrapBadge<>(id, model, renderer);
			}
		};
	}

	@Deprecated
	@Override
	public <T> Supplier<BootstrapBadge<T>> labelSupplier(String id, IModel<T> model, BootstrapRenderer<? super T> renderer) {
		return new Supplier<BootstrapBadge<T>>() {
			@Override
			public BootstrapBadge<T> get() {
				return new BootstrapBadge<>(id, model, renderer);
			}
		};
	}

}
